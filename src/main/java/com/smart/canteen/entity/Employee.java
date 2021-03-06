package com.smart.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.canteen.enums.EmployeeStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 员工
 * </p>
 *
 * @author lc
 * @since 2020-03-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("employee")
@ApiModel(value = "Employee对象", description = "员工")
public class Employee extends BaseEntity {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工号")
    @TableField(value = "no")
    private String no;

    @ApiModelProperty(value = "姓名")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "手机号")
    @TableField(value = "mobile")
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    @TableField(value = "id_card")
    private String idCard;

    @ApiModelProperty(value = "ic卡Id")
    @TableField(value = "card_id")
    private Long cardId;

    @ApiModelProperty(value = "卡号")
    @TableField(value = "card_no")
    private String cardNo;

    @ApiModelProperty(value = "组织ID")
    @TableField(value = "origination_id")
    private Long originationId;

    @ApiModelProperty(value = "组织")
    @TableField(value = "origination_name")
    private String originationName;

    @JsonIgnore
    @ApiModelProperty(value = "盐")
    @TableField(value = "salt")
    private String salt;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    @TableField(value = "password")
    private String password;

    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private EmployeeStatusEnum status;

    @ApiModelProperty(value = "能否修改")
    @TableField(value = "can_edit")
    private Boolean canEdit = true;
}
