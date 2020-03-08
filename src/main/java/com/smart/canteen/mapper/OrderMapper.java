package com.smart.canteen.mapper;

import com.smart.canteen.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 充值记录 Mapper 接口
 * </p>
 *
 * @author lc
 * @since 2020-03-08
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
