package net.myspring.util.cahe.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liuj on 2017/3/31.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface CacheInput {
    /**
     * 缓存中对应的Key
     */
    String inputKey() default "";

    /**
     * 缓存中值对应的字段
     */
    String inputInstance() default "";

    /**
     * 缓存反序列化后获取值对应的字段
     */
    String outputInstance() default "";
}
