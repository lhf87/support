package com.lhf.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Assert
 *
 * @author lhf
 * @date 2018/7/11
 */
public class Assert {

    public static void notNull(Object object) {
        notNull(object, "argument cannot be null");
    }

    public static void notNull(Object object, String error) {
        if (null == object) {
            throw new IllegalArgumentException(error);
        }
    }

    public static void isTrue(boolean expr) {
        isTrue(expr, "expr is false");
    }

    public static void isTrue(boolean expr, String error) {
        if (!expr) {
            throw new IllegalArgumentException(error);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "collection is empty");
    }

    public static void notEmpty(Collection<?> collection, String error) {
        notNull(collection);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        isInstanceOf(type, obj, "");
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    private static void instanceCheckFailed(Class<?> type, Object obj, String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.length(msg) > 0) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new IllegalArgumentException(result);
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg, Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }
}
