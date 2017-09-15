package mt.siteportal.utils.helpers;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextHelper {
	 private static final Logger logger = LoggerFactory.getLogger(TextHelper.class);
	
	public static String splitParentheses(String str){
		logger.debug(String.format("Getting number between parentheses '%s'", str));
		str.trim();
		String count = str.split("[\\(\\)]")[1];
		return count;
	}
	
	public static String[] splitNewlines(String str){
		logger.debug(String.format("Splitting texts at newline characters '%s'", str));
		str.trim();
		return str.split("\\r?\\n");
	}
	public static String appendValuesWithParentheses(String value1, String value2){
		return value1+"("+value2+")";
	}
	
	public static List<String> getElementTextContent(List<WebElement> elements){
    	logger.debug("Getting text from each element in list "+ elements);
    	List<String> content = new ArrayList<String>();
		for(WebElement element: elements){
			content.add(element.getText().trim());
		}
		return content;
    }
	
	public static List<Integer> getElementIntContent(List<WebElement> elements){
    	logger.debug("Getting text from each element in list "+ elements);
    	List<Integer> content = new ArrayList<Integer>();
		for(WebElement element: elements){
			content.add(Integer.parseInt(element.getText().trim()));
		}
		return content;
    }
	
	public static List<Integer> convertToInt(List<String> stringList){
		List<Integer> intList = new ArrayList<Integer>();
		for(String text: stringList){
			intList.add(Integer.parseInt(text));
		}
		return intList;
	}
	
	public static List<String> getAtributeValues(List<WebElement> elements, String attributeName){
    	logger.debug("Getting text from each attribute in list "+ elements);
    	List<String> content = new ArrayList<String>();
		for(WebElement element: elements){
			content.add(UiHelper.getAttributeValue(element, attributeName));
		}
		return content;
    }
	
	public static String toCamelCase(String s) {
		System.out.println("Tocamel");
	    String result = "";
	    String[] tokens = s.split("[\\s\\-]"); // or whatever the divider is
	    for (int i = 0, L = tokens.length; i<L; i++) {
	        String token = tokens[i];
	        if (i==0) result = token.toLowerCase();
	        else
	            result += token.substring(0, 1).toUpperCase() +
	                token.substring(1, token.length()).toLowerCase();
	    }
	    return result;
	}
	
	/**
	 * Takes a String, ideally in the format {TypeOfStudy}: {Name} {(Type)}* and returns the three separate parts as an array
	 * This is used in the Study Dashboard Lists and Details Pages.
	 * @param text the Text to be Split
	 * @return String[] a 2 or 3 length array
	 */
	public static String[] getPartsFromDetails(String text){
		String [] parts = text.split("[ *:]{0,1} +");
		String middleText = "";
		if(parts.length>=3)
			parts[parts.length-1] = parts[parts.length-1].replaceAll("[\\(\\)]", "");
		if(parts.length>3){
			String [] middleTexts = Arrays.copyOfRange(parts, 1, parts.length-1);
			for(String s : middleTexts)
				middleText = middleText + " " + s;
		}else if(parts.length == 3){
			middleText = parts[1];
		}else{
			return parts;
		}
		return new String[]{parts[0], middleText.trim(), parts[parts.length-1]};
	}
	
	/**
	 * Splits a String of the format ____ : ____ (___) to get three values as an array of three or 2 Strings.
	 * @param text String - the String to split
	 * @return String []
	 */
	public static String[] getPartsFromHeader(String text){
		String [] splitted = text.split("(: )| \\(");
		splitted[splitted.length-1] = splitted[splitted.length-1].replaceAll("\\)","");
		for(int i = 0; i<splitted.length; i++)
			splitted[i] = splitted[i].trim();
		return splitted;
	}

	public static String[] splitSpaces(String text) {
		String[] parts = text.split("\\s+");
		return parts;
	}
	
	/**
	 * Converts the Duration String(hh:mm:ss) to a TimeStamp in second
	 * @param duration
	 * @return
	 */
	public static int getTimeStamp(String duration){
		String [] timeParts = duration.split(":");
		//TODO: Need further  clarification on video time display
		int timeStamp = //Integer.parseInt(timeParts[0])*3600 + 
				Integer.parseInt(timeParts[0])*60 + 
				Integer.parseInt(timeParts[1]);
		System.out.println(timeStamp);
		return timeStamp;	
	}

	/**
	 * Takes an input string of the format "Version X" and returns the X part
	 * after parsing it to an integer
	 * 
	 * @param version
	 *            - String - the string to parse
	 * @return - int - the version
	 */
	public static int getVersionNumber(String version) {
		String[] parts = splitSpaces(version);
		try {
			return Integer.parseInt(parts[1]);
		} catch (Exception e) {
			System.out.println("The String [" + version + "] could not be parsed");
			return 0;
		}
	}

}
