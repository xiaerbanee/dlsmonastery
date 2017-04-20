package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ReportScoreArea;
import net.myspring.future.modules.crm.mapper.ReportScoreAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ReportScoreAreaService {

    @Autowired
    private ReportScoreAreaMapper reportScoreAreaMapper;

    public Page<ReportScoreArea> findPage(Pageable pageable, Map<String,Object> map){
        Page<ReportScoreArea> page=reportScoreAreaMapper.findPage(pageable,map);
        return page;
    }

    public ReportScoreArea findOne(String id){
        return reportScoreAreaMapper.findOne(id);
    }
}
