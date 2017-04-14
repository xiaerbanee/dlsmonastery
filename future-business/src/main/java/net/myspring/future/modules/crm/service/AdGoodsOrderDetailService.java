package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.AdGoodsOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdGoodsOrderDetailService {

    @Autowired
    private AdGoodsOrderDetailMapper adGoodsOrderDetailMapper;

}
