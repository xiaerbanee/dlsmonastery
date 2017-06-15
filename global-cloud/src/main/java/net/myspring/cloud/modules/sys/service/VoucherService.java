package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.repository.VoucherRepository;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 凭证
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public Page<VoucherDto> findPage(Pageable pageable, VoucherQuery voucherQuery){
        Page<VoucherDto> page = voucherRepository.findPage(pageable,voucherQuery);
        return page;
    }
}
