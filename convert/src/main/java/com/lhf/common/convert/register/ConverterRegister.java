package com.lhf.common.convert.register;

/**
 * 转换器注册接口
 *
 * @author lhf
 * @date 2018/7/6
 */

public interface ConverterRegister {

    /**
     * 注册转换器
     * 
     * @param convertInstance 转换实例
     */
    void register(Object convertInstance);
}
