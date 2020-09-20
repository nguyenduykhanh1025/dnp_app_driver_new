/**
 * 
 */
package vn.com.irtech.eport.system.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {
   "issuer": "367",
   "reference": "31D80734-AB0E-4E1B-BE91-2C4DABD61489",
   "issue": "2020-09-19 14:08:15",
   "function": "32",
   "issueLocation": "34CE",
   "status": "1",
   "customsReference": [],
   "acceptance": [],
   "declarationOffice": "34CE",
   "Agent": {
      "name": "XN CANG TIEN SA",
      "identity": "0400101972",
      "status": "3"
   },
   "Importer": {
      "name": "XN CANG TIEN SA",
      "identity": "0400101972"
   },
   "PortDocument": {
      "name": "XN CANG TIEN SA",
      "identity": "VNDAN"
   },
   "TransportEquipments": [
      {
         "transportIdentity": "SITC MOJI",
         "numberOfJourney": "2025N",
         "arrivalDeparture": "2020-09-14 14:33:27",
         "container": "BEAU2872484",
         "content": [],
         "containerLocation": "XK-07-1-1",
         "commodityDescription": "NA",
         "statusOfGood": "1",
         "customsReference": "103529748450",
         "acceptance": "2020-09-14 09:16:03",
         "declarationOffice": "18A3",
         "natureOfTransaction": "A12",
         "declarationOfficeControl": "34CE",
         "timeExport": "2020-09-19 14:00:54",
         "channel": "2",
         "customsStatus": "TQ",
         "enterpriseIdentity": "2300372796",
         "enterpriseName": "CôNG TY Cổ PHầN  HANACANS",
         "seal": "CQ623843",
         "BillOfLadings": {
            "BillOfLading": {
               "reference": "SITGCKDAX02087",
               "cargoCtrlNo": "150920SITGCKDAX02087"
            }
         }
      }
   ]
}
 * @author GiapHD
 *
 */
@XmlRootElement(name = "Declaration")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomDeclareResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String issuer;
	private String issue; // "2020-09-19 14:08:15",
	private String issueLocation; // 34CE
	private String status; // 1
	
	@XmlElement(name="TransportEquipment")
	private List<TransportEquipment> transportEquipments;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssueLocation() {
		return issueLocation;
	}

	public void setIssueLocation(String issueLocation) {
		this.issueLocation = issueLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TransportEquipment> getTransportEquipments() {
		return transportEquipments;
	}

	public void setTransportEquipments(List<TransportEquipment> transportEquipments) {
		this.transportEquipments = transportEquipments;
	}
	
}

@XmlRootElement(name = "TransportEquipment")
@XmlAccessorType(XmlAccessType.FIELD)
class TransportEquipment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String transportIdentity; // Vesel name
	
	private String container;
	
	private String containerLocation;
	
	private String customsReference; // appNo
	
	private String customsStatus; // TQ
	
	private String enterpriseIdentity;
	
	private String enterpriseName;
	
	private String seal;

	public String getTransportIdentity() {
		return transportIdentity;
	}

	public void setTransportIdentity(String transportIdentity) {
		this.transportIdentity = transportIdentity;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getContainerLocation() {
		return containerLocation;
	}

	public void setContainerLocation(String containerLocation) {
		this.containerLocation = containerLocation;
	}

	public String getCustomsReference() {
		return customsReference;
	}

	public void setCustomsReference(String customsReference) {
		this.customsReference = customsReference;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getEnterpriseIdentity() {
		return enterpriseIdentity;
	}

	public void setEnterpriseIdentity(String enterpriseIdentity) {
		this.enterpriseIdentity = enterpriseIdentity;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getSeal() {
		return seal;
	}

	public void setSeal(String seal) {
		this.seal = seal;
	}
	
	
	/*
	 "transportIdentity": "SITC MOJI",
     "numberOfJourney": "2025N",
     "arrivalDeparture": "2020-09-14 14:33:27",
     "container": "UETU2810091",
     "content": [],
     "containerLocation": "XK-05-2-4",
     "commodityDescription": "NA",
     "statusOfGood": "1",
     "customsReference": "103529748450",
     "acceptance": "2020-09-14 09:16:03",
     "declarationOffice": "18A3",
     "natureOfTransaction": "A12",
     "declarationOfficeControl": "34CE",
     "timeExport": "2020-09-19 14:00:54",
     "channel": "2",
     "customsStatus": "TQ",
     "enterpriseIdentity": "2300372796",
     "enterpriseName": "CôNG TY Cổ PHầN  HANACANS",
     "seal": "CQ573421",
     "BillOfLadings": {
        "BillOfLading": {
           "reference": "SITGCKDAX02087",
           "cargoCtrlNo": "150920SITGCKDAX02087"
        }
     }
     */

}
