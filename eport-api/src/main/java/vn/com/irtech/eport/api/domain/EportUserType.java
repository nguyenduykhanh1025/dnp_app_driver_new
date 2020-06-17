package vn.com.irtech.eport.api.domain;

public enum EportUserType {
	ADMIN("admin"), LOGISTIC("logistic"), TRANSPORT("transport");

	private final String value;

	EportUserType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static EportUserType fromString(String text) {
		for (EportUserType e : EportUserType.values()) {
			if (e.value().equalsIgnoreCase(text)) {
				return e;
			}
		}
		return null;
	}
}
