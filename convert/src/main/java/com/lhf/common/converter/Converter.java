package com.lhf.common.converter;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 转换器接口
 *
 * @author lhf
 * @date 2018/7/9
 */
public interface Converter<S, T> {

    /**
     * 转换
     * 
     * @param source 源对象
     * @return 目标对象
     * @throws ConvertException 转换异常
     */
    T convert(S source) throws ConvertException;

    /**
     * 合并
     * 
     * @param target 目标对象
     * @param source 源对象
     * @return 转换异常
     */
    T merge(T target, S source) throws ConvertException;

    /**
     * 获取转换器元数据
     * 
     * @return 元数据
     */
    Meta meta();

    @Data
    @Builder
    class Meta {
        private String converterName;

        @NonNull
        private Class<?> sourceClass;

        @NonNull
        private Class<?> targetClass;

        @NonNull
        private ConverterEnum action;
    }
}
