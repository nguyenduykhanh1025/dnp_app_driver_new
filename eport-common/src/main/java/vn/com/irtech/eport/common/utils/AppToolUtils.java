package vn.com.irtech.eport.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AppToolUtils {
  public static String formatDateToString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);		
	}
	
	public static String formatDateString(String dateString, String oldFormat, String newFormat) {
		try {
			Date date = new SimpleDateFormat(oldFormat).parse(dateString);
			return formatDateToString(date, newFormat);
		} catch(Exception e) {
			return "fail!";
		}	
  }
  
  public static Date formatStringToDate(String date, String format) {
    try {
      return new SimpleDateFormat(format).parse(date);
    } catch(Exception e) {
      return new Date();
    } 
  }

  public static Long getDifferenceInDate(String firstDateStr, String format1, String secondDateStr, String format2) {
	try {
		SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
		Date firstDate = sdf1.parse(firstDateStr);
		Date secondDate = sdf2.parse(secondDateStr);
	 
		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MINUTES);
	} catch(Exception e) {
		return 1l;
	}
  }
}