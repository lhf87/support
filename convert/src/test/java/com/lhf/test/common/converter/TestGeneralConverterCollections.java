package com.lhf.test.common.converter;

import com.lhf.common.convert.converter.ConvertException;
import com.lhf.common.convert.converter.Converter;
import com.lhf.common.convert.converter.ConverterAdapter;
import com.lhf.common.convert.converter.ConverterEnum;
import com.lhf.common.convert.register.RegisterFactory;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 手写的转换器
 *
 * @author lhf
 * @date 2018/7/12
 */
public class TestGeneralConverterCollections {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_DATETIME_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static Converter<TestDO, TestDTO> c1 = new ConverterAdapter<TestDO, TestDTO>() {
        @Override
        public TestDTO convert(TestDO source) throws ConvertException {
            TestDTO testDTO = new TestDTO();
            testDTO.setDtoId(source.getDoId());
            testDTO.setDtoNameByFormat("P-" + source.getDoName() + "-S");
            testDTO.setDtoDate(THREAD_LOCAL_DATETIME_FORMAT.get().format(source.getDoDate()));
            testDTO.setPublicField(source.getPublicField());
            return testDTO;
        }

        @Override
        public Meta meta() {
            return Meta.builder().converterName("TestDO -> TestDTO").sourceClass(TestDO.class)
                    .targetClass(TestDTO.class).action(ConverterEnum.CONVERTER).build();
        }
    };

    static Converter<TestDTO, TestDO> c2 = new ConverterAdapter<TestDTO, TestDO>() {
        @Override
        public TestDO convert(TestDTO source) throws ConvertException {
            TestDO testDO = new TestDO();
            testDO.setDoId(source.getDtoId());
            testDO.setDoName(source.getDtoNameByFormat().replace("P-", "").replace("-S", ""));
            try {
                testDO.setDoDate(THREAD_LOCAL_DATETIME_FORMAT.get().parse(source.getDtoDate()));
            } catch (Exception e) {
                //
            }
            testDO.setPublicField(source.getPublicField());
            return testDO;
        }

        @Override
        public Meta meta() {
            return Meta.builder().converterName("TestDTO -> TestDO").sourceClass(TestDTO.class)
                    .targetClass(TestDO.class).action(ConverterEnum.CONVERTER).build();
        }
    };

    static {
        List<Converter> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        RegisterFactory.generalRegister(list);
    }
}
