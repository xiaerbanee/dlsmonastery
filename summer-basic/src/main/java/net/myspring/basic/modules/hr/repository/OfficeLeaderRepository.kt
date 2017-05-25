package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.OfficeLeader
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by lihx on 2017/5/25.
 */
interface OfficeLeaderRepository : BaseRepository<OfficeLeader,String>{
    @Query("""
        UPDATE  hr_office_leader set enabled=:enabled where office_id=:officeId
    """, nativeQuery = true)
    fun setEnabledByOfficeId(@Param("enabled") enabled: Boolean, @Param("officeId") officeId: String): Int

    @Query("""
        UPDATE  hr_office_leader set enabled=:enabled where leader_id in :leaderList
    """, nativeQuery = true)
    fun setEnabledByLeaderIds(@Param("enabled") enabled: Boolean, @Param("leaderList") leaderList: List<String>): Int

    @Query("""
        SELECT t1.*
        FROM hr_office_leader t1
        where t1.enabled=1
        and t1.office_id=:officeId
    """, nativeQuery = true)
    fun findByOfficeId(officeId: String): List<OfficeLeader>

    @Query("""
        SELECT t1.*
        FROM hr_office_leader t1
        where t1.enabled=1
        and t1.office_id=:officeId
    """, nativeQuery = true)
    fun findAllByOfficeId(officeId: String): List<OfficeLeader>

}
