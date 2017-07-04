package net.myspring.uaa.security;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.uaa.dto.AccountDto;
import net.myspring.uaa.dto.AccountWeixinDto;
import net.myspring.uaa.dto.OfficeDto;
import net.myspring.uaa.dto.WeixinSessionDto;
import net.myspring.uaa.manager.OfficeManager;
import net.myspring.uaa.manager.PermissionManager;
import net.myspring.uaa.manager.WeixinManager;
import net.myspring.uaa.repository.AccountDtoRepository;
import net.myspring.uaa.repository.AccountWeixinDtoRepository;
import net.myspring.uaa.repository.CompanyConfigRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuj on 2017/4/1.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Value("${setting.adminIdList}")
    private String adminIdList;
    @Autowired
    private AccountDtoRepository accountDtoRepository;
    @Autowired
    private AccountWeixinDtoRepository accountWeixinDtoRepository;
    @Autowired
    private WeixinManager weixinManager;
    @Autowired
    private CompanyConfigRepository companyConfigRepository;
    @Autowired
    private OfficeManager officeManager;
    @Autowired
    private PermissionManager permissionManager;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = null;
        AccountDto accountDto = null;
        HttpServletRequest request  = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String weixinCode = request.getParameter("weixinCode");
        if(StringUtils.isNotBlank(weixinCode)) {
            String accountId = ObjectUtils.toString(request.getParameter("accountId"));
            WeixinSessionDto weixinSessionDto = weixinManager.findWeixinSessionDto(weixinCode);
            if(weixinSessionDto != null && StringUtils.isNotBlank(weixinSessionDto.getOpenid())) {
                List<AccountWeixinDto> accountWeixinDtoList = accountWeixinDtoRepository.findByOpenId(weixinSessionDto.getOpenid());
                if(CollectionUtil.isNotEmpty(accountWeixinDtoList)) {
                    Map<String,AccountWeixinDto>  accountWeixinDtoMap = CollectionUtil.extractToMap(accountWeixinDtoList,"accountId");
                    if(!accountWeixinDtoMap.containsKey(accountId)) {
                        accountId = null;
                    }
                    if(StringUtils.isBlank(accountId)) {
                        accountId = accountWeixinDtoList.get(0).getAccountId();
                    }
                }
            }
            if(StringUtils.isNotBlank(accountId)) {
                accountDto = accountDtoRepository.findOne(accountId);
            }
        } else {
            accountDto = accountDtoRepository.findByLoginName(username);
        }
        if(accountDto != null) {
            accountDto.setCompanyName(companyConfigRepository.findByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()));
            LocalDate leaveDate = accountDto.getLeaveDate();
            boolean accountNoExpired = leaveDate == null || leaveDate.isAfter(LocalDate.now());
            Set<SimpleGrantedAuthority> authList = Sets.newHashSet();
            authList.add(new SimpleGrantedAuthority(accountDto.getPositionId()));
            permissionManager.permissionCachePut(accountDto.getId(),accountDto.getRoleId());
            Boolean admin = StringUtils.getSplitList(adminIdList, CharConstant.COMMA).contains(accountDto.getId());
            OfficeDto officeDto = officeManager.findOne(accountDto.getOfficeId());
            Boolean allDataScope = officeDto.getAllDataScope();
            if(admin) {
                allDataScope = true;
            }
            List<String> officeIdList = Lists.newArrayList();
            if(!allDataScope) {
                officeIdList = officeManager.getOfficeIdList(accountDto.getOfficeId());
            }
            customUserDetails = new CustomUserDetails(
                    accountDto.getLoginName(),
                    accountDto.getPassword(),
                    accountDto.getEnabled(),
                    accountNoExpired,
                    true,
                    !accountDto.getLocked(),
                    authList,
                    accountDto.getId(),
                    accountDto.getPositionId(),
                    accountDto.getOfficeId(),
                    accountDto.getEmployeeId(),
                    accountDto.getCompanyName(),
                    accountDto.getRoleId(),
                    officeIdList,
                    admin
            );
        }
        return customUserDetails;
    }

}
