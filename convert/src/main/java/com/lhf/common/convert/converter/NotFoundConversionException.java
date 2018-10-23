package com.lhf.common.convert.converter;

/**
 * 未发现转换器异常
 *
 * @author lhf
 * @date 2018/8/2
 */
public class NotFoundConversionException extends RuntimeException {

    private Class sourceClass;

    private Class targetClass;

    public NotFoundConversionException(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    @Override
    public String getMessage() {
        return "not found conversion: " + sourceClass.getName() + "->" + targetClass.getName();
    }
}
