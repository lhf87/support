package com.lhf.test.common;

import com.lhf.common.Converters;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDTO;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lhf
 * @date 2018/7/12
 */
public class MainTest {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_DATETIME_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static class TestDO_BC extends TestDO {}

    public static class TestDTO_BC extends TestDTO {}

    // BeanCopier copy
    @Test
    public void test() throws Exception {
        List<TestDO_BC> list = new ArrayList<>();
        for (int i = 0; i < Constant.SIZE; i++) {
            TestDO_BC testDO = new TestDO_BC();
            testDO.setDoId(i);
            testDO.setDoName(i + "");
            testDO.setDoDate(new Date());
            list.add(testDO);
        }

        for (int j = 0; j < Constant.COUNT; j++) {
            long start = System.currentTimeMillis();

            for (int i = 0; i < list.size(); i++) {
                Converters.convert(list.get(i), TestDTO_BC.class);
            }

            System.out.println("BeanCopier转" + Constant.SIZE + "次" + (System.currentTimeMillis() - start));
        }
    }

    // 直接搞
    @Test
    public void test2() throws Exception {
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
                TestDTO testDTO = new TestDTO();
                TestDO source = list.get(i);
                testDTO.setDtoId(source.getDoId());
                testDTO.setDtoNameByFormat("P-" + source.getDoName() + "-S");
                testDTO.setDtoDate(THREAD_LOCAL_DATETIME_FORMAT.get().format(source.getDoDate()));
                testDTO.setPublicField(source.getPublicField());
            }

            System.out.println("直接赋值转" + Constant.SIZE + "次" + (System.currentTimeMillis() - start));
        }
    }

    @Test
    public void test3() throws Exception {
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
                TestDO source = list.get(i);
                BeanUtils.copyProperties(source, TestDTO.class.newInstance());
            }

            System.out.println("ConvertUtils转" + Constant.SIZE + "次" + (System.currentTimeMillis() - start));
        }
    }

}
