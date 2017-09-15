package steps.Tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import hu.siteportal.pages.CentralRating.CrAppointmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.pages.CrVisitList;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Required;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

public class CentralRatingSteps extends AbstractStep {

	public CentralRatingSteps() {
		crVisitList = PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
		crAppDetails = PageFactory.initElements(Browser.getDriver(), CrAppointmentDetails.class);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
	}

	public SubjectDetails backToSubject() {
		if (crAppDetails.detailsIsOpened()) {
			Log.logStep("Navigaing from CR Appointment detils to Subject detils...");
			toolbarFull.clickSecondLinkFromBreadcrumbs();
			return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		}
		Log.logWarning("Appointment details page is not opened...");
		return null;
	}

	public void appointmentDetailsVerification(String subject) {
		Log.logVerify("Verifying Apppointment details page is opened for Subject: [" + subject + "]");
		Verify.True(crAppDetails.detailsIsOpened(subject), "Details page found not correct");
	}
	
	public void appointmentDetailsVerification() {
		Log.logVerify("Verifying Apppointment details page is opened");
		Verify.True(crAppDetails.detailsIsOpened(), "Details page found not opened");
	}

	public void verifyAppointmentDetailsFields(String study, String site, String subjectName, String visitName) {
		HardVerify.EqualsIgnoreCase(crAppDetails.getAppointmentInfoForItem("Study").get("Study"), study,
				"Verifying Study field contains: [" + study + "]", "[PASSED]",
				"Study field contains: [" + study + "]. [FAILED]");
		HardVerify.EqualsIgnoreCase(crAppDetails.getAppointmentInfoForItem("Site").get("Site"), site,
				"Verifying Site field contains: [" + site + "]", "[PASSED]",
				"Study field contains: [" + site + "]. [FAILED]");
		HardVerify.EqualsIgnoreCase(crAppDetails.getAppointmentInfoForItem("Subject").get("Subject"), subjectName,
				"Verifying Subject field contains: [" + subjectName + "]", "[PASSED]",
				"Study field contains: [" + subjectName + "]. [FAILED]");
		HardVerify.EqualsIgnoreCase(crAppDetails.getAppointmentInfoForItem("Visit").get("Visit"), visitName,
				"Verifying Visit field contains: [" + visitName + "]", "[PASSED]",
				"Study field contains: [" + visitName + "]. [FAILED]");
	}

	private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	private static final DateFormat timeFormat = new SimpleDateFormat("hh-mm-a");

	public void setDate(String type, Date dateTime) {
		String formattedDate = dateFormat.format(dateTime);
		String[] splitDate = (formattedDate.split("-"));
		String date = splitDate[0];
		String month = splitDate[1].toUpperCase();
		String year = splitDate[2];

		Log.logStep("Setting " + type + " Date: [" + date + "-" + month + "-" + year + "]");
		switch (type) {
		case "Notification":
			crAppDetails.setNotificationDate(date, month, year);
			break;

		case "Schedule":
			crAppDetails.setScheduleDate(date, month, year);
			break;

		default:
			Log.logWarning("Datepicker type doesn't match. Should be: [Notification/Schedule]");
			break;
		}
	}
	
	public void setTime(String type, String hour, String min, String amPm) {
		Log.logStep("Setting " + type + " Time: [" + hour + ":" + min + " " + amPm + "]");
		switch (type) {
		case "Notification":
			crAppDetails.setNotificationTime(hour, min, amPm);
			break;

		case "Schedule":
			crAppDetails.setScheduleTime(hour, min, amPm);
			break;

		default:
			Log.logWarning("Timepicker type doesn't match. Should be: [Notification/Schedule]");
			break;
		}
	}
	
	/*private String hour, min, amPm;

	public void setTime(String type, Date dateTime) {
		String formattedTime = timeFormat.format(dateTime);
		String[] splitTime = (formattedTime.split("-"));
		hour = splitTime[0];
		min = splitTime[1];
		amPm = splitTime[2];
		switch (type) {
		case "Notification":
			Log.logStep("Setting " + type + " Time: [" + hour + ":" + min + " " + amPm + "]");
			crAppDetails.setNotificationTime(hour, min, amPm);
			break;

		case "Schedule":
			if (amPm.equalsIgnoreCase("am")) {
				int currentHour = Integer.parseInt(hour);
				if (currentHour >= 1 && currentHour < 5) {
					hour = String.valueOf(currentHour + 7);
				} else if (currentHour >= 6 && currentHour < 12) {
					hour = String.valueOf(currentHour - 5);
				} else {
					hour = String.valueOf(currentHour - 5);
				}
			}
			if (amPm.equalsIgnoreCase("pm")) {
				int currentHour = Integer.parseInt(hour);
				if (currentHour >= 9 && currentHour < 12) {
					hour = String.valueOf(currentHour - 3);
				}
			}

			int currentMin = Integer.parseInt(min);
			if (currentMin > 0 && currentMin < 15) {
				min = "0";
			} else if (currentMin > 15 && currentMin < 30) {
				min = "15";
			} else if (currentMin > 30 && currentMin < 45) {
				min = "30";
			} else if (currentMin > 45) {
				min = "45";
			}
			Log.logStep("Setting " + type + " Time: [" + hour + ":" + min + " " + amPm + "]");
			crAppDetails.setScheduleTime(hour, min, amPm);
			break;

		default:
			Log.logError("Timepicker type doesn't match");
			break;
		}
	}*/

