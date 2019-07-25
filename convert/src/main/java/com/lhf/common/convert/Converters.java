package com.lhf.common.convert;

import com.lhf.common.convert.converter.ConvertException;
import com.lhf.common.convert.converter.Converter;
import com.lhf.common.convert.converter.ConverterEnum;
import com.lhf.common.convert.converter.NotFoundConversionException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementations of {@link Converter} that implement various useful reduction
 *
 * @author lhf
 * @date 2018/7/9
 */

@Slf4j
@SuppressWarnings("unchecked")
public final class Converters {

    private static final Map<String, Converter<?, ?>> CONVERTER_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, BeanCopier> COPIER_CACHE = new ConcurrentHashMap<>();

    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            return null;
        }

        Class sourceClass = source.getClass();
        Converter<S, T> converter = (Converter<S, T>) CONVERTER_CACHE.get(generateKey(sourceClass, targetClass, ConverterEnum.CONVERTER));
        if (converter != null) {
            try {
                return converter.convert(source);
            } catch (ConvertException e) {
                log.error("{} convert to {} error", sourceClass, targetClass, e);
                return null;
            }
        }

        T target;
        String copierKey = generateKey(sourceClass, targetClass, ConverterEnum.CONVERTER);
        try {
            target = targetClass.newInstance();
            BeanCopier copier;
            if (COPIER_CACHE.containsKey(copierKey)) {
                copier = COPIER_CACHE.get(copierKey);
            } else {
                copier = BeanCopier.create(sourceClass, targetClass, false);
                COPIER_CACHE.putIfAbsent(copierKey, copier);
            }
            copier.copy(source, target, null);
        } catch (Exception e) {
            log.error("{} convert to {} error", sourceClass, targetClass, e);
            return null;
        }

        return target;
    }

    public static <T> Collection<T> convert(Collection<?> sources, Class<T> targetClass) {
        return sources.stream().map(source -> convert(source, targetClass))
                .collect(Collectors.toCollection(new Supplier<Collection<T>>() {
                    @Override
                    public Collection<T> get() {
                        try {
                            return sources.getClass().newInstance();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }));
    }

    public static <T> T merge(T target, Object... sources) {
        if (sources == null) {
            return target;
        }

        Class targetClass = target.getClass();
        for (Object source : sources) {
            if (source == null) {
                continue;
            }
            Converter converter = CONVERTER_CACHE.get(generateKey(source.getClass(), targetClass, ConverterEnum.MERGER));
            if (converter != null) {
                try {
                    converter.merge(target, source);
                } catch (ConvertException ce) {
                    log.error("{} merge to {} error", source.getClass(), targetClass, ce);
                }
            } else {
                throw new NotFoundConversionException(source.getClass(), targetClass);
            }
        }

        return target;
    }

    public static void register(Converter<?, ?> converter) {
        Converter.Meta meta = converter.meta();
        CONVERTER_CACHE.putIfAbsent(generateKey(meta.getSourceClass(), meta.getTargetClass(), meta.getAction()), converter);
    }

    public static String generateKey(Class<?> source, Class<?> target, ConverterEnum type) {
        return source.getName() + type.getSplit() + target.getName();
    }
}
