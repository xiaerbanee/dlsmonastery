package net.myspring.basic.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.FrontendMenuMapper;

@Service
public class FrontendMenuService {

    @Autowired
    private FrontendMenuMapper frontendMenuMapper;

}
