package net.myspring.basic.modules.sys.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.mapper.OfficeBusinessMapper;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.mapper.OfficeRuleMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Created by admin on 2017/4/6.
 */
@Component
public class OfficeManager {

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private OfficeRuleMapper officeRuleMapper;
    @Autowired
    private OfficeBusinessMapper officeBusinessMapper;

    public List<String> officeFilter(String accountId) {
        List<String> officeIdList = Lists.newArrayList();
        Office office = officeMapper.findByAccountId(accountId);
        if (!Const.HR_ACCOUNT_ADMIN_LIST.contains(accountId)) {
            if (OfficeTypeEnum.BUSINESS.name().equalsIgnoreCase(office.getType())) {
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

    public String findByOfficeIdAndRuleName(String officeId, String ruleName) {
        String id = null;
        OfficeRule officeRule = officeRuleMapper.findByName(ruleName);
        if (officeRule != null) {
            id = findByOfficeIdAndRuleId(officeId, officeRule.getId());
        }
        return id;
    }

    public String findByOfficeIdAndRuleId(String officeId, String ruleId) {
        Office office = officeMapper.findByOfficeIdAndRuleId(officeId, ruleId);
        if (office != null) {
            return office.getId();
        }
        return null;
    }
}
