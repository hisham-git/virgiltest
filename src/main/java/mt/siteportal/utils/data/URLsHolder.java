package mt.siteportal.utils.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * A container for all AUT's page URLs.
 * Spring bean is used, see func_tests.config.xml.
 */
public class URLsHolder {
    private static final Logger logger = LoggerFactory.getLogger(URLsHolder.class);

    private static URLsHolder holder;

    private URLsHolder(){}

    public static URLsHolder getHolder()
    {
        if (null == holder){
            try {
                holder = (URLsHolder) AppContext.getContext().getBean("urls");
            } catch (Exception e) {
                logger.error("Cannot init URLs Holder.", e);
            }
        }

        return holder;
    }

    private String hostUrl;

    private String hostPort;

    private String baseUrl;

    private String pageLogin;

    private String pageHome;
    
    private String pageAbout;

    private String pageUsersView;

    private String pageUserCreate;

    private String pageUserViewEdit;

    private String pageStudyDashboard;
    
    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPageLogin() {
        return pageLogin;
    }

    public void setPageLogin(String pageLogin) {
        this.pageLogin = pageLogin;
    }

    public String getPageHome() {
        return pageHome;
    }

    public void setPageHome(String pageHome) {
        this.pageHome = pageHome;
    }

    public String getPageAbout() {
        return pageAbout;
    }

    public void setPageAbout(String pageAbout) {
        this.pageAbout = pageAbout;
    }
    public String getPageUsersView() {
        return pageUsersView;
    }

    public void setPageUsersView(String pageUsersView) {
        this.pageUsersView = pageUsersView;
    }

    public String getPageUserCreate() {
        return pageUserCreate;
    }

    public void setPageUserCreate(String pageUserCreate) {
        this.pageUserCreate = pageUserCreate;
    }

    public String getPageUserViewEdit() {
        return pageUserViewEdit;
    }

    public void setPageUserViewEdit(String pageUserViewEdit) {
        this.pageUserViewEdit = pageUserViewEdit;
    }
    
    public void setPageStudyDashboard(String pageStudyDashboard){
    	this.pageStudyDashboard = pageStudyDashboard;
    }
    
    public String getPageStudyDashboard(){
    	return pageStudyDashboard;
    }
    
    private String siteportalURL;
    private String maportalURL;
    private String sponsorportalURL;
	private String studyDashboardRouteURL;
	
    public String getSiteportalURL() {
		return siteportalURL;
	}

	public void setSiteportalURL(String siteportalURL) {
		this.siteportalURL = siteportalURL;
	}

	public String getMaportalURL() {
		return maportalURL;
	}

	public void setMaportalURL(String maportalURL) {
		this.maportalURL = maportalURL;
	}
	
	public String getSponsorportalURL() {
		return sponsorportalURL;
	}

	public void setSponsorportalURL(String sponsorportalURL) {
		this.sponsorportalURL = sponsorportalURL;
	}

	public String getStudyDashboardRouteURL() {
		return studyDashboardRouteURL;
	}

	public void setStudyDashboardRouteURL(String studyDashboardRouteURL) {
		this.studyDashboardRouteURL = studyDashboardRouteURL;
	}
}