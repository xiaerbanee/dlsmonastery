package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import gui.ava.html.image.generator.HtmlImageGenerator;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.enums.FolderDefaultEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.service.AuditFileService;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.service.FolderService;
import net.myspring.basic.modules.sys.service.ProcessTypeService;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/auditFile")
public class AuditFileController {

    @Autowired
    private AuditFileService auditFileService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String data(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        if(searchEntity.getParams().get("auditType")==null||searchEntity.getParams().get("auditType").equals("1")){
            searchEntity.getParams().put("positionId",securityUtils.getPositionId());
        }
        Page<AuditFileDto> page = auditFileService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("folder", folderService.getAccountFolder(securityUtils.getAccountId(), FolderDefaultEnum.AUDIT_FILE.toString()));
        map.put("processTypes", processTypeService.findEnabledAuditFileType());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("folder", folderService.getAccountFolder(securityUtils.getAccountId(), FolderDefaultEnum.AUDIT_FILE.toString()));
        map.put("processTypes", processTypeService.findEnabledAuditFileType());
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "save")
    public RestResponse save(AuditFileForm auditFileForm, BindingResult result) {
        ProcessType processType = processTypeService.findOne(auditFileForm.getProcessTypeId());
        auditFileForm.setProcessType(processType);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(String id) {
        auditFileService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }


    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(AuditFileForm auditFileForm) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("auditFile", auditFileForm);
        paramMap.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(paramMap);
    }

    @RequestMapping(value = "audit")
    public String audit(AuditFile auditFile, boolean pass, String comment) {
        RestResponse restResponse = new RestResponse("审核成功",ResponseCodeEnum.audited.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public void view(AuditFile auditFile, HttpServletResponse response) {
        String html = "<div style=\"width:300px;word-break:break-all;\">" +  HtmlUtils.htmlUnescape(auditFile.getContent()) + "</div>";
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.loadHtml(html);
        BufferedImage bufferedImage = imageGenerator.getBufferedImage();
        response.setContentType("image/png");
        try {
            ImageIO.write(bufferedImage,"png",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
