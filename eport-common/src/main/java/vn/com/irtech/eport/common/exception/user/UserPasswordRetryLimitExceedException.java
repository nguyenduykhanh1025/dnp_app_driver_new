package vn.com.irtech.eport.common.exception.user;

/**
 * 用户错误最大次数异常类
 * 
 * @author admin
 */
public class UserPasswordRetryLimitExceedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount)
    {
        super("user.password.retry.limit.exceed", new Object[] { retryLimitCount });
    }
}
