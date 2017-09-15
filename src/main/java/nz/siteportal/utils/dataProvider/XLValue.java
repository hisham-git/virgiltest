package nz.siteportal.utils.dataProvider;

public class XLValue<T> {
	T value;
	
	XLValue(T v) {
		value = v;
	}
	
	T getValue() {
		if (null != value)
			return value;
		return null;
	}
	
	String showType() {
		return "Type of T is " + value.getClass().getName();
	}

}
