package net.myspring.future.modules.layout.service;

import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdGoodsOrderDetailService {

    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;

}
