package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.mapper.GoodsOrderImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsOrderImeService {

    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;

}
