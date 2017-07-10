package net.myspring.general.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.dto.ProcessTypeDto;
import net.myspring.general.modules.sys.service.ProcessFlowService;
import net.myspring.general.modules.sys.service.ProcessTypeService;
import net.myspring.general.modules.sys.web.form.ProcessTypeForm;
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Request;

import java.util.List;

@RestController
@RequestMapping(value = "sys/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ProcessTypeDto> list(Pageable pageable, ProcessTypeQuery processTypeQuery){
        Page<ProcessTypeDto> page = processTypeService.findPage(pageable,processTypeQuery);
        return page;
    }

    @RequestMapping(value = "findOne")
    public ProcessTypeDto list(ProcessTypeDto processTypeDto){
        processTypeDto=processTypeService.findOne(processTypeDto);
        return processTypeDto;
    }

    @RequestMapping(value = "getForm")
    public ProcessTypeForm list(ProcessTypeForm processTypeForm){
        return processTypeForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        processTypeService.logicDelete(id);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProcessTypeForm processTypeForm) {
        processTypeForm.setCreatedPositionIds(StringUtils.join(processTypeForm.getCreatedPositionIdList(), CharConstant.COMMA));
        processTypeForm.setViewPositionIds(StringUtils.join(processTypeForm.getViewPositionIdList(), CharConstant.COMMA));
        processTypeService.save(processTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findByCreatePositionId")
    public List<ProcessTypeDto> findByCreatePositionId(){
        List<ProcessTypeDto> processTypeDtoList=Lists.newArrayList();
        if(RequestUtils.getAdmin()){
            processTypeDtoList=processTypeService.findAll();
        }else {
            processTypeDtoList=processTypeService.findByCreatePositionId(RequestUtils.getPositionId());
        }
        return processTypeDtoList;
    }

    @RequestMapping(value = "findIdByViewPositionId")
    public List<String> findIdByViewPositionId(){
        List<String> ids= Lists.newArrayList();
        if(!RequestUtils.getAdmin()){
            List<ProcessTypeDto> processTypeDtos=processTypeService.findByViewPositionId(RequestUtils.getPositionId());
            ids=CollectionUtil.extractToList(processTypeDtos,"id");
        }
        return ids;
    }

    @RequestMapping(value = "getQuery")
    public ProcessTypeQuery getQuery(ProcessTypeQuery processTypeQuery){
        return processTypeQuery;
    }
}
