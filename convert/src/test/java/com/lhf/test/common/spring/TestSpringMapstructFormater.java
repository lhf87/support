package com.lhf.test.common.spring;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 默认类型转换器 通过spring注入
 *
 * @author lhf
 * @date 2018/7/12
 */

@Slf4j
@Component
public class TestSpringMapstructFormater {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_DATETIME_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Named("date2string")
    public String date2string(Date date) {
        String returnVal = "";

        try {
            returnVal = THREAD_LOCAL_DATETIME_FORMAT.get().format(date);
        } catch (Exception e) {
            log.warn("can't convert date to string");
        }

        return returnVal;
    }

    @Named("string2date")
    public Date string2date(String str) {
        Date returnVal = null;

        try {
            returnVal = THREAD_LOCAL_DATETIME_FORMAT.get().parse(str);
        } catch (Exception e) {
            log.warn("can't convert string to date");
        }

        return returnVal;
    }

    @Named("testformat1")
    public String testformat1(String str) {
        return "Prefix-" + str + "-Suffix";
    }

    @Named("testformat2")
    public String testformat2(String str) {
        return str.replace("Prefix-", "").replace("-Suffix", "");
    }
}
