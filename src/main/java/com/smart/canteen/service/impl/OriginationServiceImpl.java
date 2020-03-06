package com.smart.canteen.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.core.dto.Account;
import com.lc.core.error.BaseException;
import com.lc.core.utils.ModelMapperUtils;
import com.lc.core.utils.ObjectUtil;
import com.lc.core.utils.ValidatorUtil;
import com.smart.canteen.dto.CommonList;
import com.smart.canteen.dto.origination.OriginationForm;
import com.smart.canteen.dto.origination.OriginationSearch;
import com.smart.canteen.entity.Origination;
import com.smart.canteen.enums.CanteenExceptionEnum;
import com.smart.canteen.mapper.OriginationMapper;
import com.smart.canteen.service.IOriginationService;
import com.smart.canteen.utils.EntityLogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 组织 服务实现类
 * </p>
 *
 * @author lc
 * @since 2020-03-02
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OriginationServiceImpl extends ServiceImpl<OriginationMapper, Origination> implements IOriginationService {

    @Override
    public void add(OriginationForm dto, Account creator) {
        ValidatorUtil.validator(dto, OriginationForm.Insert.class);
        Origination origination = judgeIsSame(dto.getName(), dto.getCode());
        if (origination != null) {
            throw new BaseException(CanteenExceptionEnum.ORG_NAME_REPEAT);
        }
        origination = ModelMapperUtils.strict(dto, Origination.class);
        addTreeNode(origination);
        EntityLogUtil.addNormalUser(origination, creator);
        boolean save = save(origination);
        if (!save) {
            throw new BaseException(CanteenExceptionEnum.CREATE_FAIL);
        }
    }

    /**
     * 添加节点线索
     *
     * @param origination
     */
    private void addTreeNode(Origination origination) {
        long parentId = ObjectUtil.getLong(origination.getParentId());
        String path = "-";
        long level = 1L;
        Origination parent;
        if (parentId > 0) {
            parent = getById(parentId);
            if (parent == null) {
                throw new BaseException(CanteenExceptionEnum.PAR_ORG_NOT_EXIST);
            }
            level = parent.getLevel() + 1;
            if (path.equals(parent.getPath())) {
                path = parentId + "-";
            } else {
                path = parent.getPath() + parentId + "-";
            }
        }
        origination.setParentId(parentId);
        origination.setLevel(level);
        origination.setPath(path);
    }


    @Override
    public void update(OriginationForm form, Account updater) {
        ValidatorUtil.validator(form, OriginationForm.Update.class);
        Long id = form.getId();
        Origination origination = getById(id);
        if (origination == null) {
            throw new BaseException(CanteenExceptionEnum.ORG_NOT_EXIST);
        }
        Origination oldOrigination = judgeIsSame(form.getName(), form.getCode());
        if (oldOrigination != null && !oldOrigination.getId().equals(id)) {
            throw new BaseException(CanteenExceptionEnum.ORG_NAME_REPEAT);
        }
        origination.setName(form.getName());
        origination.setCode(form.getCode());
        origination.setDescription(form.getDescription());
        addTreeNode(origination);
        EntityLogUtil.addNormalUser(origination, updater);
        boolean b = updateById(origination);
        if (!b) {
            throw new BaseException(CanteenExceptionEnum.UPDATE_FAIL);
        }
    }

    @Override
    public void delete(Long id, Account updater) {
        Origination origination = getById(id);
        if (origination == null) {
            throw new BaseException(CanteenExceptionEnum.ORG_NOT_EXIST);
        }
        getBaseMapper().logicDeleted(updater, id);
    }

    @Override
    public CommonList<Origination> listByConditional(OriginationSearch search) {
        Page<Origination> page = new Page<>(search.getPage(), search.getSize());
        super.page(page, Wrappers.<Origination>lambdaQuery()
                .like(!StringUtils.isEmpty(search.getName()), Origination::getName, search.getName())

        );
        return new CommonList<>(page.hasNext(), page.getTotal(), page.getCurrent(), page.getRecords());
    }

    @Override
    public Origination getById(Long id) {
        return super.getById(id);
    }

    @Override
    public Origination getByName(String name) {
        return getOne(Wrappers.<Origination>lambdaQuery().eq(Origination::getName, name), false);
    }

    @Override
    public List<Origination> listAll() {
        return list();
    }


    @Override
    public Origination judgeIsSame(String name, String code) {
        return getOne(Wrappers.<Origination>lambdaQuery().or().eq(!StringUtils.isEmpty(name), Origination::getName, name)
                .or().eq(!StringUtils.isEmpty(code), Origination::getCode, code), false);
    }

    @Override
    public List<Origination> getAllRoot() {
        return list(Wrappers.<Origination>lambdaQuery().eq(Origination::getPath, "-"));
    }

    @Override
    public List<Origination> getChildren(Long id) {
        Origination org = getById(id);
        if (org == null) {
            throw new BaseException(CanteenExceptionEnum.ORG_NOT_EXIST);
        }
        long parentId = ObjectUtil.getLong(org.getParentId());
        return list(Wrappers.<Origination>lambdaQuery()
                .likeLeft(parentId > 0, Origination::getPath, String.format("%s%s-", org.getPath(), org.getId()))
                .likeLeft(parentId < 1, Origination::getPath, String.format("%s-", org.getId()))
                .eq(Origination::getLevel, org.getLevel() + 1));
    }
}
