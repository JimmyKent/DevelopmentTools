package com.jimmy.development.tools;

import android.util.Log;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Reflect {
    private static final String TAG = Reflect.class.getSimpleName();

    private static final HashMap<String, Constructor<?>> sConstructorCache = new HashMap<String, Constructor<?>>();
    private static final HashMap<String, Method> sMethodCache = new HashMap<String, Method>();
    private static final HashMap<String, Field> sFieldCache = new HashMap<String, Field>();

    private final Object object;
    private final boolean isClass;

    private Reflect(Class<?> type) {
        object = type;
        isClass = true;
    }

    private Reflect(Object obj) {
        object = obj;
        isClass = false;
    }

    private static Class<?> forName(String name) throws ReflectException {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ReflectException(e);
        }
    }

    /**
     * @param className 完整的类名
     * @return
     * @throws ReflectException
     */
    public static Reflect on(String className) throws ReflectException {
        return on(forName(className));
    }

    public static Reflect on(Class<?> clazz) {
        return new Reflect(clazz);
    }

    public static Reflect on(Object object) {
        return new Reflect(object);
    }

    private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException {
        try {
            return on(accessible(constructor).newInstance(args));
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    /**
     * @param className 完整的类名
     * @param args
     * @return
     * @throws ReflectException
     */
    public static Reflect newInstance(String className, Object... args) throws ReflectException {
        try {
            Class<?> clazz = Class.forName(className);
            return Reflect.on(clazz).newInstance(args);
        } catch (ClassNotFoundException e) {
            throw new ReflectException(e);
        }
    }

    /**
     * 调用一个有参构造器
     *
     * @param args 构造器参数
     * @return 工具类自身
     * @throws ReflectException
     */
    public Reflect newInstance(Object... args) throws ReflectException {
        Constructor<?> constructor = null;
        Class<?>[] types = parseTypes(args);

        Class<?> clazz = type();
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append(parametersString(types));
        sb.append("#exact");
        String fullConstructorName = sb.toString();

        if (sConstructorCache.containsKey(fullConstructorName)) {
            constructor = sConstructorCache.get(fullConstructorName);
            if (constructor == null) {
                throw new ReflectException("Not such constructor exception : " + fullConstructorName);
            }
        } else {
            try {
                constructor = clazz.getDeclaredConstructor(types);
                sConstructorCache.put(fullConstructorName, constructor);
            } catch (NoSuchMethodException e) {
                //这种情况下，构造器往往是私有的，多用于工厂方法，刻意的隐藏了构造器。
                //private阻止不了反射的脚步:)
                for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
                    if (parameterTypesMatch(ctor.getParameterTypes(), types)) {
                        constructor = ctor;
                        sConstructorCache.put(fullConstructorName, constructor);
                        break;
                    }
                }
            }
        }

        if (constructor != null) {
            return on(constructor, args);
        } else {
            throw new ReflectException("Not such constructor exception : " + fullConstructorName);
        }
    }

    /**
     * 得到包装的对象的类型，
     * 如果是基本类型,像int,float,boolean这种,
     * 那么将被转换成相应的对象类型。
     */
    public static Class<?> wrapper(Class<?> type) {
        if (type == null) return null;

        if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }

        return type;
    }

    private static Reflect excute(Object object, Method method, Object... args) throws ReflectException {
        try {
            accessible(method);
            if (method.getReturnType() == void.class) {
                method.invoke(object, args);
                return on(object);
            } else {
                return on(method.invoke(object, args));
            }
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    /**
     * 使受访问权限限制的对象转为不受限制。
     * 一般情况下，一个类的私有字段和方法是无法获取和调用的，
     * 原因在于调用前Java会检查是否具有可访问权限，
     * 当调用此方法后，访问权限检查机制将被关闭。
     *
     * @param accessible 受访问限制的对象
     * @return 不受访问限制的对象
     */
    public static <T extends AccessibleObject> T accessible(T accessible) {
        if (accessible == null) {
            return null;
        }

        if (accessible instanceof Member) {
            Member member = (Member) accessible;

            if (Modifier.isPublic(member.getModifiers()) &&
                    Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
                return accessible;
            }
        }

        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }

        return accessible;
    }

    private static Class<?>[] parseTypes(Object[] values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            Object obj = values[i];
            result[i] = obj == null ? NULL.class : obj.getClass();
        }

        return result;
    }

    public Reflect call(String methodName) {
        return call(methodName, new Object[0]);
    }

    public Reflect call(String methodName, Object... args) throws ReflectException {
        Class<?> clazz = type();
        Class<?>[] types = parseTypes(args);

        try {
            Method method = findMethodBestMatch(clazz, methodName, types);
            return excute(object, method, args);
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }

    }

    public Reflect call(String methodName, Class[] types, Object... args) throws ReflectException {
        Class<?> clazz = type();
        try {
            Method method = findMethodBestMatch(clazz, methodName, types);
            return excute(object, method, args);
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    /**
     * Look up a method in a class and set it to accessible. The result is cached.
     * This does not only look for exact matches, but for the closest match.
     * If the method was not found, a {@link NoSuchMethodError} will be thrown.
     */
    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(methodName);
        sb.append(parametersString(parameterTypes));
        sb.append("#bestmatch");
        String fullMethodName = sb.toString();

        Method method;

        if (sMethodCache.containsKey(fullMethodName)) {
            method = sMethodCache.get(fullMethodName);
            if (method == null) {
                throw new NoSuchElementException(fullMethodName);
            }

            return method;
        }

        try {
            method = exactMethod(clazz, methodName, parameterTypes);
            sMethodCache.put(fullMethodName, method);
        } catch (NoSuchMethodException e) {
            try {
                method = similarMethod(clazz, methodName, parameterTypes);
                sMethodCache.put(fullMethodName, method);
            } catch (NoSuchMethodException ignore) {
                sMethodCache.put(fullMethodName, null);
                throw e;
            }
        }

        return method;
    }

    private static String parametersString(Class<?>... clazzes) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (Class<?> clazz : clazzes) {
            if (first)
                first = false;
            else
                sb.append(",");

            if (clazz != null)
                sb.append(clazz.getCanonicalName());
            else
                sb.append("null");
        }
        sb.append(")");
        return sb.toString();
    }

    private static boolean parameterTypesMatch(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == NULL.class)
                    continue;

                if (wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i])))
                    continue;

                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * 再次确认方法签名与实际是否匹配，
     * 将基本类型转换成对应的对象类型，
     * 如int转换成Int
     */
    private static boolean isSimilarSignature(Method possiblyMatchingMethod, String desiredMethodName, Class<?>[] desiredParamTypes) {
        return possiblyMatchingMethod.getName().equals(desiredMethodName) && parameterTypesMatch(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
    }

    /**
     * 给定方法名和参数，匹配一个最接近的方法
     */
    private static Method similarMethod(Class<?> clazz, String methodName, Class<?>[] types) throws NoSuchMethodException {

        //对于公有方法:
        for (Method method : clazz.getMethods()) {
            if (isSimilarSignature(method, methodName, types)) {
                return method;
            }
        }

        //对于私有方法：
        do {
            for (Method method : clazz.getDeclaredMethods()) {
                if (isSimilarSignature(method, methodName, types)) {
                    return method;
                }
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null && !clazz.equals(Object.class));

        throw new NoSuchMethodException("No similar method " + methodName + " with params " + Arrays.toString(types) + " could be found on type " + clazz + ".");
    }

    private static Method exactMethod(Class<?> clazz, String methodName, Class<?>[] types) throws NoSuchMethodException {

        try {// 尝试直接调用
            return clazz.getMethod(methodName, types);
        } catch (NoSuchMethodException e) {// 私有方法
            do {
                try {
                    return clazz.getDeclaredMethod(methodName, types);
                } catch (NoSuchMethodException ignore) {
                    if (clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)) {
                        //ignore.printStackTrace();
                        Log.w(TAG, "message :" + ignore.toString());
                    }
                }

                clazz = clazz.getSuperclass();
            } while (clazz != null);

            throw new NoSuchMethodException();
        }
    }

    public <T> T get() {
        return (T) object;
    }

    public <T> T get(String fieldName) {
        return field(fieldName).<T>get();
    }

    private Reflect field(String fieldName) throws ReflectException {
        try {
            Field field = findField(fieldName);
            return on(field.get(object));
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    private Field findField(String fieldName) {
        Class<?> clazz = type();

        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(fieldName);
        String fullFieldName = sb.toString();

        Field field;
        if (sFieldCache.containsKey(fullFieldName)) {
            field = sFieldCache.get(fullFieldName);
            if (field == null) {
                throw new ReflectException("no such field exception : " + fullFieldName);
            }
            return field;
        }

        try {
            field = findFieldRecursiveImpl(clazz, fieldName);
            sFieldCache.put(fullFieldName, field);
            return field;
        } catch (NoSuchFieldException e) {
            sFieldCache.put(fullFieldName, null);
            throw new ReflectException(e);
        }
    }


    public static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        try {
            // only consider the specified class by using getDeclaredField()
            final Field field = cls.getDeclaredField(fieldName);
            if (!isAccessible(field)) {
                if (forceAccess) {
                    field.setAccessible(true);
                } else {
                    return null;
                }
            }
            return field;
        } catch (final NoSuchFieldException e) { // NOPMD
            // ignore
        }
        return null;
    }

    static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }

    private static Field findFieldRecursiveImpl(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {// 查询公有字段
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {// 查询私有字段
            do {
                try {
                    return accessible(clazz.getDeclaredField(fieldName));
                } catch (NoSuchFieldException ignore) {
                    if (clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)) {
                        //ignore.printStackTrace();
                        Log.w(TAG, "message :" + ignore.toString());
                    }
                }

                clazz = clazz.getSuperclass();
            } while (clazz != null && !clazz.equals(Object.class));

            throw e;
        }
    }

    private Class<?> type() {
        if (isClass) {
            return (Class<?>) object;
        } else {
            return object.getClass();
        }
    }

    public Reflect set(String fieldName, Object value) throws ReflectException {
        try {
            Field field = findField(fieldName);
            field.set(object, value);
            return this;
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    /**
     * 定义了一个null类型
     */
    private static class NULL {
    }
}
