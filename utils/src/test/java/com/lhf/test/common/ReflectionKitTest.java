package com.lhf.test.common;

import com.lhf.common.utils.ReflectionKit;
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
public class ReflectionKitTest {

    @Data
    @AllArgsConstructor
    public static class Bean {
        private int id;
    }

    @Test
    public void test() throws Exception {
        Bean newBean = ReflectionKit.newInstance(Bean.class);
        Assert.assertEquals(newBean.getId(), 0);
    }
}
