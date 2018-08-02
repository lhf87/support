package com.lhf.test.common.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * DO领域模型
 *
 * @author lhf
 * @date 2018/7/12
 */

@Data
public class TestDO {

    private Integer doId;

    private String doName;

    private Date doDate;

    private String publicField;

    private TestInnerJoinDO joinDO;

    private List<TestListDO> listDOS;
}
