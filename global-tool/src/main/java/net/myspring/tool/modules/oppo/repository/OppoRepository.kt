package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/29.
 */
@Repository
interface OppoRepository {

    @Query("""
       call plantProductSel (
              :companyId,
              :password,
              :branchId
          )
        """, nativeQuery = true)
    fun plantProductSel(@Param("companyId") companyId: String, @Param("password") password: String, @Param("branchId") branchId: String): List<OppoPlantProductSel>


    @Query("""
         call PlantAgentProductSel (
            :companyId,
            :password,
            :branchId
        )
        """, nativeQuery = true)
    fun plantAgentProductSel(@Param("companyId") companyId: String, @Param("password") password: String, @Param("branchId") branchId: String): List<OppoPlantAgentProductSel>

    @Query("""
        call PlantSendImeiPPSel (
            :companyId,
            :password,
            :createdTime
        )
        """, nativeQuery = true)
    fun plantSendImeiPPSel(@Param("companyId") companyId: String, @Param("password") password: String, @Param("createdTime") createdTime: LocalDate): List<OppoPlantSendImeiPpsel>

    @Query("""
        call PlantProductItemelectronSel (
         :companyId,
         :password,
         :systemDate
        )
    """, nativeQuery = true)
    fun plantProductItemelectronSel(@Param("companyId") companyId: String, @Param("password") password: String, @Param("systemDate") systemDate: LocalDate): List<OppoPlantProductItemelectronSel>
}
