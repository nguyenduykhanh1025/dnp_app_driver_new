package vn.com.irtech.eport.quartz.task;

import org.springframework.stereotype.Component;
import vn.com.irtech.eport.common.utils.StringUtils;

/**
 * 定时任务调度测试
 * 
 * @author admin
 */
@Component("ryTask")
public class RyTask
{
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("Execute multiple parameter methods: string type {}, boolean type {}, long integer {}, floating point {}, integer {}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("Implement the parameterized method:" + params);
    }

    public void ryNoParams()
    {
        System.out.println("Perform a parameterless method");
    }
}