	public void verifyDate(String type, Date dateTime) {
		String dateFound;
		String dateExpected = dateFormat.format(dateTime);
		switch (type) {
		case "Notification":
			dateFound = crAppDetails.getNotificationDateTime().split(" ", 2)[0];
			HardVerify.EqualsIgnoreCase(dateExpected, dateFound, "Verifying " + type + " Date: [" + dateExpected + "]",
					"[PASSED]", "Found " + type + " Date: [" + dateFound + "] [FAILED]");
			break;

		case "Schedule":
			dateFound = crAppDetails.getScheduleDateTime().split(" ", 2)[0];
			HardVerify.EqualsIgnoreCase(dateExpected, dateFound, "Verifying " + type + " Date: [" + dateExpected + "]",
					"[PASSED]", "Found " + type + " Date: [" + dateFound + "] [FAILED]");
			break;

		default:
			Log.logWarning("Datepicker type doesn't match. Should be: [Notification/Schedule]");
			break;
		}
	}

	public void verifyTime(String type, String hour, String min, String amPm) {
		String timeFound;
		String schTimeExpected = new StringBuilder().append(hour).append(":").append(min).append(" ").append(amPm)
				.toString();
		String ntfTimeExpected = new StringBuilder().append(hour).append(":").toString();
		switch (type) {
		case "Notification":
			timeFound = crAppDetails.getNotificationDateTime().split(" ", 2)[1];
			HardVerify.True(timeFound.contains(ntfTimeExpected),
					"Verifying " + type + " Time: [" + ntfTimeExpected + "]", "[PASSED]",
					"Found " + type + " Time: [" + timeFound + "] [FAILED]");
			break;

		case "Schedule":
			timeFound = crAppDetails.getScheduleDateTime().split(" ", 2)[1];
			HardVerify.Equals(schTimeExpected, timeFound, "Verifying " + type + " Time: [" + schTimeExpected + "]",
					"[PASSED]", "Found " + type + " Time: [" + timeFound + "] [FAILED]");
			break;

		default:
			Log.logWarning("Timepicker type doesn't match. Should be: [Notification/Schedule]");
			break;
		}
	}

	public void verifySaveAppointmentIsEnabled(boolean isEnabled) {
		if (isEnabled) {
			HardVerify.True(UiHelper.isEnabled(crAppDetails.getSaveButton()),
					"Verifying 'Save Appointment' button is enabled...", "[PASSED]",
					"'Save Appointment' button found disabled. [FAILED]");
		} else {
			HardVerify.False(UiHelper.isEnabled(crAppDetails.getSaveButton()),
					"Verifying 'Save Appointment' button is disabled...", "[PASSED]",
					"'Save Appointment' button found enabled. [FAILED]");
		}
	}

	public void clickPickClinicianLink() {
		Log.logStep("Picking a clinician for scheduling...");
		crAppDetails.pickClinician();
	}
	
	public void verifyClinicianCalenderIsPresent(boolean isPresent) {
		if (isPresent) {
			HardVerify.True(UiHelper.isVisible(crAppDetails.getClinicianCalender()),
					"Verifying Clinician Calender is displayed on appointment screen", "[PASSED]",
					"Clinician Calender is not displayed on appointment screen. [FAILED]");
		} else {
			HardVerify.False(UiHelper.isVisible(crAppDetails.getClinicianCalender()),
					"Verifying Clinician Calender is not displayed on appointment screen", "[PASSED]",
					"Clinician Calender is displayed on appointment screen. [FAILED]");
		}
	}
	
	public void verifyPresenceOfClinicians(boolean isPresent, List<String> clinician) {
		if (isPresent) {
			HardVerify.True(crAppDetails.getClinicianNames().containsAll(clinician),
					"Verifying Qualified clinicians with calendar are shown...", "[PASSED]",
					"Expected Clinician names not matching with calender. [FAILED]");
		} else {
			HardVerify.False(crAppDetails.getClinicianNames().containsAll(clinician),
					"Verifying Qualified clinicians with calendar are not shown...", "[PASSED]",
					"Expected Clinician names matching with calender. [FAILED]");

		}
	}
	
	public boolean selectClinicianTimeSlot(String clinician, String type, String hour, String min, String amPm) {
		List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(clinician, type);
		if (null != slotsFound) {
			String time = new StringBuilder().append(hour).append(":").append(min).append(amPm.toLowerCase())
					.toString();
			for (WebElement slot : slotsFound) {
				if (slot.getText().equalsIgnoreCase(time)) {
					Log.logStep(
							"Clicking " + type + " time slot at: [" + time + "] for clinician: [" + clinician + "]");
					UiHelper.click(slot);
					return true;
				}
			}
			Log.logWarning("No " + type + " time slot at: [" + time + "] found for clinician: [" + clinician + "]");
			return false;
		}
		Log.logWarning("No " + type + " time slot found for clinician: [" + clinician + "]");
		return false;
	}
	
	public Date getAvailableScheduleDateTime(Date currentDateTime, String clinician, int day) {
		Log.logStep("Searching first available time slot after: "+day+" day(s) for clinician: [" + clinician + "]...");
		
		int hourFound;
		Date futureDateTime = null;
		List<WebElement> slotsFound = null;
		String hour = "07", min = "00", amPm = "AM", amPmFound;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDateTime);
		cal.add(Calendar.DATE, day);
		futureDateTime = cal.getTime();

