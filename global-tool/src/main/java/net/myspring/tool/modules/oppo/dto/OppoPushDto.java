package net.myspring.tool.modules.oppo.dto;

import net.myspring.tool.common.dto.CustomerDto;
import net.myspring.tool.modules.oppo.domain.*;

import java.util.List;

public class OppoPushDto {
    private String date;
    private List<CustomerDto> customerDtos;
    private List<OppoCustomerAllot> oppoCustomerAllots;
    private List<OppoCustomerStock> oppoCustomerStocks;
    private List<OppoCustomerImeiStock> oppoCustomerImeiStocks;
    private List<OppoCustomerSale> oppoCustomerSales;
    private  List<OppoCustomerSaleImei> oppoCustomerSaleImeis;
    private List<OppoCustomerSaleCount> oppoCustomerSaleCounts;
    private List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis;
    private List<OppoCustomerDemoPhone> oppoCustomerDemoPhones;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CustomerDto> getCustomerDtos() {
        return customerDtos;
    }

    public void setCustomerDtos(List<CustomerDto> customerDtos) {
        this.customerDtos = customerDtos;
    }

    public List<OppoCustomerAllot> getOppoCustomerAllots() {
        return oppoCustomerAllots;
    }

    public void setOppoCustomerAllots(List<OppoCustomerAllot> oppoCustomerAllots) {
        this.oppoCustomerAllots = oppoCustomerAllots;
    }

    public List<OppoCustomerStock> getOppoCustomerStocks() {
        return oppoCustomerStocks;
    }

    public void setOppoCustomerStocks(List<OppoCustomerStock> oppoCustomerStocks) {
        this.oppoCustomerStocks = oppoCustomerStocks;
    }

    public List<OppoCustomerImeiStock> getOppoCustomerImeiStocks() {
        return oppoCustomerImeiStocks;
    }

    public void setOppoCustomerImeiStocks(List<OppoCustomerImeiStock> oppoCustomerImeiStocks) {
        this.oppoCustomerImeiStocks = oppoCustomerImeiStocks;
    }

    public List<OppoCustomerSale> getOppoCustomerSales() {
        return oppoCustomerSales;
    }

    public void setOppoCustomerSales(List<OppoCustomerSale> oppoCustomerSales) {
        this.oppoCustomerSales = oppoCustomerSales;
    }

    public List<OppoCustomerSaleImei> getOppoCustomerSaleImeis() {
        return oppoCustomerSaleImeis;
    }

    public void setOppoCustomerSaleImeis(List<OppoCustomerSaleImei> oppoCustomerSaleImeis) {
        this.oppoCustomerSaleImeis = oppoCustomerSaleImeis;
    }

    public List<OppoCustomerSaleCount> getOppoCustomerSaleCounts() {
        return oppoCustomerSaleCounts;
    }

    public void setOppoCustomerSaleCounts(List<OppoCustomerSaleCount> oppoCustomerSaleCounts) {
        this.oppoCustomerSaleCounts = oppoCustomerSaleCounts;
    }

    public List<OppoCustomerAfterSaleImei> getOppoCustomerAfterSaleImeis() {
        return oppoCustomerAfterSaleImeis;
    }

    public void setOppoCustomerAfterSaleImeis(List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis) {
        this.oppoCustomerAfterSaleImeis = oppoCustomerAfterSaleImeis;
    }

    public List<OppoCustomerDemoPhone> getOppoCustomerDemoPhones() {
        return oppoCustomerDemoPhones;
    }

    public void setOppoCustomerDemoPhones(List<OppoCustomerDemoPhone> oppoCustomerDemoPhones) {
        this.oppoCustomerDemoPhones = oppoCustomerDemoPhones;
    }
}
