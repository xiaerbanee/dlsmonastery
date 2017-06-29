package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.repository.VoucherEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
@Transactional
public class VoucherEntryService {
    @Autowired
    private VoucherEntryRepository voucherEntryRepository;

}
