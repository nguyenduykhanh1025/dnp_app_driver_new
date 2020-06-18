package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * driver_truck Object driver_truck
 * 
 * @author admin
 * @date 2020-06-18
 */
public class DriverTruck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID tài xế */
    @Excel(name = "ID tài xế")
    private Long driverId;

    /** truck_id */
    @Excel(name = "truck_id")
    private String truckId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setTruckId(String truckId) 
    {
        this.truckId = truckId;
    }

    public String getTruckId() 
    {
        return truckId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("driverId", getDriverId())
            .append("truckId", getTruckId())
            .toString();
    }
}
