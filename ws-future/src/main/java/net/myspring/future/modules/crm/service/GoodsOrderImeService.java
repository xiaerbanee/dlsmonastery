package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.mapper.GoodsOrderImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsOrderImeService {

    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;


    public List<GoodsOrderImeDto> findDtoList(String goodsOrderId) {
        return goodsOrderImeMapper.findDtoListByGoodsOrderId(goodsOrderId);
    }
}
