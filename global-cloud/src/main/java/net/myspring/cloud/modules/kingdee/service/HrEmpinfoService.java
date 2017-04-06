package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.mapper.HrEmpinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class HrEmpinfoService {
    @Autowired
    private HrEmpinfoMapper hrEmpinfoMapper;

    public String findFNumberByName(String name){
        return hrEmpinfoMapper.findFNumberByName(name);
    }
}
