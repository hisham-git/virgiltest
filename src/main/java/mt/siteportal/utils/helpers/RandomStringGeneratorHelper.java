package mt.siteportal.utils.helpers;
/**
 * Helper Class containing functions that generate some random data for a bunch of Data types such as Day, Hour, etc. 
 * 
 * @author Syed A. Zawad
 */
public class RandomStringGeneratorHelper {

	/*
	 * Array of Strings that contain all the values that could be generated for a particular kind of data
	 */
	private static final String[] MONTHS = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
			"SEP", "OCT", "NOV", "DEC" };
	private static final String[] YEARS = new String[] { "2016", "2017", "2018" };
	private static final String[] AM_PM = new String[] { "AM", "PM" };

	/**
	 * Generates a random number, uses it as an index for the given data and returns the value at that index
	 * 
	 * @param listOfWords String[] containing the possible values from which a random String can be chosen
	 * @return String - a random value from the input array
	 */
	private static String getRandomStringFromArray(String[] listOfWords) {
		int random_index = (int) Math.floor(Math.random() * listOfWords.length);
		if (random_index == listOfWords.length)
			random_index -= 1;
		return listOfWords[random_index];
	}

	/**
	 * Generates a random number between 1 to parameter : number inclusive and returns that number as String.
	 * Any number that is less than 10 has a "0" added to the front. E.g. 1 will return "01"
	 * 
	 * @param number int - the highest number that could be randomly generated
	 * @return String - the String version of the randomly generated number, with a "0" added if less than 10
	 */
	private static String getRandomStringFromNumber(int number) {
		int randomNumber = (int) Math.ceil(Math.random() * number);
		if (randomNumber / 10 <= 0) {
			return "0" + Integer.toString(randomNumber);
		}
		return Integer.toString(randomNumber);
	}

	/**
	 * Generates a random number between 0 to parameter : number-1 inclusive and returns that number as String.
	 * Any number that is less than 10 has a "0" added to the front. E.g. 1 will return "01"
	 * 
	 * @param number int - the highest number that can be randomly generated is one less than this number
	 * @return String - the String version of the randomly generated number, with a "0" added if less than 10
	 */
	private static String getRandomStringFromNumberStartingFromZero(int number) {
		int randomNumber = (int) Math.floor(Math.random() * number);
		if (randomNumber == number)
			randomNumber -= 1;
		if (randomNumber / 10 <= 0) {
			return "0" + Integer.toString(randomNumber);
		}
		return Integer.toString(randomNumber);
	}

	/**
	 * Generates a random Date, ranging from "01" to "28"
	 * @return String - A random String of the format "DD"
	 */
	public static String getRandomDate() {
		return getRandomStringFromNumber(28);
	}

	/**
	 * Generates a random Month, ranging from "JAN" to "DEC"
	 * @return String - A random String of the format "MMM"
	 */
	public static String getRandomMonth() {
		return getRandomStringFromArray(RandomStringGeneratorHelper.MONTHS);
	}
	
	/**
	 * Generates a random Year, ranging from "2016" to "2017"
	 * @return String - A random String of the format "YYYY"
	 */
	public static String getRandomYear() {
		return getRandomStringFromArray(RandomStringGeneratorHelper.YEARS);
	}
	
	/**
	 * Generates a random Hour, ranging from "01" to "12"
	 * @return String - A random String of the format "hh"
	 */
	public static String getRandomHour() {
		return getRandomStringFromNumber(12);
	}

	/**
	 * Generates a random Minute, ranging from "00" to "29"
	 * @return String - A random String of the format "mm"
	 */
	public static String getRandomMinute() {
		return getRandomStringFromNumberStartingFromZero(30);
	}

	/**
	 * Generates a random AM or PM String
	 * @return String - A random String of either "AM" or "PM"
	 */
	public static String getRandomAmOrPm() {
		return getRandomStringFromArray(RandomStringGeneratorHelper.AM_PM);
	}
}
