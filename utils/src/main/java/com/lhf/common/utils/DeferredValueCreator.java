package com.lhf.common.utils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * 如果获取期望值是需要耗费时间的,例如值是通过RPC调用获得的.那么可以通过异步化获取期望值的过程,避免对当前线程的阻塞.
 * <p>
 * <code>DeferredValueCreator</code>使用计算期望值的异步任务来创建期望值的代理对象,只有实际使用该对象时才会阻塞当前线程来等待期望值计算完成.
 * </p>
 *
 */
public interface DeferredValueCreator {
    /**
     * 创建期望值的代理,对该代理的所有方法调用都会触发执行 <code>future.get(timeout)</code>以获取期望值.
     *
     * @param valueType 期望值的类型
     * @param future    计算期望值的任务
     * @param timeout   超时时间
     * @param <T>       期望值类型参数
     * @return 期望值的推迟代理对象, 该对象同时是 {@link DeferredValue} 的实例
     */
    <T> T create(Class<T> valueType, CompletableFuture<? extends T> future, Duration timeout);

    /**
     * 是否支持为指定类型生成代理
     *
     * @param type 要判断是否支持的对象
     * @return 则返回该type对应的Class对象，如果不支持返回null
     */
    @SuppressWarnings("unchecked")
    default <T> Class<T> support(Type type) {
        Class<T> clazz = null;
        if (type instanceof Class) {
            clazz = ((Class<T>)type);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType)type).getRawType();
            clazz = ((Class<T>)rawType);
        }
        if (clazz != null
                && !clazz.isArray()
                && !clazz.isPrimitive()
                && !Modifier.isFinal(clazz.getModifiers())) {
            return clazz;
        }
        return null;
    }
}
