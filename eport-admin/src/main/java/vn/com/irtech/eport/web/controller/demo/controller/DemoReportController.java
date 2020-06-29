package vn.com.irtech.eport.web.controller.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Report
*
* @author admin
*/
@Controller
@RequestMapping("/demo/report")
public class DemoReportController
{
    private String prefix = "demo/report";

    /**
    * Baidu ECharts
    */
    @GetMapping("/echarts")
    public String echarts()
    {
        return prefix + "/echarts";
    }

    /**
    * Chart plugin
    */
    @GetMapping("/peity")
    public String peity()
    {
        return prefix + "/peity";
    }

    /**
    * Line chart plugin
    */
    @GetMapping("/sparkline")
    public String sparkline()
    {
        return prefix + "/sparkline";
    }

    /**
    * Chart combination
    */
    @GetMapping("/metrics")
    public String metrics()
    {
        return prefix + "/metrics";
    }
}
