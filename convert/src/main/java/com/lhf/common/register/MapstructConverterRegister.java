package com.lhf.common.register;

import com.lhf.common.Converters;
import com.lhf.common.converter.ConvertException;
import com.lhf.common.converter.Converter;
import com.lhf.common.converter.ConverterAdapter;
import com.lhf.common.converter.ConverterEnum;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.MappingTarget;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * http://mapstruct.org/documentation/stable/reference/html/
 *
 * @author lhf
 * @date 2018/7/9
 */

@Slf4j
public class MapstructConverterRegister implements ConverterRegister {

    /**
     * 遍历注册转换方法，暂时只支持 T method(obj...) 类型的方法
     * 
     * @param convertInstance mapstruct生成的转换实例，可能包含多个转换方法
     */
    @Override
    public void register(Object convertInstance) {
        Arrays.stream(getOriginalClass(convertInstance).getDeclaredMethods()).forEach(method -> {
            Class<?> targetClass = method.getReturnType();
            Class<?>[] argsClasses = method.getParameterTypes();
            Class mergeTargetClass = Arrays.stream(method.getParameters()).filter(
                    parameter -> parameter.getAnnotation(MappingTarget.class) != null)
                    .findFirst().map(Parameter::getType).orElse(null);

            if(!targetClass.equals(Void.TYPE) && argsClasses.length == 1) {
                registerConverter(argsClasses[0], targetClass, method, convertInstance);
            } else if(mergeTargetClass != null && argsClasses.length > 2) {
                registerMerger(argsClasses, mergeTargetClass, method, convertInstance);
            } else {
                log.warn("only supports for [ T method(obj...) ] converter or merger");
            }
        });
    }

    private Class getOriginalClass(Object instance) {
        Class instanceClass = instance.getClass();
        return Modifier.isAbstract(instanceClass.getSuperclass().getModifiers())
                ? instanceClass.getSuperclass()
                : instanceClass.getInterfaces()[0];
    }

    private void registerConverter(Class sourceClass, Class targetClass, Method method, Object instance) {
        Converter converter = new ConverterAdapter() {
            @Override
            public Object convert(Object source) throws ConvertException {
                Object target;
                try {
                    target = method.invoke(instance, source);
                } catch (Exception e) {
                    throw new ConvertException(source, e);
                }
                return target;
            }

            @Override
            public Converter.Meta meta() {
                return Converter.Meta.builder()
                        .converterName(getName(instance.getClass(), method, "Converter"))
                        .sourceClass(sourceClass)
                        .targetClass(targetClass)
                        .action(ConverterEnum.CONVERTER)
                        .build();
            }
        };

        Converters.register(converter);
    }

    private String getName(Class clazz, Method method, String splitName) {
        return clazz.getName() + "." + method.getName() + "$" + splitName;
    }

    private void registerMerger(Class[] argsClasses, Class targetClass, Method method, Object instance) {
        Arrays.stream(argsClasses).filter(argClass -> !targetClass.equals(argClass)).forEach(argClass -> {
            Converter merger = new ConverterAdapter() {
                @Override
                public Object merge(Object target, Object source) throws ConvertException {
                    try {
                        Object[] args = Arrays.stream(argsClasses).map(argClazz -> {
                            if (argClazz.equals(target.getClass())) {
                                return target;
                            } else if (argClazz.equals(source.getClass())) {
                                return source;
                            } else {
                                return null;
                            }
                        }).toArray();
                        method.invoke(instance, args);
                    } catch (Exception e) {
                        throw new ConvertException(source, e);
                    }
                    return target;
                }

                @Override
                public Meta meta() {
                    return Converter.Meta.builder()
                            .converterName(getName(instance.getClass(), method, "Merger"))
                            .sourceClass(argClass)
                            .targetClass(targetClass)
                            .action(ConverterEnum.MERGER)
                            .build();
                }
            };

            Converters.register(merger);
        });
    }
}
