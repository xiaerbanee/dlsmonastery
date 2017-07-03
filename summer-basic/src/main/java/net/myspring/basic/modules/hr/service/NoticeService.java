package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

}
