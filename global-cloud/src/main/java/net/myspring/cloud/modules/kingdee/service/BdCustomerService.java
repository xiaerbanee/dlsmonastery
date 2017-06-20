package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.repository.BdCustomerRepository;
import net.myspring.cloud.modules.kingdee.web.query.BdCustomerQuery;
import net.myspring.common.dto.NameValueDto;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户
 * Created by lihx on 2017/5/12.
 */
@Service
@KingdeeDataSource
public class BdCustomerService {
    @Autowired
    private BdCustomerRepository bdCustomerRepository;

    public Page<BdCustomer> findPageIncludeForbid(Pageable pageable, BdCustomerQuery bdCustomerQuery) {
         Page<BdCustomer> bdCustomerPage= bdCustomerRepository.findPageIncludeForbid(pageable,bdCustomerQuery);
        return bdCustomerPage;
    }

    public List<BdCustomer> findByNameLike(String name) {
        if (StringUtils.isNotBlank(name)){
            return bdCustomerRepository.findByNameLike(name);
        }
        return null;
    }

    public List<BdCustomer> findAll(){
        return bdCustomerRepository.findAll();
    }

    public List<BdCustomer> findByMaxModifyDate(LocalDateTime modifyDate){
        return bdCustomerRepository.findByMaxModifyDate(modifyDate);
    }
}
