package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.SysDataSource;
import net.myspring.cloud.modules.sys.mapper.GlVoucherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@SysDataSource
public class GlVoucherService {
    @Autowired
    private GlVoucherMapper glVoucherMapper;
}
