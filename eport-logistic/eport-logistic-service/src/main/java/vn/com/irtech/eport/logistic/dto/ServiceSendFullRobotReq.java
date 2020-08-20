package vn.com.irtech.eport.logistic.dto;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

public class ServiceSendFullRobotReq extends ServiceRobotReq {

	private static final long serialVersionUID = 1L;

	public ProcessOrder processOrder;
    public List<ShipmentDetail> containers;

    public ServiceSendFullRobotReq(ProcessOrder processOrder, List<ShipmentDetail> containers) {
        this.containers = containers;
        this.processOrder = processOrder;
    }

}