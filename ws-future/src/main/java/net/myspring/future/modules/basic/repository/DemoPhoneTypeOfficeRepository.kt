package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice

/**
 * Created by zhangyf on 2017/5/24.
 */
interface DemoPhoneTypeOfficeRepository : BaseRepository<DemoPhoneTypeOffice,String>{

    fun findByDemoPhoneTypeId(demoPhoneTypeId: String): MutableList<DemoPhoneTypeOffice>
}