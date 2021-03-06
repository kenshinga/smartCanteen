package com.smart.canteen.dto.recharge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author lc
 * @date 2020/3/8下午 5:58xO
 */
@Data
@ApiModel
public class RechargeForm implements Serializable {

    @Size(min = 1)
    @NotNull
    @ApiModelProperty(value = "卡片Id")
    private List<Long> cardIds;

    @Min(1)
    @Max(2)
    @NotNull
    @ApiModelProperty(value = "充值类型")
    private Integer rechargeType;

    @Min(0)
    @NotNull
    @ApiModelProperty(value = "充值金额")
    private Double money;

    @Length(max = 255)
    @ApiModelProperty(value = "充值描述")
    private String description;
}
