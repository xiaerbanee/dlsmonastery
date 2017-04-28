package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.mapper.OfficeRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wangzm on 2017/4/28.
 */
@Component
public class OfficeRuleManager {

    @Autowired
    private OfficeRuleMapper officeRuleMapper;

}
