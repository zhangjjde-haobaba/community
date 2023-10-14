package com.kgc.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String value() default "";

    /**
     * @Target(ElementType.METHOD)
     * @Target 注解用于指定该注解可以应用的Java元素类型。ElementType.METHOD 表示这个注解只能用于方法。其他可能的值包括：
     *
     * ElementType.TYPE: 类、接口（包括注解类型）或枚举声明
     * ElementType.FIELD: 字段声明（包括枚举常量）
     * ElementType.PARAMETER: 参数声明
     * 等等
     * @Retention(RetentionPolicy.RUNTIME)
     * @Retention 注解用于指定该注解的保留策略，即它应该在什么级别可用。RetentionPolicy.RUNTIME 表示这个注解在运行时仍然可用，因此可以通过反射机制读取。
     *
     * 其他可能的值包括：
     *
     * RetentionPolicy.SOURCE: 注解将被编译器丢弃（即它不会有任何语义影响，仅用于在源代码级别提供一些额外的信息）。
     * RetentionPolicy.CLASS: 注解在class文件中可用，但在运行时不可用。
     * @Documented
     * @Documented 注解表明使用该注解的元素应该被javadoc或其他类似工具文档化。这意味着这个注解的信息也会出现在自动生成的文档中。
     */
}
