package com.lhf.test.common.converter;

import org.mapstruct.Named;

/**
 * 格式化
 *
 * @author lhf
 * @date 2018/7/12
 */
public class TestFormater {

    @Named("testformat1")
    public String testformat1(String str) {
        return "Prefix-" + str + "-Suffix";
    }

    @Named("testformat2")
    public String testformat2(String str) {
        return str.replace("Prefix-", "").replace("-Suffix", "");
    }
}
