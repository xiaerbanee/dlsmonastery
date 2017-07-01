package net.myspring.cloud.modules.kingdee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdSupplier;
import net.myspring.cloud.modules.kingdee.repository.BdSupplierRepository;
import net.myspring.cloud.modules.kingdee.web.query.BdSupplierQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public Page<BdSupplier> findPageIncludeForbid(Pageable pageable, BdSupplierQuery bdSupplierQuery) {
        bdSupplierQuery.setSort("t1.fsupplierid,DESC");
        Page<BdSupplier> bdCustomerPage= bdSupplierRepository.findPageIncludeForbid(pageable,bdSupplierQuery);
        return bdCustomerPage;
    }

    public List<BdSupplier> findByNameLike(String name){
        if(StringUtils.isNotBlank(name)){
            return bdSupplierRepository.findByNameLike(name);
        }
        return null;
    }

    public List<BdSupplier> findAll(){
        return bdSupplierRepository.findAll();
    }

    //应付报表
    public BdSupplierQuery getQueryForSupplierPayable(){
        BdSupplierQuery bdSupplierQuery = new BdSupplierQuery();
        bdSupplierQuery.setSort("t1.fsupplierid,DESC");
        return bdSupplierQuery;
    }
}
