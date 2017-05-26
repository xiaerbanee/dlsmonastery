package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.repository.PriceChangeRepositoryCustom
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.DemoPhoneDto

import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


interface DemoPhoneRepository : BaseRepository<DemoPhone, String>,DemoPhoneRepositoryCustom {





}

interface DemoPhoneRepositoryCustom{
    fun findPage(pageable: Pageable, demoPhoneQuery : DemoPhoneQuery): Page<DemoPhoneDto>?
}

class DemoPhoneRepositoryImpl @Autowired constructor(val entityManager: EntityManager): DemoPhoneRepositoryCustom {
    override fun findPage(pageable: Pageable, demoPhoneQuery: DemoPhoneQuery): Page<DemoPhoneDto>? {
        return null


    }


}