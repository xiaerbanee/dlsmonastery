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
        UPDATE #{#entityName} set enabled=:enabled where officeId=:officeId
    """)
    fun setEnabledByOfficeId(@Param("enabled") enabled: Boolean, @Param("officeId") officeId: String): Int

    @Query("""
        UPDATE  #{#entityName} set enabled=:enabled where leaderId in :leaderList
    """)
    fun setEnabledByLeaderIds(@Param("enabled") enabled: Boolean, @Param("leaderList") leaderList: MutableList<String>): Int

    @Query("""
        SELECT t1.*
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.officeId=:officeId
    """)
    fun findByOfficeId(officeId: String): MutableList<OfficeLeader>

    @Query("""
        SELECT t1.*
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.officeId=:officeId
    """)
    fun findAllByOfficeId(officeId: String): MutableList<OfficeLeader>

}
