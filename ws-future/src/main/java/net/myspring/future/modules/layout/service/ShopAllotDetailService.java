package net.myspring.future.modules.layout.service;

import net.myspring.future.modules.layout.mapper.ShopAllotDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopAllotDetailService {

    @Autowired
    private ShopAllotDetailMapper shopAllotDetailMapper;

}
