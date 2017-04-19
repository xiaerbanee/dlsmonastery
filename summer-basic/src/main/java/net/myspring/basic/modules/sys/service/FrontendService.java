package net.myspring.basic.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.FrontendMapper;

@Service
public class FrontendService {

    @Autowired
    private FrontendMapper frontendMapper;

}
