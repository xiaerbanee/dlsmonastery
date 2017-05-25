package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountWeixinDto
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.Query

@Component
class AccountWeixinDtoRepository @Autowired constructor(val entityManager: EntityManager) {
    fun findByOpenId(openId: String): List<AccountWeixinDto> {
        return entityManager.createNativeQuery("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.open_id = :openId
                    and t1.enabled=1
                """).setParameter("openId",openId).resultList as List<AccountWeixinDto>;
    }

    fun findByAccountId(accountId: String): AccountWeixinDto {
        return entityManager.createNativeQuery("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.account_id = :accountId
                    and t1.enabled=1
                """).setParameter("accountId",accountId).resultList as AccountWeixinDto;
    }

    fun save(accountWeixinDto: AccountWeixinDto) {
        var query: Query;
        if(StringUtils.isBlank(accountWeixinDto.id)) {
            query = entityManager.createNativeQuery("INSERT  INTO hr_account_weixin(account_id,company_id,open_id) VALUE (:accountId,:companyId,:openId)");
        } else {
            query =  entityManager.createNativeQuery("UPDATE  hr_account_weixin SET acount_id=:accountId,company_id=:companyId,openId=:openId where id=:id");
        }
        QueryUtils.setParameter(query,accountWeixinDto);
        query.executeUpdate();
    }
}