package com.lhf.common.utils;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 反射工具类
 *
 * @author lhf
 * @date 2018/10/23
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionKit {

    private static Map<Class<?>, Object> PRIMITIVES_DEFAULT = ImmutableMap.<Class<?>, Object>builder()
        .put(boolean.class, false)
        .put(char.class, '\u0000')
        .put(byte.class, (byte) 0)
        .put(short.class, (short) 0)
        .put(int.class, 0)
        .put(long.class, 0L)
        .put(float.class, 0F)
        .put(double.class, 0D)
        .build();

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz) throws Exception {
        if (clazz.isPrimitive()) {
            return (T)PRIMITIVES_DEFAULT.get(clazz);
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Constructor[] constructors = clazz.getDeclaredConstructors();
            Constructor publicConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.getModifiers() == Modifier.PUBLIC)
                .findFirst().orElse(null);
            if (publicConstructor == null) {
                throw new IllegalAccessException(clazz.getName() + " have no accessible constructor");
            }
            Class[] parameterTypes = publicConstructor.getParameterTypes();
            Object[] params = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                params[i] = newInstance(parameterTypes[i]);
            }
            return (T) publicConstructor.newInstance(params);
        }
    }

    /**
     * 扫描某接口的实现类或子类
     * @param packageToScan 包路径
     * @param clazz 接口
     * @param annotation 注解
     * @param <T> T
     * @return 接口的实现类
     */
    public static <T> Set<Class<? extends T>> scanSub(String packageToScan,
                                                   Class<T> clazz,
                                                   Class<? extends Annotation> annotation) {
        ConfigurationBuilder cb = new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath());

        if(!Strings.isNullOrEmpty(packageToScan)) {
            cb.filterInputsBy(new FilterBuilder().includePackage(packageToScan));
        }

        return new Reflections(cb).getSubTypesOf(clazz)
                                  .stream()
                                  .filter(clz -> null == annotation || clz.isAnnotationPresent(annotation))
                                  .collect(Collectors.toSet());
    }

    public static List<Field> getFieldList(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                                      .filter(field -> !Modifier.isStatic(field.getModifiers())
                                          && !Modifier.isTransient(field.getModifiers()))
                                      .collect(Collectors.toList());
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return fieldList;
        }
        return excludeOverrideSuperField(fieldList, getFieldList(superClass));
    }

    private static List<Field> excludeOverrideSuperField(List<Field> fieldList, List<Field> superFieldList) {
        Map<String, Field> fieldMap = fieldList.stream()
                                               .collect(Collectors.toMap(Field::getName, Function.identity()));
        superFieldList.forEach(superField -> {
            if (null == fieldMap.get(superField.getName())) {
                fieldList.add(superField);
            }
        });
        return fieldList;
    }
}
