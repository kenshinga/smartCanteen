package com.smart.canteen.enums;

import com.lc.core.error.IErrorInterface;
import lombok.AllArgsConstructor;

/**
 * @author lc
 * @date 2019-12-15
 */
@AllArgsConstructor
public enum CanteenExceptionEnum implements IErrorInterface {

    /**
     *
     */
    CREATE_FAIL(2001, "保存失败"),
    UPDATE_FAIL(2002, "操作过于频繁，请稍后再试"),
    USER_NOT_EXIST(2003, "用户不存在"),
    ACCOUNT_REPEAT(2004, "工号或身份证号或手机号重复"),
    ROLE_NAME_REPEAT(2005, "角色名称重复"),
    ROLE_NOT_EXIST(2006, "角色不存在"),
    PASSWORD_NOT_SAME(2007, "密码不一致"),
    ADD_ROLE_FAIL(2008, "添加角色失败"),
    ADD_ORG_FAIL(2009, "添加部门失败"),
    ORG_NOT_EXIST(2010, "部门不存在"),
    PAR_ORG_NOT_EXIST(2011, "上级部门不存在"),
    ORG_NAME_REPEAT(2012, "已经存在相同名称或者代码的部门"),
    CARD_NO_REPEAT(2013, "卡号重复"),
    SIGN_ERROR(2013, "非法请求，CRC错误"),

    UPDATE_FA2IL(99999, "update  fail");

    private Integer code;

    private String msg;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
