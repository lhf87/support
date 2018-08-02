package com.lhf.common.converter;

/**
 * 转换异常
 *
 * @author lhf
 * @date 2018/7/10
 */
public class ConvertException extends Exception {

    private Object source;

    public ConvertException(Object source, Throwable cause) {
        super(cause);
        this.source = source;
    }

    @Override
    public String getMessage() {
        return "convert error , source: {" + source + "}";
    }
}
