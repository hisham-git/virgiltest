package tests.Common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "About" })
public class AboutTest extends AbstractCommon {

	private final String aboutMedAvante = "MedAvante is the global clinical data services company " +
			"dedicated to maximizing signal detection in clinical trials. Founded in 2002," +
			" MedAvante pioneered Central Ratings, a groundbreaking clinical trial methodology." +
			" This heritage of clinical expertise, coupled with technical innovation and " +
			"operational skills, enabled MedAvante to develop the electronic source (eSource) " +
			"platform, Virgil, the first such technology to replace costly and error-prone paper" +
			" rating scales with real-time digital collection and cloud management of source data. Designed " +
			"by clinicians for clinicians, Virgil offers built-in clinical guidance to ensure" +
			" accurate, standardized assessments. MedAvante helps brings better drug therapies to market" +
			" through smarter, faster clinical trials. Based in Hamilton, NJ with operations teams in the" +
			" US, Germany, Russia and Japan, MedAvante delivers services for clinical trials in more" +
			" than 40 countries worldwide. For more information, please visit www.medavante.com.";

	private final String legalNotice = "This program is the confidential and proprietary product "
			+ "of MedAvante. Neither this program nor the information contained in "
			+ "this program may be disclosed to any third party. No portion of this"
			+ " program may be used for any purpose or transferred or reproduced in"
			+ " any form or by any means without the prior written permission of MedAvante"
			+ " in each instance. This program is subject to the additional terms, conditions "
			+ "and restrictions of a license agreement with MedAvante.";

	private final String copyright = new String("VIRGIL 2.3.0 © MedAvante 2016. All Rights Reserved.");
	private final String medavanteUrl = "http://www.medavante.com/";
	private final String privacyUrl = "http://www.medavante.com/privacy-policy/";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAs(adminLogin,adminPasword);
	}

	@Test(groups = { "AboutPage", "SFB-TC-604" }, description = "Verify controls and content displayed on About Page")
	public void aboutPageTest() {
		commonSteps.openAboutPage();
		commonSteps.aboutMedAvanteContentVerification(aboutMedAvante);
		commonSteps.legalNoticeContentVerification(legalNotice);
		commonSteps.navigationToMedAvanteVerification(medavanteUrl);
		UiHelper.switchBack(Browser.getDriver());
		commonSteps.navigationToPrivacyVerification(privacyUrl);
		UiHelper.switchBack(Browser.getDriver());
	}
}
