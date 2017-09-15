package mt.siteportal.utils.tools;

import mt.siteportal.utils.data.Constants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Contains auxiliary/utility methods.
 * TODO: generalize string-date/ date-string converters (extract common, support locales etc).
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	/**
	 * Gets current date/time based timestamp formatted with the specified dateFormat.
	 * @param dateFormat - date format to use.
	 * @return current date/time based timestamp formatted with the specified dateFormat.
	 */
	public static String getTimeStamp(DateFormat dateFormat){
		return dateFormat.format(new Date());
	}
	
	/**
	 * Gets current date/time based timestamp formatted with the specified dateFormat string.
	 * @param dateFormatString - date format string to use.
	 * @return current date/time based timestamp formatted with the specified dateFormat string.
	 */
	public static String getTimeStamp(String dateFormatString){
		
		return getTimeStamp(new SimpleDateFormat(dateFormatString));
	}	
	
	/**
	 * Gets current date/time based timestamp formatted with the Log date formatter.
	 * @return current date/time based timestamp formatted with the Log date formatter.
	 */
	public static String getTimeStampForLog(){		
		return getTimeStamp(Constants.DATE_FORMAT_LOG);
	}
	
	/**
	 * Gets current date/time based timestamp formatted with the FileName date formatter.
	 * @return current date/time based timestamp formatted with the FileName date formatter.
	 */
	public static String getTimeStampForFileName(){		
		return getTimeStamp(Constants.DATE_FORMAT_FILE);
	}

    /**
     * Gets current date based timestamp formatted with the FormDate date formatter (as used in forms' date fields).
     * @return current date based timestamp formatted with the FormDate date formatter.
     */
    public static String getCurrentDateForFormDateFieldAsString(){
        return getTimeStamp(Constants.DATE_FORMAT_FORM_DATES);
    }

    /**
     * Converts the date specified into a string using FormDate date formatter (as used in forms' date fields).
     * @return date-as-string using FormDate date formatter.
     */
    public static String convertFormDateToString(Date date){
        return Constants.DATE_FORMAT_FORM_DATES.format(date);
    }

    /**
     * Converts date specified as a string to Date object.
     * Note:
     * - the string is supposed to be in 'yyyy-MM-dd' format;
     * - default locale is used.
     *
     * @param dateAsString - input date-string.
     *
     * @return Date object which corresponds to the input string.
     */
    public static Date convertToFormDate(String dateAsString){
        return convertToDate(dateAsString, Constants.DATE_FORMAT_FORM_DATES);
    }

    /**
     * Converts date specified as a string to Date object.
     * Note:<br/>
     * To parse dates like "Feb 15, 2003" you need to use a formatter with English locale:<br/>
     *   new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
     *
     * @param dateAsString - input date-string.
     * @param formatter - date formatter to use.
     *
     * @return Date object which corresponds to the input string.
     */
    public static Date convertToDate(String dateAsString, DateFormat formatter){
        try {
            return formatter.parse(dateAsString);
        } catch (ParseException e) {
            logger.error(String.format("Cannot parse date(%s) using formater(%s)."), dateAsString, formatter);
        }
        return null;
    }

    /**
     * Generates a string value based on current timestamp and random digits.
     *
     * @param maxLength - maximum length of the string.
     *
     * @return a string value based on current timestamp and random digits.
     */
    public static String getSomeValue(int maxLength){
        Random random = new Random();

        int randomPrefix= random.nextInt(9990);
        String timestamp = getTimeStampForFileName();
        int randomSuffix = random.nextInt(5555);
        double randomTail= maxLength * random.nextDouble();

        String result = String.format("%s_%s_%s ____ %s",randomPrefix,timestamp,randomSuffix, randomTail);

        if (result.length() > maxLength){
            return result.substring(0, maxLength - 1);
        }

        return result;
    }

    /**
     * Generates a string value based on current timestamp and random digits.
     *
     * @return a string value based on current timestamp and random digits.
     */
    public static String getSomeValue(){
        return getSomeValue(721);
    }

    /**
     * Generates a short string value based on current timestamp and random digits.
     *
     * @return a short string value based on current timestamp and random digits.
     */
    public static String getSomeShortValue(){
        return getSomeValue(53);
    }

    /**
     * Generates a very short string value based on current timestamp and random digits.
     *
     * @return a very short string value based on current timestamp and random digits.
     */
    public static String getSomeVeryShortValue(){
        return getSomeValue(23);
    }
    
    /**
     * Read information from excel file
     *
     * @return HashMap<String, Locator>
     */
    public static HashMap<String, String> readFromXLS(String sheetName) {
		HashMap<String, String> locators = new HashMap<String, String>();
		try {
			FileInputStream file = new FileInputStream(new File(
					"src\\main\\resources\\Locators.xlsx"));

			// Create Workbook instance holding reference to .xlsx file

			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String name = row.getCell(0).getStringCellValue();
				String locator = row.getCell(2).getStringCellValue();
					
				locators.put(name, locator);
				}
			
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return locators;
	}
    
    /**
     * 
     */
}