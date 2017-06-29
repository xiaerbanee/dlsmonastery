package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BasAssistant;
import net.myspring.cloud.modules.kingdee.repository.BasAssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * 辅助资料
 * Created by lihx on 2017/6/13.
 */
@Service
@KingdeeDataSource
@Transactional
public class BasAssistantService {
    @Autowired
    private BasAssistantRepository basAssistantRepository;

    public String findNumberSubByName(String name){
        return basAssistantRepository.findByName(name).getFNumber().substring(0,4);
    }

    public List<BasAssistant> findByType(String type){
        return basAssistantRepository.findByType(type);
    }
}
