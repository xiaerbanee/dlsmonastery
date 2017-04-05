package net.myspring.cloud.common.dataSource;

public class DynamicDataSourceContext  {
    private String lookupKey;
    private static ThreadLocal<DynamicDataSourceContext> threadLocal = new ThreadLocal<DynamicDataSourceContext>();

    public static DynamicDataSourceContext get() {
        if (threadLocal.get() == null) {
            DynamicDataSourceContext threadLocalContext = new DynamicDataSourceContext();
            threadLocal.set(threadLocalContext);
        }
        return threadLocal.get();
    }

    public void remove() {
        this.lookupKey=null;
        threadLocal.remove();
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }
}
