package net.myspring.hr.modules.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.hr.modules.hr.service.NoticeService;

@Controller
@RequestMapping(value = "hr/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

}
