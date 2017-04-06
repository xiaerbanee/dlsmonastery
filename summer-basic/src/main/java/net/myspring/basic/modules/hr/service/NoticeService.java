package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

}
