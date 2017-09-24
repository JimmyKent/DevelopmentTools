package com.jimmy.development.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

/**
 * Created by jinguochong on 2017/9/16.
 */

public class AnnotationLearning {

//    @Target           注解用在什么地方
//    ANNOTATION_TYPE,
//    CONSTRUCTOR,      构造器
//    FIELD,            域声明(包括enum实例)
//    LOCAL_VARIABLE,   局部声明
//    METHOD,           方法声明
//    PACKAGE,          包声明
//    PARAMETER,        参数声明
//    TYPE,             类,接口(包括注解类型)或者enum声明
//    TYPE_PARAMETER,
//    TYPE_USE;

//    @Retention()      需要在什么级别保存该注解信息:
//    CLASS             在class文件可用,但是被VM丢弃
//    SOURCE            编译期
//    RUNTIME           运行期
// 编译期和运行期

//    @Documented       将此注解包含在Javadoc里面

//    @Inherited        允许子类继承父类的注解


    //编写注解处理器


//    注解元素可用的类型:
//    所有基本类型(int, float, boolean etc)
//    String
//    Class
//    enum
//    Annotation 意味着可用嵌套, 如果使用嵌套,那么被嵌套的必须所有元素都有默认值,如果被嵌套的注解存在没有默认值的元素,需要在嵌套的注解的地方加上默认值.见SuperUser
//    以上类型的数组

//    默认值限制:元素不能有不确定的值:要么有默认值,要么用注解时提供的值, 所有非基本类型都不能用null作为default, 比如String 的default 不能是 null

//    生成外部文件, 快捷方式:如果某个类型的值只有一个,比如int, @SQLiteString(30) 就默认value = 30 eg. Member

//    20.2.4 注解不支持继承
//    20.2.5 实现处理器,
//    20.3 apt处理注解    --> XXX 注解在编译之后,是什么样子的? 进过apt新生成的文件是什么样子的?
//    观察者模式用于apt
//    单元测试




}
