package mt.siteportal.utils.helpers;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

/**
 * api - compile 'com.microsoft.ews-java-api:ews-java-api:2.0'
 * 
 * @author ubiswas
 *
 */

public class EwsCalendarReader {

	public static void main(String[] args) throws Exception {
		ExchangeService service = initiateExchangeServer("maclintest2@medavante.com", "#10Pencil");
		DateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = st.parse("2017-02-14 09:00:00");
		DateFormat end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endDate = end.parse("2017-02-14 09:45:00");
		findAppointments(service, startDate, endDate);

	}

	private static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {
		public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl) {
			return redirectionUrl.toLowerCase().startsWith("https://");
		}
	}

	private static ExchangeService connectViaExchangeAutodiscover(String email, String password) {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		try {

			service.setCredentials(new WebCredentials(email, password));
			service.autodiscoverUrl(email, new RedirectionUrlCallback());
			// service.setUrl(new
			// URI("https://outlook.office365.com/EWS/Exchange.asmx"));
			service.setTraceEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}

	public static void findAppointments(ExchangeService service, Date startDate, Date endDate) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar, new PropertySet());
		FindItemsResults<Appointment> findResults;
		CalendarView cView = new CalendarView(startDate, endDate);

		findResults = cf.findAppointments(cView);
		for (Appointment appt : findResults.getItems()) {
			System.out.println("SUBJECT=====" + appt.getSubject().toString());
			System.out.println("Satrt date: " + formatter.format(appt.getStart()));
			System.out.println("End Date :" + formatter.format(appt.getEnd()));

		}
	}

	public static ExchangeService initiateExchangeServer(String clnUserEmail, String clnPass) {
		ExchangeService service = connectViaExchangeAutodiscover(clnUserEmail, clnPass);

		return service;
	}
}
