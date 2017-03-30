package net.myspring.hr.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.sys.mapper.FolderMapper;

@Service
public class FolderService {

    @Autowired
    private FolderMapper folderMapper;

}
