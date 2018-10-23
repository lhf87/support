package com.lhf.test.common;

import com.lhf.common.reflect.ReflectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试
 *
 * @author lhf
 * @date 2018/10/23
 */
public class ReflectUtilsTest {

    @Data
    @AllArgsConstructor
    public static class Bean {
        private int id;
    }

    @Test
    public void test() throws Exception {
        Bean newBean = ReflectUtils.newInstance(Bean.class);
        Assert.assertEquals(newBean.getId(), 0);
    }
}
