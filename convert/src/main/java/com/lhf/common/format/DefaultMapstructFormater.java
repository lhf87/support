package com.lhf.common.format;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 默认类型转换器
 *
 * @author lhf
 * @date 2018/7/12
 */

@Slf4j
public class DefaultMapstructFormater {

    private static final int DEFAULT_INT = 0;
    private static final long DEFAULT_LONG = 0L;

    private static final ZoneId ZONEID = ZoneId.systemDefault();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Named("string2int")
    public int string2int(String str) {
        if (!isDigit(str)) {
            log.warn("string not int! return default");
            return DEFAULT_INT;
        }
        return Integer.valueOf(str);
    }

    @Named("int2string")
    public String int2string(int i) {
        return String.valueOf(i);
    }

    @Named("string2long")
    public long string2long(String str) {
        long returnVal = DEFAULT_LONG;

        try {
            returnVal = Long.valueOf(str);
        } catch (Exception e) {
            log.warn("string not long! return default");
        }

        return returnVal;
    }

    @Named("long2string")
    public String long2string(long l) {
        return String.valueOf(l);
    }

    @Named("dateString2timestamp")
    public long dateString2timestamp(String dateString) {
        long returnVal = DEFAULT_LONG;

        try {
            returnVal = Date.from(LocalDateTime.parse(dateString, DATE_FORMATTER).atZone(ZONEID).toInstant()).getTime();
        } catch (Exception e) {
            log.warn("can't convert dateString to timestamp");
        }

        return returnVal;
    }

    @Named("date2string")
    public String date2string(Date date) {
        String returnVal = "";

        try {
            returnVal = date.toInstant().atZone(ZONEID).toLocalDateTime().format(DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("can't convert date to string");
        }

        return returnVal;
    }

    @Named("string2date")
    public Date string2date(String str) {
        Date returnVal = null;

        try {
            returnVal = Date.from(LocalDateTime.parse(str, DATE_FORMATTER).atZone(ZONEID).toInstant());
        } catch (Exception e) {
            log.warn("can't convert string to date");
        }

        return returnVal;
    }

    private boolean isDigit(String str) {
        if (null == str || str.length() == 0) {
            return false;
        }

        for (int i = str.length(); --i >= 0;) {
            int c = str.charAt(i);
            if (c < 48 || c > 57) {
                return false;
            }
        }

        return true;
    }
}
