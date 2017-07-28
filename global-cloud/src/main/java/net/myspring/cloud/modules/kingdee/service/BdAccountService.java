package net.myspring.cloud.modules.kingdee.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdAccount;
import net.myspring.cloud.modules.kingdee.repository.BdAccountRepository;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 账户（科目）
 * Created by lihx on 2017/6/8.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class BdAccountService {
    @Autowired
    private BdAccountRepository bdAccountRepository;

    public BdAccount findByNumber(String numberHtml){
        if (StringUtils.isNotBlank(numberHtml)) {
            String number = HtmlUtils.htmlUnescape(numberHtml);
            return bdAccountRepository.findByNumber(number);
        }
        return null;
    }

    public BdAccount findByName(String nameHtml){
        if (StringUtils.isNotBlank(nameHtml)){
            String name = HtmlUtils.htmlUnescape(nameHtml);
            return bdAccountRepository.findByName(name);
        }
        return null;
    }

    public List<BdAccount> findAll(){
        return bdAccountRepository.findAll();
    }
}
