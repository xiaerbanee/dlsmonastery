package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.RetailReportEnum;
import net.myspring.cloud.modules.sys.domain.DynamicSubject;
import net.myspring.cloud.modules.sys.mapper.DynamicSubjectMapper;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Created by lihx on 2017/2/10.
 */
@Service
@LocalDataSource
public class DynamicSubjectForRetailReportService {
    @Autowired
    private DynamicSubjectMapper dynamicSubjectMapper;

    public void filterAndAddSubject(List<DynamicSubject> newManagementFeeList){
        List<DynamicSubject> oldSubjectList = dynamicSubjectMapper.findByAccName("管理费用");
        HashSet<DynamicSubject> differentSubjectList = new HashSet<DynamicSubject>();
        List<String> oldFyNumList = CollectionUtil.extractToList(oldSubjectList,"fyNum");
        for(DynamicSubject newSubject : newManagementFeeList){
            if (!oldFyNumList.contains(newSubject.getFyNum())){
                differentSubjectList.add(newSubject);
            }
        }
        if(differentSubjectList.size() > 0 ){
            for(DynamicSubject differentSubject : differentSubjectList){
                if(!differentSubject.getFyNum().equals(RetailReportEnum.price_adjustment.getFyNum()) &&
                        !differentSubject.getFyNum().equals(RetailReportEnum.sales_allowance.getFyNum()) &&
                        !differentSubject.getFyNum().equals(RetailReportEnum.advertising_fee.getFyNum()) &&
                        !differentSubject.getFyNum().equals("6602.026") &&
                        !differentSubject.getFyNum().equals("004"))
                dynamicSubjectMapper.add(differentSubject.getAccNumber(),differentSubject.getAccName(),differentSubject.getFyNum(),differentSubject.getFyName());
            }
        }
    }

    public List<DynamicSubject> findCloudDynamicSubjectList(List<DynamicSubject> newManagementFeeList){
        filterAndAddSubject(newManagementFeeList);
        List<DynamicSubject> dynamicSubjectList = dynamicSubjectMapper.findByAccName("管理费用");
        return dynamicSubjectList;
    }

}
