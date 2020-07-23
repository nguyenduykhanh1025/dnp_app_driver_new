package vn.com.irtech.eport.logistic.form;

import java.util.List;

public class DriverInfo {

    private String driverName;

    private String driverPhoneNumber;

    private List<String> truckNoList;

    private List<String> chassisNoList;

    public String getDriverName() {
        return this.driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoneNumber() {
        return this.driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public List<String> getTruckNoList() {
        return this.truckNoList;
    }

    public void setTruckNoList(List<String> truckNoList) {
        this.truckNoList = truckNoList;
    }

    public List<String> getChassisNoList() {
        return this.chassisNoList;
    }

    public void setChassisNoList(List<String> chassisNoList) {
        this.chassisNoList = chassisNoList;
    }
    
}