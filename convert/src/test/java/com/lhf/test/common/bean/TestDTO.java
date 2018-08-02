package com.lhf.test.common.bean;

import lombok.Data;

import java.util.List;

/**
 * DTO领域模型
 *
 * @author lhf
 * @date 2018/7/12
 */

@Data
public class TestDTO {

    private Integer dtoId;

    private String dtoNameByFormat;

    private String dtoDate;

    private String publicField;

    private String outerName;

    private List<TestListDTO> listDTOS;
}
