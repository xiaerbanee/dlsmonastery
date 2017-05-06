package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.future.modules.crm.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ReportScoreService {

    @Autowired
    private ReportScoreMapper reportScoreMapper;
    @Autowired
    private ReportScoreAreaMapper reportScoreAreaMapper;
    @Autowired
    private ReportScoreOfficeMapper reportScoreOfficeMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private DepotMapper depotMapper;

    public ReportScore findOne(String id) {
        ReportScore reportScore = reportScoreMapper.findOne(id);
        return reportScore;
    }

    public Page<ReportScore> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ReportScore> page = reportScoreMapper.findPage(pageable, map);
        return page;
    }

    @Transactional
    public void save(ReportScore reportScore){
    }

    //获取真实打分
    private void setRealScore(ReportScore reportScore) {
        reportScore.setScore(reportScore.getCompanyScore());
        reportScore.setMonthScore(reportScore.getCompanyMonthScore());
    }

    @Transactional
    public void delete(ReportScore reportScore){
        reportScoreMapper.logicDeleteOne(reportScore.getId());
    }
}
