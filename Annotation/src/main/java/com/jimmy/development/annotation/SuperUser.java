package com.jimmy.development.annotation;

/**
 * Created by jinguochong on 2017/9/24.
 */

public @interface SuperUser {
    public UseCase use() default @UseCase(id = -1);//有元素没有默认值
    public UseCase2 use2() default @UseCase2;//所有元素都有默认值
}
