package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.OfficeChnageTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.dto.OfficeChangeDto;
import net.myspring.basic.modules.hr.dto.OfficeChangeFormDto;
import net.myspring.basic.modules.hr.repository.OfficeChangeRepository;
import net.myspring.basic.modules.hr.web.form.OfficeChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.hr.web.query.OfficeChangeQuery;
import net.myspring.basic.modules.sys.client.ActivitiClient;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.utils.HandsontableUtils;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OfficeChangeService {

    @Autowired
    private OfficeChangeRepository officeChangeRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;


    public Page<OfficeChangeDto> findPage(Pageable pageable, OfficeChangeQuery officeChangeQuery){
        Page<OfficeChangeDto> page=officeChangeRepository.findPage(pageable,officeChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public OfficeChange findOne(String id){
        OfficeChange officeChange=officeChangeRepository.findOne(id);
        return officeChange;
    }

    public OfficeChangeForm getForm(OfficeChangeForm officeChangeForm){
        if(!officeChangeForm.isCreate()){
            OfficeChange officeChange=findOne(officeChangeForm.getId());
            officeChangeForm= BeanUtil.map(officeChange,OfficeChangeForm.class);
            cacheUtils.initCacheInput(officeChangeForm);
        }
        return officeChangeForm;
    }

    public List<OfficeChangeFormDto> findByOfficeId(String officeId){
        List<OfficeDto> officeList=officeRepository.findDtoByParentIdsLike(officeId);
        List<OfficeChangeFormDto> officeChangeFormDtos= BeanUtil.map(officeList,OfficeChangeFormDto.class);
        cacheUtils.initCacheInput(officeChangeFormDtos);
        return officeChangeFormDtos;
    }

    @Transactional
    public void save(String id,String json){
        List<List<Object>> data = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(json), ArrayList.class);
        List<String> parentNameList = Lists.newArrayList();
        List<String> idList = Lists.newArrayList();
        BigDecimal afterTaskPoint = BigDecimal.ZERO;
        for(List<Object> row: data){
            idList.add(HandsontableUtils.getValue(row,0));
            parentNameList.add(HandsontableUtils.getValue(row,5));
            String tempTaskPoint = HandsontableUtils.getValue(row,7);
            if (StringUtils.isNotBlank(tempTaskPoint)){
                afterTaskPoint = afterTaskPoint.add(new BigDecimal(tempTaskPoint));
            }
        }
        List<OfficeChangeFormDto> officeChangeFormDtos = findByOfficeId(id);
        BigDecimal beforeTaskPoint = BigDecimal.ZERO;
        for (OfficeChangeFormDto officeChangeFormDto : officeChangeFormDtos){
            if (officeChangeFormDto.getTaskPoint() != null){
                beforeTaskPoint = beforeTaskPoint.add(officeChangeFormDto.getTaskPoint());
            }
        }
        if (afterTaskPoint.compareTo(beforeTaskPoint) == 0) {
            Map<String, Office> officeNameMap = officeRepository.findByNameIn(parentNameList).stream().collect(Collectors.toMap(Office::getName, Office -> Office));
            Map<String, Office> officeIdMap = officeRepository.findMap(idList);
            List<OfficeChange> officeChangeList=Lists.newArrayList();
            for (List<Object> row : data) {
                String officeId = HandsontableUtils.getValue(row, 0);
                String parentName = HandsontableUtils.getValue(row, 5);
                String name = HandsontableUtils.getValue(row, 6);
                String taskPoint = HandsontableUtils.getValue(row, 7);
                Office office = officeIdMap.get(officeId);
                Office parent = officeNameMap.get(parentName);
                if(!office.getParentId().equals(parent.getId())){
                    OfficeChange officeChange=new OfficeChange();
                    officeChange.setType(OfficeChnageTypeEnum.上级.name());
                    officeChange.setNewLabel(parentName);
                    officeChange.setNewValue(parent.getId());
                    officeChange.setOldLabel(HandsontableUtils.getValue(row, 1));
                    officeChange.setOldValue(office.getParentId());
                    officeChange.setOfficeId(officeId);
                    officeChangeList.add(officeChange);
                }
                if(office.getTaskPoint().compareTo(new BigDecimal(taskPoint))!=0){
                    OfficeChange officeChange=new OfficeChange();
                    officeChange.setType(OfficeChnageTypeEnum.任务点位.name());
                    officeChange.setNewLabel(taskPoint);
                    officeChange.setNewValue(taskPoint);
                    officeChange.setOldLabel(office.getTaskPoint().toString());
                    officeChange.setOldValue(office.getTaskPoint().toString());
                    officeChange.setOfficeId(officeId);
                    officeChangeList.add(officeChange);
                }
                if(!office.getName().equals(name)){
                    OfficeChange officeChange=new OfficeChange();
                    officeChange.setType(OfficeChnageTypeEnum.名称.name());
                    officeChange.setNewLabel(name);
                    officeChange.setNewValue(name);
                    officeChange.setOldLabel(office.getName());
                    officeChange.setOldValue(office.getName());
                    officeChange.setOfficeId(officeId);
                    officeChange.setProcessStatus("申请中");
                    officeChangeList.add(officeChange);
                }
                officeChangeRepository.save(officeChangeList);
            }
        }else {
            throw new ServiceException("任务点位总和与修改之前总和不一样");
        }
    }

    @Transactional
    public void audit(String id,boolean pass,String comment) {
        OfficeChange officeChange = officeChangeRepository.findOne(id);
        if (pass) {
            officeChange.setProcessStatus("已通过");
            Office office=officeRepository.findOne(officeChange.getOfficeId());
            if (officeChange.getType().equals(OfficeChnageTypeEnum.名称.toString())) {
                office.setName(officeChange.getNewValue());
                if(office.getName().contains("废弃")||office.getName().contains("停用")){
                    office.setEnabled(false);
                }
            } else if (officeChange.getType().equals(OfficeChnageTypeEnum.上级.toString())) {
                office.setParentId(officeChange.getNewValue());
            } else if (officeChange.getType().equals(OfficeChnageTypeEnum.任务点位.toString())) {
                office.setTaskPoint(new BigDecimal(officeChange.getNewValue()));
            }
            officeRepository.save(office);
        }else {
            officeChange.setProcessStatus("未通过");
        }
        officeChangeRepository.save(officeChange);
    }
    @Transactional
    public void batchPass(String[] ids, boolean pass){
        List<String> idList= Arrays.asList(ids);
        for(String id:idList){
            audit(id,pass,"批量审核");
        }
    }

}
