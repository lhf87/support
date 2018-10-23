package com.lhf.common.convert.converter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举
 *
 * @author lhf
 * @date 2018/8/1
 */

@Getter
@AllArgsConstructor
public enum ConverterEnum {

    CONVERTER("$Convert$"),

    MERGER("$Merge$");

    private String split;
}
