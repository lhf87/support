package com.lhf.test.common;

import com.lhf.common.Converters;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDTO;
import com.lhf.test.common.bean.TestInnerJoinDO;
import com.lhf.test.common.bean.TestListDO;
import com.lhf.test.common.converter.TestMapstructConverter;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lhf
 * @date 2018/7/12
 */
public class MainMapstructTest {

    /**
     * 此处只是为了加载class 以便执行static代码块进行注册，也可以直接使用 如果是spring扫描方式就不用这种操作了
     */
    private static TestMapstructConverter testMapstructConverter = Mappers.getMapper(TestMapstructConverter.class);

    @Test
    public void main() throws Exception {
        TestDO testDO = new TestDO();
        testDO.setDoId(1);
        testDO.setDoName("name");
        testDO.setDoDate(new Date());
        testDO.setPublicField("public");

        // 1对1
        TestInnerJoinDO testInnerJoinDO = new TestInnerJoinDO();
        testInnerJoinDO.setInnerName("innerJoinName");
        testDO.setJoinDO(testInnerJoinDO);

        // 1对多
        List<TestListDO> testListDOS = new ArrayList<>();
        TestListDO testListDO1 = new TestListDO();
        testListDO1.setId(1);
        testListDO1.setDetailName("1detail");
        TestListDO testListDO2 = new TestListDO();
        testListDO1.setId(2);
        testListDO1.setDetailName("2detail");
        testListDOS.add(testListDO1);
        testListDOS.add(testListDO2);
        testDO.setListDOS(testListDOS);

        TestDTO testDTO = Converters.convert(testDO, TestDTO.class);
        // 直接使用
        TestDTO testDTO2 = testMapstructConverter.convertA(testDO);
        TestDO testDO2 = testMapstructConverter.convertB(testDTO2);

        System.out.println(testDTO);
        System.out.println(testDTO2);
        System.out.println(testDO2);
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

            System.out.println("marpstruct转" + Constant.SIZE + "次" + (System.currentTimeMillis() - start));
        }
    }
}
