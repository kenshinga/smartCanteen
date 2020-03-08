package com.smart.canteen.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.core.dto.Account;
import com.lc.core.error.BaseException;
import com.lc.core.utils.MathUtil;
import com.lc.core.utils.ModelMapperUtils;
import com.lc.core.utils.ValidatorUtil;
import com.smart.canteen.dto.card.CardForm;
import com.smart.canteen.entity.Employee;
import com.smart.canteen.entity.IcCard;
import com.smart.canteen.enums.*;
import com.smart.canteen.mapper.IcCardMapper;
import com.smart.canteen.service.IIcCardService;
import com.smart.canteen.utils.EntityLogUtil;
import com.smart.canteen.vo.CardVo;
import com.smart.canteen.vo.ResponseMsg;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * iC卡 服务实现类
 * </p>
 *
 * @author lc
 * @since 2020-03-03
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class IcCardServiceImpl extends ServiceImpl<IcCardMapper, IcCard> implements IIcCardService {

    @Override
    public Long addCard(CardForm form, Employee employee, Account create) {
        ValidatorUtil.validator(form, CardForm.Insert.class);
        IcCard card = getByCode(form.getNo());
        if (card != null) {
            throw new BaseException(CanteenExceptionEnum.CARD_NO_REPEAT);
        }
        card = ModelMapperUtils.strict(form, IcCard.class);
        card.setStatus(CardStatusEnum.ENABLE);
        card.setAccountStatus(CardAccountEnum.NORMAL);
        card.setEmployeeId(employee.getId());
        card.setEmployeeNo(employee.getNo());
        card.setEmployeeName(employee.getName());
        EntityLogUtil.addNormalUser(card, create);
        boolean save = save(card);
        if (!save) {
            throw new BaseException(CanteenExceptionEnum.CREATE_FAIL);
        }
        return card.getId();
    }

    @Override
    public IcCard getByCode(String no) {
        return getOne(Wrappers.<IcCard>lambdaQuery().eq(IcCard::getNo, no), false);
    }

    @Override
    public IcCard getById(Long id) {
        return super.getById(id);
    }

    @Override
    public void update(CardForm form, Account create) {
        ValidatorUtil.validator(form, CardForm.Update.class);
        IcCard card = getById(form.getId());
        if (card == null) {
            throw new BaseException(CanteenExceptionEnum.CARD_NO_EXIST);
        }
        BeanUtils.copyProperties(form, card, "id", "no");
        boolean b = updateById(card);
        if (!b) {
            throw new BaseException(CanteenExceptionEnum.UPDATE_FAIL);
        }
    }

    @Override
    public CardVo getByNo(String no) {
        if (StringUtils.isEmpty(no)) {
            throw new BaseException(CanteenExceptionEnum.CARD_NOT_EXIST);
        }
        return getBaseMapper().getByNo(no);
    }

    @Override
    public ResponseMsg deductions(String cardNo, Integer money) {
        IcCard card = getOne(Wrappers.<IcCard>lambdaQuery()
                        .eq(IcCard::getNo, cardNo),
                false);
        if (card == null) {
            return new ResponseMsg(CmdCodeEnum.CON, Voices.INVALID, cardNo, null, null);
        }
        if (card.getStatus() == CardStatusEnum.DISABLE) {
            return new ResponseMsg(CmdCodeEnum.CON, Voices.LOSS, cardNo, null, null);
        }
        Double currentBalance = card.getCurrentBalance();
        Double lastBalance = currentBalance - MathUtil.div(money, 100, 2);
        if (currentBalance < 0) {
            return new ResponseMsg(CmdCodeEnum.CON, Voices.NOT, cardNo, lastBalance, card.getEmployeeName());
        }
        return null;
    }

    @Override
    public ResponseMsg search(String cardNo) {
        IcCard card = getOne(Wrappers.<IcCard>lambdaQuery()
                        .eq(IcCard::getNo, cardNo),
                false);
        if (card == null) {
            return new ResponseMsg(CmdCodeEnum.CON, Voices.WELCOME, cardNo, null, null);
        }
        if (card.getStatus() == CardStatusEnum.DISABLE) {
            return new ResponseMsg(CmdCodeEnum.CON, Voices.LOSS, cardNo, null, null);
        }
        return new ResponseMsg(CmdCodeEnum.CON, Voices.WELCOME, cardNo, card.getCurrentBalance(), card.getEmployeeName());
    }
}
