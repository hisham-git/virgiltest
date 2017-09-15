package hu.siteportal.pdfreport;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

public class PdfLog {
	private static String actionMsg = null;

	private static List<String> expResultList,actResultList,statusList;
	private static ListMultimap<String, List<String>> pdfLogMap = LinkedListMultimap.create();

	private static String newLine = "\r\n </br>";

	public static void setStep(String message) {
		actionMsg = message;
		expResultList = new ArrayList<String>();
		actResultList = new ArrayList<String>();
		statusList = new ArrayList<String>();
	}

	public static void setExpected(String message) {
		expResultList.add(message);
	}

	public static void setActual(String message) {
		actResultList.add(message);
	}

	public static void setStepStatus(String message) {
		statusList.add(message);
		setStepLog();
	}
	
	public static void setStepLog(){
		pdfLogMap.put(actionMsg, expResultList);
		pdfLogMap.put(actionMsg, actResultList);
		pdfLogMap.put(actionMsg, statusList);
	}
	
	public static ListMultimap<String, List<String>> getStepLog(){
		return pdfLogMap;
	}

	public static List<String> getExpectedResultList() {
		return expResultList;
	}

	public static List<String> getActualResultList() {
		return actResultList;
	}

	public static List<String> getStatusList() {
		return statusList;
	}

	public static void setDriver(WebDriver driver) {
		PdfLogging.setDriver(driver);
	}

	public static void clearActionResultStatusList() {
		expResultList.clear();
		actResultList.clear();
		statusList.clear();
		pdfLogMap.clear();
	}
}