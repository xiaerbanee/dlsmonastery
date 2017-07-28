package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BasAssistant;
import net.myspring.cloud.modules.kingdee.repository.BasAssistantRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


/**
 * 辅助资料
 * Created by lihx on 2017/6/13.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BasAssistantService {
    @Autowired
    private BasAssistantRepository basAssistantRepository;

    public String findNumberSubByName(String nameHtml){
        if (StringUtils.isNotBlank(nameHtml)){
            String name = HtmlUtils.htmlUnescape(nameHtml);
            return basAssistantRepository.findByName(name).getFNumber().substring(0,4);
        }
        return null;
    }

    public List<BasAssistant> findByType(String type){
        return basAssistantRepository.findByType(type);
    }
}
