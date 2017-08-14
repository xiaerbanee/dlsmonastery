package net.myspring.basic.modules.hr.web.controller;

import gui.ava.html.image.generator.HtmlImageGenerator;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.service.AccountFavoriteService;
import net.myspring.basic.modules.hr.service.AuditFileService;
import net.myspring.basic.modules.hr.web.form.AuditFileForm;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private AccountFavoriteService accountFavoriteService;


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:view')")
    public Page<AuditFileDto> list(Pageable pageable, AuditFileQuery auditFileQuery) {
        Page<AuditFileDto> page = auditFileService.findPage(pageable,auditFileQuery);
        return page;
    }


    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:view')")
    public AuditFileQuery getQuery(AuditFileQuery auditFileQuery) {
        String accountId= RequestUtils.getAccountId();
        auditFileQuery.getExtra().put("accountFavoriteList",accountFavoriteService.findAll());
        return auditFileQuery;
    }

    @RequestMapping(value = "getForm", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:view')")
    public AuditFileForm getForm(AuditFileForm auditFileForm) {
        return auditFileForm;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'hr:auditFile:edit')")
    public RestResponse save(AuditFileForm auditFileForm, BindingResult result) {
        auditFileService.save(auditFileForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:delete')")
    public RestResponse delete(String id) {
        auditFileService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "findOne", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:view')")
    public AuditFileDto findOne(AuditFileDto auditFileDto) {
        auditFileDto=auditFileService.findOne(auditFileDto);
        return auditFileDto;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(String id, boolean pass, String comment) {
        RestResponse restResponse = new RestResponse("文件审核成功",null);
        AuditFile auditFile=auditFileService.findOne(id);
        if(!RequestUtils.getPositionIdList().contains(auditFile.getPositionId())){
            return new RestResponse("文件审核失败,当前审批岗位与你的岗位不符",null);
        }
        auditFileService.audit(id, pass, comment);
        return restResponse;
    }

    @RequestMapping(value="updateMemo")
    public RestResponse updateMemo(String id,String memo){
        auditFileService.updateMemo(id,memo);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:auditFile:view')")
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
