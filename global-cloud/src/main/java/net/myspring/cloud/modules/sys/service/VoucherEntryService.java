package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.repository.VoucherEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class VoucherEntryService {
    @Autowired
    private VoucherEntryRepository voucherEntryRepository;

}
