package vn.com.irtech.eport.common.exception.file;

import vn.com.irtech.eport.common.exception.base.BaseException;

/**
 * File information exception class
 * 
 * @author admin
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
