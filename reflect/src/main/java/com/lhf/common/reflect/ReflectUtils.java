package com.lhf.common.reflect;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author lhf
 * @date 2018/10/23
 */
public class ReflectUtils {

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
}
