package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="crm_after_sale")
public class AfterSale extends CompanyEntity<AfterSale> {
    private String businessId;
    private String badProductImeId;
    private String badType;
    private String replaceProductImeId;
    private String toAreaProductImeId;
    private String packageStatus;
    private String memory;
    private String toStoreType;
    private String toStoreRemarks;
    private LocalDate toStoreDate;
    private LocalDate toCompanyDate;
    private String toCompanyRemarks;
    private LocalDate fromCompanyDate;
    private String fromCompanyProductId;
    private String areaDepotId;
    private Boolean fromAreaToFinance;
    private Boolean toAreaToFinance;
    private Boolean fromCompanyToFinance;
    private Boolean toCompanyToFinance;
    private String badDepotId;
    private LocalDateTime toFinanceDate;
    private Integer version = 0;
    private Depot depot;
    private String depotId;

    private ProductIme badProductIme;
    private ProductIme replaceProductIme;
    private ProductIme toAreaProductIme;
    private Depot badDepot;
    private Depot areaDepot;
    private Product fromCompanyProduct;
}
