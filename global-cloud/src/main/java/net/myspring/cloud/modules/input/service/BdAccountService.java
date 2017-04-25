package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.mapper.BdAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by lihx on 2017/4/6.
 */
@Service
@KingdeeDataSource
public class BdAccountService {
    @Autowired
    private BdAccountMapper bdAccountMapper;

    public String findFNumberByName(String name){
        return bdAccountMapper.findFNumberByName(name);
    }
}
