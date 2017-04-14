package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import net.myspring.future.modules.crm.mapper.ReportScoreOfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ReportScoreOfficeService {

    @Autowired
    private ReportScoreOfficeMapper reportScoreOfficeMapper;

    public Page<ReportScoreOffice> findPage(Pageable pageable, Map<String,Object> map){
        Page<ReportScoreOffice> page=reportScoreOfficeMapper.findPage(pageable,map);
        return page;
    }

    public ReportScoreOffice findOne(String id){
        return reportScoreOfficeMapper.findOne(id);
    }
}
