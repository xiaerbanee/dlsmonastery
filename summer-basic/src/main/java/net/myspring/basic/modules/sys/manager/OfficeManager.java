package net.myspring.basic.modules.sys.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.OfficeRuleEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.mapper.OfficeBusinessMapper;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.mapper.OfficeRuleMapper;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2017/4/6.
 */
@Component
public class OfficeManager {

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeRuleMapper officeRuleMapper;
    @Autowired
    private OfficeBusinessMapper officeBusinessMapper;

    @Cacheable(value = "offices", key = "#p0")
    public Office findOne(String id) {
        return officeMapper.findOne(id);
    }

    @CachePut(value = "offices", key = "#p0.id")
    public Office save(Office office) {
        officeMapper.save(office);
        return office;
    }

    @CachePut(value = "offices", key = "#p0.id")
    public Office update(Office office) {
        officeMapper.update(office);
        return officeMapper.findOne(office.getId());
    }

    @CachePut(value = "offices", key = "#p0.id")
    public Office updateForm(OfficeForm officeForm) {
        officeMapper.updateForm(officeForm);
        return officeMapper.findOne(officeForm.getId());
    }


    public List<String> officeFilter(String accountId) {
        List<String> officeIdList = Lists.newArrayList();
        Office office = officeMapper.findByAccountId(accountId);
        OfficeRule officeRule = officeRuleMapper.findOne(office.getId());
        if (!Const.HR_ACCOUNT_ADMIN_LIST.contains(accountId)) {
            if (OfficeRuleEnum.BUSINESS_OFFICE.name().equalsIgnoreCase(officeRule.getType())) {
                officeIdList.add(office.getId());
                officeIdList.addAll(CollectionUtil.extractToList(officeMapper.findByParentIdsLike(office.getParentId()), "id"));
            } else {
                officeIdList.addAll(officeBusinessMapper.findBusinessIdById(office.getId()));
            }
            if (CollectionUtil.isNotEmpty(officeIdList)) {
                officeIdList.add("0");
            }
        }
        officeIdList = Lists.newArrayList(Sets.newHashSet(officeIdList));
        return officeIdList;
    }

    public Office findByOfficeIdAndRuleName(String officeId, String ruleName) {
        Office office = null;
        OfficeRule officeRule = officeRuleMapper.findByName(ruleName);
        if (officeRule != null) {
            office = findByOfficeIdAndRuleId(officeId, officeRule.getId());
        }
        return office;
    }

    public Office findByOfficeIdAndRuleId(String officeId, String ruleId) {
        return officeMapper.findByOfficeIdAndRuleId(officeId, ruleId);
    }
}
