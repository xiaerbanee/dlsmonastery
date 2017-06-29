package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdSupplier;
import net.myspring.cloud.modules.kingdee.repository.BdSupplierRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 供应商
 * Created by lihx on 2017/6/13.
 */
@Service
@KingdeeDataSource
@Transactional
public class BdSupplierService {
    @Autowired
    private BdSupplierRepository bdSupplierRepository;

    public List<BdSupplier> findByNameLike(String name){
        if(StringUtils.isNotBlank(name)){
            return bdSupplierRepository.findByNameLike(name);
        }
        return null;
    }

    public List<BdSupplier> findAll(){
        return bdSupplierRepository.findAll();
    }
}
