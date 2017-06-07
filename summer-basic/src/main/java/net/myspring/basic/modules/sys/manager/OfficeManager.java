package net.myspring.basic.modules.sys.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.repository.OfficeBusinessRepository;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.basic.modules.sys.repository.OfficeRuleRepository;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/6.
 */
@Component
public class OfficeManager {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private OfficeBusinessRepository officeBusinessRepository;
    @Value("${setting.adminIdList}")
    private String adminIdList;

    public List<String> officeFilter(String officeId) {
        List<String> officeIdList = Lists.newArrayList();
        if (!StringUtils.getSplitList(adminIdList, CharConstant.COMMA).contains(RequestUtils.getAccountId())) {
            Office office = officeRepository.findOne(officeId);
            officeIdList.add(office.getId());
            if (OfficeTypeEnum.职能部门.name().equalsIgnoreCase(office.getType())) {
                officeIdList.addAll(CollectionUtil.extractToList(officeRepository.findByParentIdsLike(office.getParentId()), "id"));
            } else {
                List<OfficeBusiness> businessList = officeBusinessRepository.findBusinessIdById(office.getId());
                if (CollectionUtil.isNotEmpty(businessList)) {
                    List<String> officeIds = CollectionUtil.extractToList(businessList, "id");
                    officeIdList.addAll(officeIds);
                    List<Office> childOfficeList = officeRepository.findByParentIdsListLike(officeIds);
                    officeIdList.addAll(CollectionUtil.extractToList(childOfficeList, "id"));
                }
            }
            if (CollectionUtil.isNotEmpty(officeIdList)) {
                officeIdList.add("0");
            }
        }
        officeIdList = Lists.newArrayList(Sets.newHashSet(officeIdList));
        return officeIdList;
    }

    public List<Office> findByOfficeIdAndRuleName(String officeId, String ruleName) {
        List<Office> officeList =Lists.newArrayList();
        OfficeRule officeRule = officeRuleRepository.findByName(ruleName);
        if (officeRule != null) {
            officeList = findByOfficeIdAndRuleId(officeId, officeRule.getId());
        }
        return officeList;
    }

    public List<Office> findByOfficeIdAndRuleId(String officeId, String ruleId) {
        List<Office> officeList = officeRepository.findByOfficeIdAndRuleId(officeId, ruleId);
        return officeList;
    }

    //根据officeId获取某个级别的Office
    public  String getOfficeIdByOfficeRule(String officeId, String officeRuleId) {
        Office office = officeRepository.findOne(officeId);
        if(office!=null){
            if (officeRuleId.equals(office.getOfficeRuleId())) {
                return officeId;
            } else {
                Office parent = officeRepository.findOne(office.getParentId());
                for (int i = 1; i < 10; i++) {
                    if (parent != null) {
                        if (officeRuleId.equals(parent.getOfficeRuleId())) {
                            return parent.getId();
                        } else {
                            parent = officeRepository.findOne(parent.getParentId());
                        }
                    }
                }
            }
            return office.getParentId() == null ? officeId : office.getParentId();
        }
        return null;
    }
}
