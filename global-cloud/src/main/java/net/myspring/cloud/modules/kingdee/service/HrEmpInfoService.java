package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.HrEmpInfo;
import net.myspring.cloud.modules.kingdee.repository.HrEmpInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 银行账户
 * Created by lihx on 2017/6/16.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class HrEmpInfoService {
    @Autowired
    private HrEmpInfoRepository hrEmpInfoRepository;

    public List<HrEmpInfo> findAll(){
        return hrEmpInfoRepository.findAll();
    }

    public HrEmpInfo findByName(String name){
        return hrEmpInfoRepository.findByName(name);
    }
}