		while (null == slotsFound) {
			setDate("Schedule", futureDateTime);
			setTime("Schedule", hour, min, amPm);
			clickPickClinicianLink();
			do {
				UiHelper.fluentWaitForElementVisibility(crAppDetails.getClinicianCalender(), 5);
				slotsFound = crAppDetails.getClinicianTimeSlots(clinician, "Available");
				if (null != slotsFound) {
					for (WebElement slot : slotsFound) {
						String time = slot.getText();
						Log.logStep("Selecting first available time slot: [" + time + "] for clinician: [" + clinician
								+ "]");
						String[] hrMin = time.split("(:|\\D)");
						hour = hrMin[0];
						min = hrMin[1];
						amPm = time.split("([^a-z])")[5];
						selectClinicianTimeSlot(clinician, "Available", hour, min, amPm);
						return getCurrentScheduleDateTime();
					}
				}
				crAppDetails.changeScheduleTime("Minute", "Increase");
				String timeFound = crAppDetails.getScheduleDateTime().split(" ", 2)[1];
				String[] splitTime = timeFound.split("(:| )");
				hourFound = Integer.parseInt(splitTime[0]);
				amPmFound = splitTime[2];
			} while (amPmFound.equalsIgnoreCase("AM"));
			Log.logStep("No available slot found for date: [" + futureDateTime + "]");
			cal.setTime(futureDateTime);
			cal.add(Calendar.DATE, 1);
			futureDateTime = cal.getTime();
			Log.logStep("Date after increasing 1 day: [" + formatter.format(futureDateTime) + "]");
		}
		return null;
	}
	
	public void deSelectClinicianTimeSlot(String clinician, String type, Date scheduleDate) {
		String formattedTime = timeFormat.format(scheduleDate);
		String[] splitTime = (formattedTime.split("-"));
		String hour = splitTime[0];
		String min = splitTime[1];
		String amPm = splitTime[2];
		Log.logStep(
				"Deselecting " + type + " time slot at: [" + formattedTime + "] for clinician: [" + clinician + "]");
		selectClinicianTimeSlot(clinician, type, hour, min, amPm);
	}

	public void setSubjectVisitComment(String comment) {
		Log.logStep("Entering Subject Visit Comment: [" + comment + "] for appointment...");
		crAppDetails.setSubjectVisitComments(comment);
	}

	public void verifyCommentIsEntered(String comment) {
		String commentFound = crAppDetails.getSubjectVisitComments();
		Verify.True(comment.equalsIgnoreCase(commentFound),
				"Verfying comment properly entered on Subject Visit Comments field...", "[Passed]",
				"Expected: [" + comment + "] but found: [" + commentFound + "] [FAILED]");
	}

	public void saveAppointment(boolean shouldSave) {
		if (shouldSave) {
			Log.logStep("Clicking save button on Appointment details...");
			crAppDetails.saveChanges();
		}
	}
	
	public ConfirmPopUp appointmentAction(String type) {
		ConfirmPopUp confirmPopUp = null;
		Log.logStep("Clicking '" + type + " Appointment' button...");

		switch (type) {
		case "Schedule":
			UiHelper.click(crAppDetails.getScheduleAppointmentButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
			break;

		case "Reschedule":
			UiHelper.click(crAppDetails.getScheduleAppointmentButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#informationDialog.modalshow")));
			break;
			
		case "Update":
			UiHelper.click(crAppDetails.getUpdateAppointmentButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#informationDialog.modalshow")));
			break;

		case "Initiate":
			UiHelper.click(crAppDetails.getInitiateButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
			break;
			
		case "Recall":
			UiHelper.click(crAppDetails.getRecallButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
			break;
			
		case "Cancel":
			crAppDetails.getAliasInput().clear();
			crAppDetails.getSubjectVisitCommentsTextarea().clear();
			UiHelper.click(crAppDetails.getCancelAppointmentButton());
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#informationDialog.modalshow")));
			break;

		default:
			Log.logWarning("Action type doesn't match. Should be: [Schedule/Reschedule/Update/Initiate/Recall/Cancel].");
			Log.logInfo("Returning null...");
			break;
		}
		return confirmPopUp;
	}
	
	public void scheduleAndVerifyConfirmMessage() {
		String date = crAppDetails.getScheduleDateTime().split(" ", 2)[0];
		String time = crAppDetails.getScheduleDateTime().split(" ", 2)[1];
		String timeZone = crAppDetails.getScheduleTimeZone();
		
		final String msgExp = "Are you sure you want to schedule this assessment for " + date + " at " + time + " " + timeZone
				+ "?";
		ConfirmPopUp popUp = appointmentAction("Schedule");
		HardVerify.True(popUp.isOpen(), "Verifying schedule confirmation dialog-box is displyed...", "[PASSED]",
				"Confirmation dialog-box is not displayed. [Failed]");
		HardVerify.EqualsIgnoreCase(popUp.getConfirmationText(), msgExp,
				"Verifying schedule confirmation message: [" + msgExp + "] is displyed...", "[PASEED]",
				"schedule confirmation message found: [" + popUp.getConfirmationText() + "] [FAILED]");
		Log.logStep("Clicking 'Yes' on confirm dialog...");
		popUp.clickYesButton();
	}
	
	public void reScheduleAndVerifyConfirmMessage(boolean reSchedule) {
		String date = crAppDetails.getScheduleDateTime().split(" ", 2)[0];
		String time = crAppDetails.getScheduleDateTime().split(" ", 2)[1];
		String timeZone = crAppDetails.getScheduleTimeZone();
		
		final String msgExp = "You have selected to move this appointment to a different date/time: " + date + " at " + time
				+ " " + timeZone + ". Are you sure you want to continue?";
		ConfirmPopUp popUp = appointmentAction("Reschedule");
		HardVerify.True(popUp.isOpen(), "Verifying reschedule confirmation dialog-box is displyed...", "[PASSED]",
				"Confirmation dialog-box is not displayed. [FAILED]");
		HardVerify.EqualsIgnoreCase(popUp.getConfirmationText(), msgExp,
				"Verifying Reschedule confirmation message: [" + msgExp + "] is displyed...", "[PASEED]",
				"Reschedule confirmation message found: [" + popUp.getConfirmationText() + "] [FAILED]");
		HardVerify.True(Required.isDropdownRequired(popUp.getReasonDropdown()), "Verifying 'Reason dropdown' is required",
				"[PASSED]", "'Reason dropdown' is not required [FAILED]");
		if (reSchedule) {
			Log.logStep("Selecting reason 'Scheduling Adjustment' from dropdown...");
			popUp.inputPredefinedReason("Scheduling Adjustment");
			Log.logStep("Clicking 'Yes' on confirm dialog...");
			popUp.clickYesButton();
		} else {
			Log.logStep("Clicking 'No' on confirm dialog...");
			popUp.clickNoButton();
		}
	}

	public void verifyVisitStatusInAppointmentDetails(String status) {
		HardVerify.EqualsIgnoreCase(crAppDetails.getVisitStatusText(), status,
				"Verifying Visit Status: [" + status + "] in appointment details", "[PASSED]",
				" Visit Status found: [" + crAppDetails.getVisitStatusText() + "] in appointment details. [FAILED]");
	}

	public ConfirmPopUp scheduleAppointmentAt(String type, String hr, String min, String amOrPm) {
		List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(type);
		if (null != slotsFound) {
			boolean foundTime = false;
			String time = new StringBuilder().append(hr).append(":").append(min).append(amOrPm.toLowerCase())
					.toString();
			for (WebElement slot : slotsFound) {
				if (slot.getText().equalsIgnoreCase(time)) {
					foundTime = true;
					UiHelper.click(slot);
					break;
				}
			}
			if (foundTime) {
				Log.logStep("Schduling appointment at: [" + time + "]");
				UiHelper.click(crAppDetails.getScheduleAppointmentButton());
				return new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
			} else {
				Log.logWarning("No slot is available for time: [" + time + "]");
			}
		}
		return null;
	}

	public void scheduleAppointmentRandomly(String type) {
		List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(type);
		if (null != slotsFound)
			UiHelper.click(crAppDetails.getScheduleAppointmentButton());
	}
	
	public void verifyClinicianDisplayedAsSelected(boolean isSelected, String clinician) {
		if (isSelected) {
			HardVerify.True(crAppDetails.clinicianIsSelected(clinician),
					"Verifying clinician: [" + clinician + "] is displayed as selected...", "[PASSED]",
					"Clinician: [" + clinician + "] is not displayed as selected. [FAILED]");
		} else {
			HardVerify.False(crAppDetails.clinicianIsSelected(clinician),
					"Verifying clinician: [" + clinician + "] is not displayed as selected...", "[PASSED]",
					"Clinician: [" + clinician + "] is displayed as selected. [FAILED]");
		}
	}
	
	public void verifyInitiateAppointmentIsVisible(boolean isVisible) {
		if (isVisible) {
			HardVerify.NotNull(crAppDetails.getInitiateButton(),
					"Verifying 'Initiate Appointment' button is visible in CR Appointment screen...", "[PASSED]",
					"'Initiate Appointment' button found not visible. [FAILED]");
		} else {
			HardVerify.Null(crAppDetails.getInitiateButton(),
					"Verifying 'Initiate Appointment' button is not visible in CR Appointment screen...", "[PASSED]",
					"'Initiate Appointment' button found visible. [FAILED]");
		}
	}
	
	public void verifyInitiateAppointmentIsEnabled(boolean isEnabled) {
		if (isEnabled) {
			HardVerify.True(UiHelper.isEnabled(crAppDetails.getInitiateButton()),
					"Verifying 'Initiate Appointment' button is enabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Initiate Appointment' button found disabled for clicking. [FAILED]");
		} else {
			HardVerify.False(UiHelper.isEnabled(crAppDetails.getInitiateButton()),
					"Verifying 'Initiate Appointment' button is disabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Initiate Appointment' button found enabled for clicking. [FAILED]");
		}
	}
	
	public void requiredFieldsValidation(boolean isRequired) {
		if (isRequired) {
			HardVerify.True(Required.isRequired(crAppDetails.getAliasInput()), "Verifying 'Alias' input is required",
					"[PASSED]", "'Alias' input found not required. [FAILED]");
		} else {
			HardVerify.True(Required.isRequired(crAppDetails.getAliasInput()), "Verifying 'Alias' input is not required",
					"[PASSED]", "'Alias' input found required. [FAILED]");
		}
	}
	
	public void setSubjectAlias(String alias) {
		Log.logStep("Filling Subject 'Alias' with value: [" + alias + "]");
		crAppDetails.setAliasInput(alias);
	}
	
	public void initiateAppointment() {
		Log.logStep("Initiating Appointment...");
		UiHelper.click(crAppDetails.getInitiateButton());
		new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#queryConfirmation")));
	}
	
	public void initiateAndVerifyConfirmMessage(boolean shouldInitiate) {
		final String msgExp = "You have selected to initiate the appointment. Are you sure you want to continue?";
		ConfirmPopUp popUp = appointmentAction("Initiate");
		HardVerify.True(popUp.isOpen(), "Verifying initiate confirmation dialog-box is displyed...", "[PASSED]",
				"Confirmation dialog-box is not displayed. [FAILED]");
		HardVerify.EqualsIgnoreCase(popUp.getConfirmationText(), msgExp,
				"Verifying Reschedule confirmation message: [" + msgExp + "] is displyed...", "[PASEED]",
				"Reschedule confirmation message found: [" + popUp.getConfirmationText() + "] [FAILED]");
		if (shouldInitiate) {
			Log.logStep("Clicking 'Yes' on confirm dialog...");
			popUp.clickYesButton();
		} else {
			Log.logStep("Clicking 'No' on confirm dialog...");
			popUp.clickNoButton();
		}
	}
	
	public void verifyRecallAssessmentIsEnabled(boolean isEnabled) {
		if (isEnabled) {
			HardVerify.True(UiHelper.isEnabled(crAppDetails.getRecallButton()),
					"Verifying 'Recall Assessment' button is enabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Recall Assessment' button found disabled for clicking. [FAILED]");
		} else {
			HardVerify.False(UiHelper.isEnabled(crAppDetails.getRecallButton()),
					"Verifying 'Recall Assessment' button is disabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Recall Assessment' button found enabled for clicking. [FAILED]");
		}
	}
	
	public void recallAndVerifyConfirmMessage(boolean shouldRecall) {
		final String msgExp = "The assessment will be recalled. Are you sure you want to proceed?";
		final String successMsg = "Assessment recalled successfully";
		ConfirmPopUp popUp = appointmentAction("Recall");
		HardVerify.True(popUp.isOpen(), "Verifying recall confirmation dialog-box is displyed...", "[PASSED]",
				"Confirmation dialog-box is not displayed. [FAILED]");
		HardVerify.EqualsIgnoreCase(popUp.getConfirmationText(), msgExp,
				"Verifying recall confirmation message: [" + msgExp + "] is displyed...", "[PASEED]",
				"Recall confirmation message found: [" + popUp.getConfirmationText() + "] [FAILED]");
		if (shouldRecall) {
			Log.logStep("Clicking 'Yes' on confirm dialog...");
			popUp.clickYesButton();
			String infoFound = crAppDetails.getSuccessInfo();
			Verify.True(infoFound.equalsIgnoreCase(successMsg), "Verifying recall success info...", "[PASSED]",
					"Info found: [" + infoFound + "]. [FAILED]");
		} else {
			Log.logStep("Clicking 'No' on confirm dialog...");
			popUp.clickNoButton();
		}
	}
	
	public void verifyCancelAppointmentIsEnabled(boolean isEnabled) {
		if (isEnabled) {
			HardVerify.True(UiHelper.isEnabled(crAppDetails.getCancelAppointmentButton()),
					"Verifying 'Cancel Appointment' button is enabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Cancel Appointment' button found disabled for clicking. [FAILED]");
		} else {
			HardVerify.False(UiHelper.isEnabled(crAppDetails.getCancelAppointmentButton()),
					"Verifying 'Cancel Appointment' button is disabled for clicking in CR Appointment screen...",
					"[PASSED]", "'Cancel Appointment' button found enabled for clicking. [FAILED]");
		}
	}
	
	public void cancelAndVerifyConfirmMessage(boolean shouldCancel) {
		final String msgExp = "Are you sure you want to cancel this appointment?";
		ConfirmPopUp popUp = appointmentAction("Cancel");
		HardVerify.True(popUp.isOpen(), "Verifying cancel confirmation dialog-box is displyed...", "[PASSED]",
				"Confirmation dialog-box is not displayed. [FAILED]");
		HardVerify.EqualsIgnoreCase(popUp.getConfirmationText(), msgExp,
				"Verifying cancel confirmation message: [" + msgExp + "] is displyed...", "[PASEED]",
				"Cancel confirmation message found: [" + popUp.getConfirmationText() + "] [FAILED]");
		HardVerify.True(Required.isDropdownRequired(popUp.getReasonDropdown()),
				"Verifying 'Reason dropdown' is required", "[PASSED]", "'Reason dropdown' is not required [FAILED]");
		if (shouldCancel) {
			Log.logStep("Selecting reason 'Tech Difficulties' from dropdown...");
			popUp.inputPredefinedReason("Tech Difficulties");
			Log.logStep("Clicking 'Yes' on confirm dialog...");
			popUp.clickYesButton();
		} else {
			Log.logStep("Clicking 'No' on confirm dialog...");
			popUp.clickNoButton();
		}
	}

	public void veifyScheduledDateIsCleared() {
		HardVerify.True(crAppDetails.getScheduleDateTime().equals(""), "Verifying Scheduled date is blank...",
				"[PASSED]", "Scheduled date found: [" + crAppDetails.getScheduleDateTime() + "] [FAILED]");
	}
	
	Date uiPreviousNotificationDate, uiPreviousScheduledDate, uiCurrentNotificationDate, uiCurrentScheduledDate;
	
	public void verifyDateTimeFromUi(String type) {
		String formattedTime, hour, min, amPm;
		String[] splitTime;
		switch (type) {
		case "Notification":
			formattedTime = timeFormat.format(uiCurrentNotificationDate);
			splitTime = (formattedTime.split("-"));
			hour = splitTime[0];
			min = splitTime[1];
			amPm = splitTime[2];
			verifyDate(type, uiCurrentNotificationDate);
			verifyTime(type, hour, min, amPm);
			break;
		case "Schedule":
			formattedTime = timeFormat.format(uiCurrentScheduledDate);
			splitTime = (formattedTime.split("-"));
			hour = splitTime[0];
			min = splitTime[1];
			amPm = splitTime[2];
			verifyDate(type, uiCurrentScheduledDate);
			verifyTime(type, hour, min, amPm);
			break;
		default:
			break;
		}
	}
	
	///////////////////////// Steps for: NotificationDateTest /////////////////////////
	
	static String currentNotificationDateTime, currentScheduleDateTime;

	public Date getCurrentNotificationDateTime() {
		currentNotificationDateTime = crAppDetails.getNotificationDateTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
		if (false == currentNotificationDateTime.isEmpty()) {
			try {
				Date date = formatter.parse(currentNotificationDateTime);
				return date;
			} catch (ParseException e) {
				Log.logWarning(
						"Current Notification Date found: [" + currentNotificationDateTime + "], couldn't parse.");
				return null;
			}
		}
		return null;
	}

	public Date getCurrentScheduleDateTime() {
		currentScheduleDateTime = crAppDetails.getScheduleDateTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
		if (false == currentScheduleDateTime.isEmpty()) {
			try {
				Date date = formatter.parse(currentScheduleDateTime);
				return date;
			} catch (ParseException e) {
				Log.logWarning("Current Scheduled Date found: [" + currentScheduleDateTime + "], couldn't parse.");
				return null;
			}
		}
		return null;
	}
	
	public Date getUserTimeZoneDateTime(String userTimeZone) {
		Date currentDateTime = new Date();
		DateFormat dateTimeZoneFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a '('z')'");
		Log.logInfo("Current host date time: [" + dateTimeZoneFormatter.format(currentDateTime) + "]");
		DateTime datetime = new DateTime(currentDateTime);
		DateTimeZone datetimeZone = DateTimeZone.forID(userTimeZone);
		DateTime userTimeZoneDateTime = datetime.withZone(datetimeZone);
		Date tzFormattedDate = userTimeZoneDateTime.toLocalDateTime().toDate();
		Log.logInfo("Converted date time is: [" + dateTimeZoneFormatter.format(tzFormattedDate)
				+ "] to user time zone: [" + userTimeZone + "]");
		return tzFormattedDate;
	}
	
	public void setDateTime(String type, Date dateTime, boolean shouldSet) {
		String formattedTime = timeFormat.format(dateTime);
		String[] splitTime = (formattedTime.split("-"));
		String hour = splitTime[0];
		String min = splitTime[1];
		String amPm = splitTime[2];
		uiPreviousNotificationDate = getCurrentNotificationDateTime();
		uiPreviousScheduledDate = getCurrentScheduleDateTime();
		if (shouldSet) {
			switch (type) {
			case "Notification":
				
				crAppDetails.clearNotificationDate();
				setDate(type, dateTime);
				setTime(type, hour, min, amPm);
				uiCurrentNotificationDate = getCurrentNotificationDateTime();
				break;

			case "Schedule":
				DateFormat minuteFormatter = new SimpleDateFormat("mm");
				try {
					String currentMin = minuteFormatter.format(uiPreviousScheduledDate);
					if (currentMin.equalsIgnoreCase(min)) {
						Log.logStep("Changing given schedule minute:[" + min
								+ "] because found equal to current UI minute: [" + currentMin + "]");
						if (currentMin.equalsIgnoreCase("45")) {
							min = String.valueOf(Integer.parseInt(min) - 15);
						} else {
							min = String.valueOf(Integer.parseInt(min) + 15);
						}
					}
				} catch (NullPointerException npe) {
					Log.logInfo("Current schedule date found blank");
				}
				crAppDetails.clearScheduleDate();
				setDate(type, dateTime);
				setTime(type, hour, min, amPm);
				uiCurrentScheduledDate = getCurrentScheduleDateTime();
				break;
			
			default:
				break;
			}
		}
	}
	
	public void pickClinicianTimeSlot(String clinician, String type, Date scheduleDate, boolean pickSlot) {
		if (pickSlot) {
			String formattedTime = timeFormat.format(scheduleDate);
			String[] splitTime = (formattedTime.split("-"));
			String hour = splitTime[0];
			String min = splitTime[1];
			String amPm = splitTime[2];
			clickPickClinicianLink();
			selectClinicianTimeSlot(clinician, type, hour, min, amPm);
		}
	}

	public void pickClinicianTimeSlot(String clinician, String type, boolean pickSlot) {
		if (pickSlot) {
			Date scheduleDate = getCurrentScheduleDateTime();
			String formattedTime = timeFormat.format(scheduleDate);
			String[] splitTime = (formattedTime.split("-"));
			String hour = splitTime[0];
			String min = splitTime[1];
			String amPm = splitTime[2];
			clickPickClinicianLink();
			selectClinicianTimeSlot(clinician, type, hour, min, amPm);
		}
	}
	
	/*public boolean pickClinicianFirstTimeSlot(String clinician, String type, boolean pickSlot) {
		boolean found = false;
		if (pickSlot) {
			clickPickClinicianLink();
			List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(clinician, type);
			if (null != slotsFound) {
				UiHelper.click(slotsFound.get(0));
				uiCurrentScheduledDate = getCurrentScheduleDateTime();
				found = true;
			} else {
				throw new SkipException("No slot found for clinician: [" + clinician + "] with type: [" + type + "]");
			}
			return found;
		}
		return found;
	}*/
	
	public boolean pickClinicianFirstTimeSlot(String clinician, String type, boolean pickSlot) {
		boolean found = false;
		if (pickSlot) {
			String hour, min, amPm, amPmFound;
			int hourFound;
			Date scheduledDate;
			Calendar cal;
			clickPickClinicianLink();
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(clinician, type);
			while (null == slotsFound) {
				do {
					crAppDetails.changeScheduleTime("Minute", "Increase");
					slotsFound = crAppDetails.getClinicianTimeSlots(clinician, type);
					if (null != slotsFound) {
						Log.logInfo("Found time slot for clinician: [" + clinician + "] with type: [" + type + "]");
						break;
					}
					String timeFound = crAppDetails.getScheduleDateTime().split(" ", 2)[1];
					String[] splitTime = timeFound.split("(:| )");
					hourFound = Integer.parseInt(splitTime[0]);
					amPmFound = splitTime[2];
				} while (amPmFound.equalsIgnoreCase("pm") && (hourFound < 8));
				if (null != slotsFound)
					break;
				scheduledDate = getCurrentScheduleDateTime();
				cal = Calendar.getInstance();
				cal.setTime(scheduledDate);
				cal.add(Calendar.DATE, 1);
				scheduledDate = cal.getTime();
				setDate("Schedule", scheduledDate);
				hour = "07";
				min = "00";
				amPm = "AM";
				setTime("Schedule", hour, min, amPm);
				slotsFound = crAppDetails.getClinicianTimeSlots(clinician, type);
			}
			WebElement slotFound = slotsFound.get(0);
			Log.logStep("Clicking first time slot at: [" + slotFound.getText() + "]");
			UiHelper.click(slotFound);
			uiCurrentScheduledDate = getCurrentScheduleDateTime();
			found = true;
			return found;
		}
		return found;
	}
	
	public void scheduleAppointment(boolean shouldSchedule) {
		if (shouldSchedule) {
			ConfirmPopUp popUp = appointmentAction("Schedule");
			Log.logStep("Clicking 'Yes' on confirm dialog...");
			popUp.clickYesButton();
		}
	}
	
	public void verifyScheduledDateTime(Date expDateTime) {
		String formattedTime = timeFormat.format(expDateTime);
		String[] splitTime = (formattedTime.split("-"));
		String hour = splitTime[0];
		String min = splitTime[1];
		String amPm = splitTime[2];
		verifyDate("Schedule", expDateTime);
		verifyTime("Schedule", hour, min, amPm);
	}
	
	public void verifyDateTime(String type, Date expDateTime) {
		String formattedTime = timeFormat.format(expDateTime);
		String[] splitTime = (formattedTime.split("-"));
		String hour = splitTime[0];
		String min = splitTime[1];
		String amPm = splitTime[2];
		switch (type) {
		case "Notification":
			verifyDate(type, expDateTime);
			verifyTime(type, hour, min, amPm);
			break;
		case "Schedule":
			verifyDate(type, expDateTime);
			verifyTime(type, hour, min, amPm);
			break;
		default:
			break;
		}
	}
	
	public void cancelAppointment() {
		ConfirmPopUp popUp = appointmentAction("Cancel");
		Log.logStep("Canceling the appointment...");
		popUp.inputPredefinedReason("Tech Difficulties");
		popUp.clickYesButton();
	}
	
	public void setAppointmentStatus(String status, Date schDate, boolean shouldSet) {
		if (shouldSet) {
			switch (status) {
			case "Requested":
				WebElement cancelBtn = UiHelper
						.fluentWaitForElementClickability(crAppDetails.getCancelAppointmentButton(), 15);
				if (null != cancelBtn)
					cancelAppointment();
				WebElement block = UiHelper.fluentWaitForElementVisibility(crAppDetails.getScheduledDateBlock(), 15);
				if (null != block) {
					setDateTime("Schedule", schDate, true);
					saveAppointment(true);
				}
				break;
				
			case "Scheduled":
				clickPickClinicianLink();
				List<String> clinicians = crAppDetails.getClinicianNames();
				for (String clinician : clinicians) {
					if(pickClinicianFirstTimeSlot(clinician, "Available", true)){
						scheduleAndVerifyConfirmMessage();
						break;
					}
				}
				break;

			default:
				break;
			}
		}
	}
	
	public Date getRandomCurrentDate(int interval) {
		Random random = new Random();
		DateTime startTime = new DateTime(new Date());
		Minutes minimumPeriod = Minutes.minutes(interval);
		int minimumPeriodInSeconds = minimumPeriod.toStandardSeconds().getSeconds();
		int maximumPeriodInSeconds = Hours.ONE.toStandardSeconds().getSeconds();
		Seconds randomPeriod = Seconds.seconds(random.nextInt(maximumPeriodInSeconds - minimumPeriodInSeconds));
		DateTime endTime = startTime.plus(minimumPeriod).plus(randomPeriod);
		return endTime.toLocalDateTime().toDate();
	}
	
///////////////////////// Steps for: UpdateDateInScheduledAppointmentTests /////////////////////////
	
	public void saveAppointmentAndConfirm(boolean shouldSave, boolean shouldConfirm) {
		ConfirmPopUp confirmPopUp = null;
		if (shouldSave) {
			Log.logStep("Clicking save button on Appointment details...");
			crAppDetails.saveChanges();
			confirmPopUp = new ConfirmPopUp(UiHelper.findElement(By.cssSelector("div#informationDialog.modalshow")));
			if (shouldConfirm) {
				Log.logStep("Selecting reason 'Scheduling Adjustment' from dropdown...");
				confirmPopUp.inputPredefinedReason("Scheduling Adjustment");
				Log.logStep("Clicking 'Yes' on confirm dialog...");
				confirmPopUp.clickYesButton();
			} else {
				Log.logStep("Clicking 'No' on confirm dialog...");
				confirmPopUp.clickNoButton();
			}
		}
	}
	
	public void getCurrentClinicianCalender(boolean shouldClick) {
		if (shouldClick) {
			Log.logStep("Getting calender info for selected clinician...");
			UiHelper.clickAndWait(crAppDetails.getCurrentClinicianLink());
		}
	}
	
	public boolean updateClinicianAndTimeSlot(boolean setSchedule, boolean changeClinician, boolean updateTimeSlot,
			boolean shouldUpdate) {
		if (shouldUpdate) {
			ConfirmPopUp confirmPopUp = null;
			String slotTime;

			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mma");
			
//			Date currentScheduleDate = getCurrentScheduleDateTime();
//			String currentScheduleTime = timeFormatter.format(currentScheduleDate);
			
			String previousScheduleTime = timeFormatter.format(uiPreviousScheduledDate);

			String currentClinician = crAppDetails.getCurrentClinicianName(setSchedule);
			List<String> clinicians = crAppDetails.getClinicianNames();

			if (changeClinician) { // new clinician
				if (clinicians.size() > 1) {
					if (updateTimeSlot) {
						Log.logStep("Changing time slot & clinician");
						for (String clinician : clinicians) {
							if (false == clinician.equalsIgnoreCase(currentClinician)) {
								List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(clinician,
										"Available");

								if (null != slotsFound) {
									for (WebElement slot : slotsFound) {
										slotTime = slot.getText();
										Log.logDebugMessage(
												"currentScheduleTime: " + slotTime + " previousScheduleTime: " + previousScheduleTime);
										if (!slotTime.equalsIgnoreCase(previousScheduleTime)) {
											Log.logStep("Clicking time slot: [" + slotTime + "]");
											UiHelper.click(slot);
											confirmPopUp = appointmentAction("Reschedule");
											Log.logStep("Selecting reason 'Scheduling Adjustment' from dropdown...");
											confirmPopUp.inputPredefinedReason("Scheduling Adjustment");
											Log.logStep("Clicking 'Yes' on confirm dialog...");
											confirmPopUp.clickYesButton();
											return true;
										}
									}
								} else {
									Log.logWarning("No available slot found for clinician: [" + clinician + "]");
								}
							}
						}
					} else {
						Log.logStep("Changing clinician with same time slot");
						for (String clinician : clinicians) {
							if (false == clinician.equalsIgnoreCase(currentClinician)) {
								List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(clinician,
										"Available");

								if (null != slotsFound) {
									for (WebElement slot : slotsFound) {
										slotTime = slot.getText();
										Log.logDebugMessage(
												"currentScheduleTime: " + slotTime + " previousScheduleTime: " + previousScheduleTime);
										if (slotTime.equalsIgnoreCase(previousScheduleTime)) {
											Log.logStep("Clicking time slot: [" + slotTime + "]");
											UiHelper.click(slot);
											confirmPopUp = appointmentAction("Update");
											Log.logStep("Clicking 'Yes' on confirm dialog...");
											confirmPopUp.clickYesButton();
											return true;
										}
									}
								} else {
									Log.logWarning("No available slot found for clinician: [" + clinician + "]");
								}
							}
						}
					}
				} else {
					throw new SkipException("Found less than two clinician. Skipping tests...");
				}

			} else { // same clinician
				if (updateTimeSlot) {
					Log.logStep("Changing time slot for clinician: [" + currentClinician + "]");
					List<WebElement> slotsFound = crAppDetails.getClinicianTimeSlots(currentClinician, "Available");

					if (null != slotsFound) {
						for (WebElement slot : slotsFound) {
							slotTime = slot.getText();
							Log.logDebugMessage(
									"currentScheduleTime: " + slotTime + " previousScheduleTime: " + previousScheduleTime);
							if (!slotTime.equalsIgnoreCase(previousScheduleTime)) {
								Log.logStep("Clicking time slot: [" + slotTime + "]");
								UiHelper.click(slot);
								confirmPopUp = appointmentAction("Reschedule");
								Log.logStep("Selecting reason 'Scheduling Adjustment' from dropdown...");
								confirmPopUp.inputPredefinedReason("Scheduling Adjustment");
								Log.logStep("Clicking 'Yes' on confirm dialog...");
								confirmPopUp.clickYesButton();
								return true;
							}
						}
					} else {
						Log.logWarning("No available slot found for clinician: [" + currentClinician + "]");
					}
				} else {
					Log.logWarning(
							"Different time slot should be selected for same clinician: [" + currentClinician + "].");
				}
			}
			return false;
		}
		return false;
	}
}
