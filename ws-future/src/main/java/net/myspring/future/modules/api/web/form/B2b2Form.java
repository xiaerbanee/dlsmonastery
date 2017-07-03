package net.myspring.future.modules.api.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.List;

public class B2b2Form {
    private String username;
    private String password;
    private String yzm;
    private String shipDate;
    private LocalDate shipDateStart;
    private LocalDate shipDateEnd;
    private String codeStr;
    private String orderIdStr;
    private List<String> codeList=Lists.newArrayList();
    private List<String> orderIdList=Lists.newArrayList();

    public String getCodeStr() {
        return codeStr;
    }

    public void setCodeStr(String codeStr) {
        this.codeStr = codeStr;
    }

    public String getOrderIdStr() {
        return orderIdStr;
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public List<String> getOrderIdList() {
        if(StringUtils.isNotBlank(orderIdStr)){
            this.orderIdList= StringUtils.getSplitList(HtmlUtils.htmlUnescape(orderIdStr), CharConstant.ENTER);
        }
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }

    public List<String> getCodeList() {
        if(StringUtils.isNotBlank(codeStr)){
            this.orderIdList= StringUtils.getSplitList(HtmlUtils.htmlUnescape(codeStr), CharConstant.ENTER);
        }
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public LocalDate getShipDateStart() {
        if(StringUtils.isNotBlank(shipDate)) {
            return LocalDateUtils.parse(shipDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        } else {
            return LocalDate.now().minusDays(7);
        }
    }

    public void setShipDateStart(LocalDate shipDateStart) {
        this.shipDateStart = shipDateStart;
    }

    public LocalDate getShipDateEnd() {
        if(StringUtils.isNotBlank(shipDate)) {
            return LocalDateUtils.parse(shipDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        } else {
            return LocalDate.now().plusDays(1);
        }
    }

    public void setShipDateEnd(LocalDate shipDateEnd) {
        this.shipDateEnd = shipDateEnd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    public boolean isOppo(){
        if(StringUtils.isNotBlank(username)&& OppoUsernameEnum.getList().contains(username)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isVivo(){
        if(StringUtils.isNotBlank(username)&& VivoUsernameEnum.getList().contains(username)){
            return true;
        }else {
            return false;
        }
    }

    public enum OppoUsernameEnum{
        ncop,NCLZ;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
            if(CollectionUtil.isEmpty(list)){
                for(OppoUsernameEnum oppoUsernameEnum: OppoUsernameEnum.values()){
                    list.add(oppoUsernameEnum.name());
                }
            }
            return list;
        }
    }

    public enum VivoUsernameEnum{
        ksdz,NCHX;

        private static List<String> list= Lists.newArrayList();

        public static List<String> getList(){
            if(CollectionUtil.isEmpty(list)){
                for(VivoUsernameEnum vivoUsernameEnum: VivoUsernameEnum.values()){
                    list.add(vivoUsernameEnum.name());
                }
            }
            return list;
        }
    }
}
