package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountWeixinDto
import org.springframework.data.jpa.repository.Query

interface AccountWeixinDtoRepository{
    @Query("""
        SELECT
        t1.company_id,
        t1.account_id,
        t1.open_id
        FROM
        hr_account_weixin t1
        WHERE
        t1.open_id = ?1
        and t1.enabled=1
        """, nativeQuery = true)
    fun findByOpenId(openId: String): List<AccountWeixinDto>

    @Query("""
        SELECT
        t1.company_id,
        t1.account_id,
        t1.open_id
        FROM
        hr_account_weixin t1
        WHERE
        t1.account_id = ?1
        and t1.enabled=1
        """, nativeQuery = true)
    fun findByAccountId(accountId: String): AccountWeixinDto

    @Query("""
        INSERT  INTO hr_account_weixin(account_id,company_id,open_id) VALUE (?#{[0].accountId},?#{[0].companyId},?#{[0].openId})
        """, nativeQuery = true)
    fun save(accountWeixinDto: AccountWeixinDto): Int

    @Query("""
        UPDATE  hr_account_weixin SET acount_id=?#{[0].accountId},company_id=?#{[0].companyId},openId=?#{[0].openId} where id=?#{[0].id}
        """, nativeQuery = true)
    fun update(accountWeixinDto: AccountWeixinDto): Int
}
