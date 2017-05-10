package net.myspring.cloud.modules.report.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.report.mapper.GlcxViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/5/10.
 */
@Service
@KingdeeDataSource
public class GlcxViewService {
    @Autowired
    private GlcxViewMapper glcxViewMapper;

    public List<String > findDefaultDepartment(){
       return glcxViewMapper.findTowDepartmentNumber();
    }

}
