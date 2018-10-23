package com.lhf.common.convert.converter;

/**
 * 转换适配
 *
 * @author lhf
 * @date 2018/7/16
 */
public class ConverterAdapter<S, T> implements Converter<S, T> {

    @Override
    public T convert(S source) throws ConvertException {
        throw new UnsupportedOperationException("not impl this method");
    }

    @Override
    public T merge(T target, S source) throws ConvertException {
        throw new UnsupportedOperationException("not impl this method");
    }

    @Override
    public Meta meta() {
        throw new UnsupportedOperationException("must impl this method");
    }
}
