package net.myspring.hr.modules.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.hr.mapper.PositionMapper;

@Service
public class PositionService {

    @Autowired
    private PositionMapper positionMapper;

}