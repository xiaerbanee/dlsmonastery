package net.myspring.mybatis.exception;

/**
 * Created by liuj on 2017/3/18.
 */
public class MybatisException  extends RuntimeException {

    public MybatisException() {
        super();
    }

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(Throwable cause) {
        super(cause);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }
}
