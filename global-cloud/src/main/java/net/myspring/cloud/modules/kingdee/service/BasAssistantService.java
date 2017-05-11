package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.mapper.BasAssistantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class BasAssistantService {
    @Autowired
    private BasAssistantMapper basAssistantMapper;

    public String findFNumberByNameAndDataValue(String lbName,String name){
        return basAssistantMapper.findFNumberByNameAndDataValue(lbName,name);
    }
}
