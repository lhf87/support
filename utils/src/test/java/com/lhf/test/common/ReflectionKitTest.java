package com.lhf.test.common;

import com.lhf.common.utils.ReflectionKit;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

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

    interface Iface {}

    class SubClass implements Iface {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ann {}

    @Ann
    class SubClassWithAnn implements Iface {}

    @Test
    public void test() throws Exception {
        Bean newBean = ReflectionKit.newInstance(Bean.class);
        Assert.assertEquals(newBean.getId(), 0);

        Set<Class<? extends Iface>> ifaces = ReflectionKit.scanSub("com.lhf.test.common", Iface.class, null);
        Assert.assertEquals(ifaces.size(), 2);

        ifaces = ReflectionKit.scanSub("com.lhf.test.common", Iface.class, Ann.class);
        Assert.assertEquals(ifaces.size(), 1);
        Assert.assertTrue(ifaces.contains(SubClassWithAnn.class));
    }
}
