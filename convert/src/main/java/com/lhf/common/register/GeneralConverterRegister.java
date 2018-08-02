package com.lhf.common.register;



import com.lhf.common.Converters;
import com.lhf.common.converter.Converter;
import com.lhf.common.utils.Assert;

/**
 * 普通的convert注册器
 *
 * @author lhf
 * @date 2018/7/10
 */

public class GeneralConverterRegister implements ConverterRegister {

    @Override
    public void register(Object convertInstance) {
        Assert.isInstanceOf(Converter.class, convertInstance, "must instanceof Converter");

        Converter.Meta meta = ((Converter) convertInstance).meta();
        Class<?> targetClass = meta.getTargetClass();

        Assert.isTrue(!targetClass.equals(Void.TYPE), "target class must not be void");

        Converters.register((Converter) convertInstance);
    }
}
