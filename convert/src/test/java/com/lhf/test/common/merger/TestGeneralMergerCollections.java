package com.lhf.test.common.merger;

import com.lhf.common.convert.converter.ConvertException;
import com.lhf.common.convert.converter.Converter;
import com.lhf.common.convert.converter.ConverterAdapter;
import com.lhf.common.convert.converter.ConverterEnum;
import com.lhf.common.convert.register.RegisterFactory;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDO2;
import com.lhf.test.common.bean.TestDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 手写的合并器
 *
 * @author lhf
 * @date 2018/8/1
 */
public class TestGeneralMergerCollections {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_DATETIME_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static Converter<TestDO, TestDTO> c1 = new ConverterAdapter<TestDO, TestDTO>() {
        @Override
        public TestDTO merge(TestDTO target, TestDO source) throws ConvertException {
            target.setDtoId(source.getDoId());
            target.setDtoNameByFormat("P-" + source.getDoName() + "-S");
            target.setDtoDate(THREAD_LOCAL_DATETIME_FORMAT.get().format(source.getDoDate()));
            return target;
        }

        @Override
        public Meta meta() {
            return Meta.builder().converterName("TestDO -> TestDTO").sourceClass(TestDO.class)
                    .targetClass(TestDTO.class).action(ConverterEnum.MERGER).build();
        }
    };

    public static Converter<TestDO2, TestDTO> c2 = new ConverterAdapter<TestDO2, TestDTO>() {
        @Override
        public TestDTO merge(TestDTO target, TestDO2 source) throws ConvertException {
            target.setPublicField(source.getPublicField2());
            return target;
        }

        @Override
        public Meta meta() {
            return Meta.builder().converterName("TestDO2 -> TestDTO").sourceClass(TestDO2.class)
                    .targetClass(TestDTO.class).action(ConverterEnum.MERGER).build();
        }
    };

    static {
        List<Converter> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        RegisterFactory.generalRegister(list);
    }
}
