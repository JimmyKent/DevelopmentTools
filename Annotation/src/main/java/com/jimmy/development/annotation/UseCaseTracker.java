package com.jimmy.development.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jinguochong on 2017/9/18.
 */

public class UseCaseTracker {

    public static void trackUseCase(List<Integer> useCases, Class<?> cl) {
        for (Method m : cl.getDeclaredMethods()) {
            UseCase uc = m.getAnnotation(UseCase.class);
            if (uc != null) {
                System.out.println("Found Use Case : " + uc.id() + "  " + uc.desc());
                useCases.remove(new Integer(uc.id()));
            }
        }

        for (int i : useCases) {
            System.out.println("Warning : Missing use case - " + i);
        }
    }

    public static void main(String[] args) {

        List<Integer> useCases = new ArrayList<>();
        Collections.addAll(useCases, 47, 48, 49, 50);
        trackUseCase(useCases, PwdUtils.class);
    }
}
