package com.lhf.test.common.spring;

import com.lhf.common.Converters;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDO2;
import com.lhf.test.common.bean.TestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * springtest
 *
 * @author lhf
 * @date 2018/7/12
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-test.xml"})
public class SpringMapstructTest {

    @Test
    public void test() throws Exception {
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoName("name");
        testDO.setDoDate(new Date());
        testDO.setPublicField("public");

        TestDTO testDTO = Converters.convert(testDO, TestDTO.class);

        System.out.println(testDTO);
    }

    @Test
    public void testMarge() throws Exception {
        TestDTO testDTO = new TestDTO();
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoDate(new Date());
        TestDO2 testDO2 = new TestDO2();
        testDO2.setPublicField2("pf2");
        Converters.merge(testDTO, testDO, testDO2);
        System.out.println(testDTO);
    }
}
