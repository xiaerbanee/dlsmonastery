package net.myspring.cloud.common.utils;

import net.myspring.cloud.modules.sys.dto.AccountDto;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;


public class ThreadLocalContext {

    private AccountDto accountDto;
    private KingdeeBookDto kingdeeBookDto;
    private String dataSourceType;

    private static ThreadLocal<ThreadLocalContext> threadLocal = new ThreadLocal<ThreadLocalContext>();

    public static ThreadLocalContext get() {
        if (threadLocal.get() == null) {
            ThreadLocalContext threadLocalContext = new ThreadLocalContext();
            threadLocal.set(threadLocalContext);
        }
        return threadLocal.get();
    }

    public void remove() {
        this.accountDto = null;
        this.kingdeeBookDto = null;
        this.dataSourceType = "";
        threadLocal.remove();
    }

    public AccountDto getAccountDto() {
        return accountDto;
    }

    public void setAccountDto(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    public KingdeeBookDto getKingdeeBookDto() {
        return kingdeeBookDto;
    }

    public void setKingdeeBookDto(KingdeeBookDto kingdeeBookDto) {
        this.kingdeeBookDto = kingdeeBookDto;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }


}