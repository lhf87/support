package com.lhf.test.common.converter;

import com.lhf.common.convert.format.DefaultMapstructFormater;
import com.lhf.common.convert.register.RegisterFactory;
import com.lhf.test.common.bean.TestDO;
import com.lhf.test.common.bean.TestDTO;
import com.lhf.test.common.bean.TestListDO;
import com.lhf.test.common.bean.TestListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * mapstruct转换器
 *
 * @author lhf
 * @date 2018/7/12
 */

@Mapper(uses = {DefaultMapstructFormater.class, TestFormater.class})
public abstract class TestMapstructConverter {

    static {
        RegisterFactory.mapstructRegister(TestMapstructConverter.class);
    }

    @Mappings({@Mapping(target = "dtoId", source = "doId"),
            @Mapping(target = "dtoNameByFormat", source = "doName", qualifiedByName = {"testformat1"}),
            @Mapping(target = "dtoDate", source = "doDate", qualifiedByName = {"date2string"}),
            @Mapping(target = "outerName", source = "joinDO.innerName"),
            @Mapping(target = "listDTOS", source = "listDOS")})
    public abstract TestDTO convertA(TestDO testDO);

    // 如果没有反向转换就不用要这个
    @Mappings({@Mapping(target = "doId", source = "dtoId"),
            @Mapping(target = "doName", source = "dtoNameByFormat", qualifiedByName = {"testformat2"}),
            @Mapping(target = "doDate", source = "dtoDate", qualifiedByName = {"string2date"}),
            @Mapping(target = "joinDO.innerName", source = "outerName"),
            @Mapping(target = "listDOS", source = "listDTOS")})
    public abstract TestDO convertB(TestDTO testDTO);

    @Mappings({@Mapping(target = "dtoId", source = "id"), @Mapping(target = "dtoDetailName", source = "detailName")})
    public abstract TestListDTO convert(TestListDO testListDO);

    // 如果没有反向转换就不用要这个
    @Mappings({@Mapping(target = "id", source = "dtoId"), @Mapping(target = "detailName", source = "dtoDetailName")})
    public abstract TestListDO convert(TestListDTO testListDTO);
}
