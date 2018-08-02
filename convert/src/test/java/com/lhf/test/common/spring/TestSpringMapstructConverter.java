package com.lhf.test.common.spring;

import com.lhf.common.spring.SpringRegister;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * componentModel = spring 则要继承自SpringRegister进行注册 Formater也要通过spring加载
 *
 * @author lhf
 * @date 2018/7/12
 */

@Mapper(componentModel = "spring", uses = TestSpringMapstructFormater.class)
public interface TestSpringMapstructConverter extends SpringRegister {

    @Mappings({@Mapping(target = "dtoId", source = "doId"),
            @Mapping(target = "dtoNameByFormat", source = "doName", qualifiedByName = {"testformat1"}),
            @Mapping(target = "dtoDate", source = "doDate", qualifiedByName = {"date2string"})})
    TestDTO convertA(TestDO testDO);

    @Mappings({@Mapping(target = "doId", source = "dtoId"),
            @Mapping(target = "doName", source = "dtoNameByFormat", qualifiedByName = {"testformat2"}),
            @Mapping(target = "doDate", source = "dtoDate", qualifiedByName = {"string2date"})})
    TestDO convertB(TestDTO testDTO);
}
