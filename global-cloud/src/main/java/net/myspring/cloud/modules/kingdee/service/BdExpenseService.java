package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdExpense;
import net.myspring.cloud.modules.kingdee.repository.BdExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 费用项目
 * Created by lihx on 2017/8/9.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdExpenseService {
    @Autowired
    private BdExpenseRepository bdExpenseRepository;

    public List<BdExpense> findAll(){
        return bdExpenseRepository.findAll();
    }
}
