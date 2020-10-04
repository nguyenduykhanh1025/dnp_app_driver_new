package vn.com.irtech.api.dto;

import java.io.Serializable;

/**
 * @author admin
 *
 */
public class ContainerReqDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String containerNos;

	private String userVoy;

	public String getContainerNos() {
		return containerNos;
	}

	public void setContainerNos(String containerNos) {
		this.containerNos = containerNos;
	}

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

}
