package cn.ld.networkmonitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.ld.networkmonitor.type.NetworkType;

/**
 * Created by Administrator on 2019/8/14.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Network {

    NetworkType netType() default NetworkType.NONE;
}
