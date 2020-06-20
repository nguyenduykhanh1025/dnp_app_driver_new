package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    /** ID tài xế */
    private Long driverId;

    /** truck_id */
    private Long truckId;

    private Truck truck;

    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setTruckId(Long truckId) 
    {
        this.truckId = truckId;
    }

    public Long getTruckId() 
    {
        return truckId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("driverId", getDriverId())
            .append("truckId", getTruckId())
            .toString();
    }

    public Truck getTruck() {
        if(truck == null){
            truck = new Truck();
        }
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}
