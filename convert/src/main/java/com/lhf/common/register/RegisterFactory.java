package com.lhf.common.register;

import com.lhf.common.converter.Converter;
import com.lhf.common.utils.Assert;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

/**
 * 简单注册工厂
 *
 * @author lhf
 * @date 2018/7/9
 */
public class RegisterFactory {

    private static ConverterRegister mapstructRegister = new MapstructConverterRegister();

    private static ConverterRegister generalRegister = new GeneralConverterRegister();

    public static void mapstructRegister(Class<?> mapstructConvert) {
        mapstructRegister.register(Mappers.getMapper(mapstructConvert));
    }

    public static void generalRegister(Collection<Converter> converters) {
        Assert.notEmpty(converters);
        converters.forEach(converter -> generalRegister.register(converter));
    }

    public static void springRegister(Object mapstructConvertInstance) {
        mapstructRegister.register(mapstructConvertInstance);
    }
}
