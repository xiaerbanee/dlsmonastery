package net.myspring.basic.modules.sys.service;

import net.myspring.basic.modules.sys.manager.MonitorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorService {

    @Autowired
    private MonitorManager monitorManager;

}
