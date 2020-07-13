package vn.com.irtech.eport.carrier.validation.constraint;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import vn.com.irtech.eport.carrier.validation.annotation.AcceptedValues;

public class AcceptedValuesValidator implements ConstraintValidator<AcceptedValues, String> {

	private List<String> valueList;

	@Override
	public void initialize(AcceptedValues constraintAnnotation) {
		valueList = new ArrayList<String>();
		for (String val : constraintAnnotation.values()) {
			valueList.add(val.toUpperCase());
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		return valueList.contains(value.toUpperCase());
	}

}
