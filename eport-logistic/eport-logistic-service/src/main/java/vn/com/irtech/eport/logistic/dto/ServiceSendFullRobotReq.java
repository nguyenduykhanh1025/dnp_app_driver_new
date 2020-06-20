package vn.com.irtech.eport.logistic.dto;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.QueueOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

public class ServiceSendFullRobotReq extends ServiceRobotReq {

    public QueueOrder shipment;
    public List<ShipmentDetail> containers;

    public ServiceSendFullRobotReq(QueueOrder shipment, List<ShipmentDetail> containers) {
        this.containers = containers;
        this.shipment = shipment;
    }

}