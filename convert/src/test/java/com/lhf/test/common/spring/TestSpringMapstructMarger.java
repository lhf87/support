package com.lhf.test.common.spring;

import com.lhf.common.convert.spring.SpringRegister;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDO2;
import com.lhf.test.common.bean.TestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 * componentModel = spring 则要继承自SpringRegister进行注册 Formater也要通过spring加载
 *
 * @author lhf
 * @date 2018/8/1
 */

@Mapper(componentModel = "spring", uses = TestSpringMapstructFormater.class)
public interface TestSpringMapstructMarger extends SpringRegister {

    @Mappings({
            @Mapping(target = "dtoId", source = "testDO.doId"),
            @Mapping(target = "dtoDate", source = "testDO.doDate", qualifiedByName = {"date2string"}),
            @Mapping(target = "publicField", source = "testDO2.publicField2" )
    })
    TestDTO merage(@MappingTarget TestDTO testDTO, TestDO testDO, TestDO2 testDO2);
}
