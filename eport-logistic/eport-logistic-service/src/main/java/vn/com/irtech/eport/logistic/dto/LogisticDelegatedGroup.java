package vn.com.irtech.eport.logistic.dto;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.LogisticDelegated;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;

public class LogisticDelegatedGroup {
	
	private static final long serialVersionUID = 1L;
	
	private LogisticGroup logisticGroup;
	
	private List<LogisticDelegated> delegatedLogistics;

	public LogisticGroup getLogisticGroup() {
		return logisticGroup;
	}

	public void setLogisticGroup(LogisticGroup logisticGroup) {
		this.logisticGroup = logisticGroup;
	}

	public List<LogisticDelegated> getDelegatedLogistics() {
		return delegatedLogistics;
	}

	public void setDelegatedLogistics(List<LogisticDelegated> delegatedLogistics) {
		this.delegatedLogistics = delegatedLogistics;
	}
	
	
}
