package com.lhf.test.common.spring;

import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDO2;
import com.lhf.test.common.bean.TestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.dc.pr.PRError;

import java.util.Date;

/**
 * 直接使用MapstructConverter的转换器
 *
 * @author lhf
 * @date 2018/7/12
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-test.xml"})
public class SpringMapstructTest2 {

    @Autowired
    private TestSpringMapstructConverter testSpringMapstructConverter;

    @Autowired
    private TestSpringMapstructMarger testSpringMapstructMarger;

    @Test
    public void test2() throws Exception {
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoName("name");
        testDO.setDoDate(new Date());
        testDO.setPublicField("public");

        TestDTO testDTO = testSpringMapstructConverter.convertA(testDO);

        System.out.println(testDTO);
    }

    @Test
    public void test22() throws Exception {
        TestDTO testDTO = new TestDTO();
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoDate(new Date());
        TestDO2 testDO2 = new TestDO2();
        testDO2.setPublicField2("pf2");

        testSpringMapstructMarger.merage(testDTO, testDO, testDO2);

        System.out.println(testDTO);
    }
}
