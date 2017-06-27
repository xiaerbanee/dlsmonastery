package net.myspring.tool.modules.oppo.service;


import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.client.CustomerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@LocalDataSource
public class OppoPlantAgentProductSelService {

    @Autowired
    private CustomerClient customerClient;
}
