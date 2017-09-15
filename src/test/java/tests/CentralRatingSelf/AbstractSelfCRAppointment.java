package tests.CentralRatingSelf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.siteportal.pages.CentralRating.SelfCRAppointment;
import mt.siteportal.controls.DayPickerWidget;
import mt.siteportal.details.SubjectDetails;
import steps.Configuration.BeforeSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "CentralRatingSelf" })
public abstract class AbstractSelfCRAppointment extends AbstractTest {
	BeforeSteps beforeSteps = new BeforeSteps();
	SubjectDetails subjectD;
	SelfCRAppointment selfcr;
	DayPickerWidget datePicker;
	SelfCRAppointmentSteps crStep = new SelfCRAppointmentSteps();

	DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	Random rn = new Random();
	String study = "PharmCo - uTest_CR_Study_2";
	String site = "01 - MST Investigator";
	String subject = "uTest_Sub_1";
	String visitName = "Cr_Visit_2";

	int leadTime = 0;

	String username = "testmst01";
	String pass = "#3sftest";

	String Month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	String Date[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20", "21", "22", "23", "24", "25", "26", "27", "28" };

	protected int getRandomNumber() {
		return 0 + (int) (Math.random() * 30);
	}

	protected String getARandomMonth() {
		int m = 0 + (int) (Math.random() * 12);
		return Month[m % 12];
	}

	protected String getARandomDate() {
		return Date[getRandomNumber() % 28];
	}

	protected String[] getCurrentMonthDateDay() {
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		String dates[] = date.split("-");
		return dates;
	}

	protected Date getCurrentDate() {
		return new Date();

	}

	protected String getCurrentMonth() {
		String[] month = getCurrentMonthDateDay();
		return month[1];
	}

	protected String getCurrentYear() {
		String[] year = getCurrentMonthDateDay();
		return year[2];
	}

	protected String getCurrentDay() {
		String[] day = getCurrentMonthDateDay();
		return day[0];
	}

}
