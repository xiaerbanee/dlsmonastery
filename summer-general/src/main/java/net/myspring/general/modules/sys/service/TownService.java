package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.mapper.TownMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TownService {

    @Autowired
    private TownMapper townMapper;


}
