package net.myspring.basic.modules.hr.web.controller;

import gui.ava.html.image.generator.HtmlImageGenerator;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.service.AuditFileService;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping(value = "hr/auditFile")
public class AuditFileController {

    @Autowired
    private AuditFileService auditFileService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<AuditFileDto> data(Pageable pageable, AuditFileQuery auditFileQuery) {
        if(auditFileQuery.getAuditType()==null||auditFileQuery.getAuditType().equals("1")){
            auditFileQuery.setPositionId(SecurityUtils.getPositionId());
        }
        Page<AuditFileDto> page = auditFileService.findPage(pageable,auditFileQuery);
        return page;
    }


    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public AuditFileQuery getQuery(AuditFileQuery auditFileQuery) {
        return auditFileQuery;
    }


    @RequestMapping(value = "save")
    public RestResponse save(AuditFileForm auditFileForm, BindingResult result) {
        auditFileService.save(auditFileForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(String id) {
        auditFileService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "findForm", method = RequestMethod.GET)
    public AuditFileForm detail(AuditFileForm auditFileForm) {
        auditFileForm=auditFileService.findForm(auditFileForm);
        auditFileForm.setBoolMap(BoolEnum.getMap());
        return auditFileForm;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(AuditFile auditFile, boolean pass, String comment) {
        RestResponse restResponse = new RestResponse("审核成功",ResponseCodeEnum.audited.name());
        return restResponse;
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
