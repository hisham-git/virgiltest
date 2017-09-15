package mt.siteportal.utils.helpers;

import java.util.Set;

import mt.siteportal.utils.Browser;

/**
 * Simple helper class to close any extra browser windows/tabs
 * 
 * @author Syed A. Zawad
 *
 */
public class BrowserPDFViewerHelper {

	/**
	 * Cycles through all the available windows tabs and closes them if they do
	 * not have the title "MedAvante Portal Application". Then switch browser
	 * focus to the remaining tabs
	 */
	public static void close() {
		Set<String> handles = Browser.getDriver().getWindowHandles();
		if (handles.size() == 1)
			return;
		for (String handle : handles) {
			Browser.getDriver().switchTo().window(handle);
			if (!"MedAvante Portal Application".equals(Browser.getPageTitle())) {
				Browser.getDriver().close();
			}
		}
		handles = Browser.getDriver().getWindowHandles();
		for (String handle : handles) {
			Browser.getDriver().switchTo().window(handle);
		}
	}
}