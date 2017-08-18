package net.myspring.report.modules.future.service;

import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.repository.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepotService {

    @Autowired
    private DepotRepository depotRepository;

    public List<DepotDto> findByAccountId(String accountId){
        return depotRepository.findByAccountId(accountId);
    }
}
