package com.lhf.common.utils;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class DeferredValueCreatorCglibImpl implements DeferredValueCreator {
    //public static final Class<?>[] DEFERRED_VALUE_INTERFACE = {DeferredValue.class};
    public static final Class<?>[] DEFERRED_VALUE_CALLBACK = {DeferredMethodInterceptor.class};
    private final Map<Class<?>, ProxyConstructor<?>> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> valueType, CompletableFuture<? extends T> future, Duration timeout) {
        ProxyConstructor<T> proxyConstructor =
            (ProxyConstructor<T>)CACHE.computeIfAbsent(
                valueType, DeferredValueCreatorCglibImpl::createProxyConstructor);
        return proxyConstructor.newInstance(future, timeout);
    }

    private static <T> ProxyConstructor<? extends T> createProxyConstructor(Class<T> clazz) {
        Class<? extends T> proxyClass = createProxyClass(clazz);
        return new ProxyConstructor<>(proxyClass);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> createProxyClass(Class<T> valueType) {
        Enhancer enhancer = new Enhancer();
        //enhancer.setInterfaces(DEFERRED_VALUE_INTERFACE);
        enhancer.setSuperclass(valueType);
        enhancer.setUseFactory(false);
        enhancer.setCallbackTypes(DEFERRED_VALUE_CALLBACK);
        enhancer.setCallbackFilter(method -> 0);
        enhancer.setNamingPolicy(DeferredValueNamingPolicy.INSTANCE);
        return enhancer.createClass();
    }

    private static class ProxyConstructor<T> {
        private static final ObjenesisStd OBJENESIS_STD = new ObjenesisStd();
        private final Class<T> proxyClass;

        public ProxyConstructor(Class<T> proxyClass) {
            this.proxyClass = proxyClass;
        }

        private T newInstance(CompletableFuture<? extends T> future, Duration timeout) {
            Enhancer.registerCallbacks(proxyClass, new Callback[] {new DeferredMethodInterceptor(future, timeout)});
            try {
                return OBJENESIS_STD.newInstance(proxyClass);
            } finally {
                Enhancer.registerStaticCallbacks(proxyClass, null);
            }
        }
    }

    private static final class DeferredMethodInterceptor implements MethodInterceptor {
        public static final String DEFERRED_VALUE_INTERFACE_METHOD = "toFuture";
        private final CompletableFuture<?> future;
        private final Duration timeout;

        public DeferredMethodInterceptor(CompletableFuture<?> future, Duration timeout) {
            this.future = future;
            this.timeout = timeout;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String methodName = method.getName();
            if (DEFERRED_VALUE_INTERFACE_METHOD.equals(methodName)) {
                return future;
            }
            //Object target = ApiUtils.getReturnValue(future, timeout);
            return null; //methodProxy.invoke(target, args);
        }
    }

    public static final class DeferredValueNamingPolicy extends DefaultNamingPolicy {
        public static final DeferredValueNamingPolicy INSTANCE = new DeferredValueNamingPolicy();

        @Override
        protected String getTag() {
            return "DeferredValue";
        }
    }

}

