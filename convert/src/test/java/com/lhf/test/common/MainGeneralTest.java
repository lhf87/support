package com.lhf.test.common;

import com.lhf.common.Converters;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDO2;
import com.lhf.test.common.bean.TestDTO;
import com.lhf.test.common.converter.TestGeneralConverterCollections;
import com.lhf.test.common.merger.TestGeneralMergerCollections;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lhf
 * @date 2018/7/12
 */
public class MainGeneralTest {

    /**
     * 此处只是为了加载class 以便执行static代码块进行注册，也可以直接使用 如果是spring扫描方式就不用这种操作了
     */
    private static TestGeneralConverterCollections converters = new TestGeneralConverterCollections();
    private static TestGeneralMergerCollections mergers = new TestGeneralMergerCollections();

    @Test
    public void main() throws Exception {
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoName("name");
        testDO.setDoDate(new Date());
        testDO.setPublicField("public");

        TestDTO testDTO = Converters.convert(testDO, TestDTO.class);
        // 直接使用
        TestDTO testDTO2 = TestGeneralConverterCollections.c1.convert(testDO);

        System.out.println(testDTO);
        System.out.println(testDTO2);
    }

    @Test
    public void testMerge() throws Exception {
        TestDTO testDTO = new TestDTO();
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoDate(new Date());
        TestDO2 testDO2 = new TestDO2();
        testDO2.setPublicField2("pf2");

        TestGeneralMergerCollections.c1.merge(testDTO, testDO);
        TestGeneralMergerCollections.c2.merge(testDTO, testDO2);

        System.out.println(testDTO);
    }

    @Test
    public void big() throws Exception {
        List<TestDO> list = new ArrayList<>();
        for (int i = 0; i < Constant.SIZE; i++) {
            TestDO testDO = new TestDO();
            testDO.setDoId(i);
            testDO.setDoName(i + "");
            testDO.setDoDate(new Date());
            list.add(testDO);
        }

        for (int j = 0; j < Constant.COUNT; j++) {
            long start = System.currentTimeMillis();

            for (int i = 0; i < list.size(); i++) {
                Converters.convert(list.get(i), TestDTO.class);
            }

            System.out.println("手写convert转" + Constant.SIZE + "次" + (System.currentTimeMillis() - start));
        }
    }
}
