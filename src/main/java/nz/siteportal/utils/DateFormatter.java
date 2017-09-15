package nz.siteportal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper Class, Simply used to format the String Dates in queries to the Java
 * Date object
 * 
 * @author Syed A. Zawad
 *
 */
public class DateFormatter {

	private static SimpleDateFormat format;
	private static Date dateTime;

	public static Date toDate(String date, String format_of_date) {
		format = new SimpleDateFormat(format_of_date);
		try {
			dateTime = format.parse(date.trim());
			return dateTime;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date toDate(String date) {
		return toDate(date, "dd-MMM-yyyy hh:mm a");
	}

}
