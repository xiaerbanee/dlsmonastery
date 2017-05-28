package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.repository.ImeAllotRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImeAllotService {

    @Autowired
    private ImeAllotRepository imeAllotRepository;

    @Autowired
    private ProductImeRepository productImeRepository;



//    public void save(ImeAllot imeAllot) {
//        List<String> imeList = StringUtils.getSplitList(imeAllot.getImeStr(), "");
//        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
//        for(ProductIme productIme:productImeList) {
//            if(!productIme.getDepotId().equals(imeAllot.getToDepotId())) {
//                ImeAllot allot = new ImeAllot();
//                allot.setProductImeId(productIme.getId());
//                allot.setFromDepotId(productIme.getDepotId());
//                allot.setToDepotId(imeAllot.getToDepotId());
//                allot.setRemarks(imeAllot.getRemarks());
//                allot.setCrossArea(crossArea(imeAllot.getToDepotId(),productIme.getDepotId()));
//                imeAllotRepository.save(allot);
//                productIme.setDepotId(imeAllot.getToDepotId());
//                productImeRepository.save(productIme);
//            }
//        }
//    }

    public void logicDelete(String id) {
        imeAllotRepository.logicDelete(id);
    }

    public ImeAllot findOne(String id) {
        ImeAllot imeAllot = imeAllotRepository.findOne(id);
        return imeAllot;
    }


    public List<ImeAllot> findByProductImeId(String productImeId){
        return imeAllotRepository.findByProductImeId(productImeId);
    }


    private boolean crossArea(String toDepotId,String productImeDepotId){
        return true;
    }

}
