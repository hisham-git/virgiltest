package mt.siteportal.utils.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A container for configurable data: user names, passwords etc.
 * Spring bean is used, see smoke_tests.config.xml.
 */
public class DataHolder {
    private static final Logger logger = LoggerFactory.getLogger(DataHolder.class);

    private static DataHolder holder;

    private DataHolder(){}

    public static DataHolder getHolder()
    {
        if (null == holder){
            try {
                holder = (DataHolder) AppContext.getContext().getBean("data");
               
            } catch (Exception e) {
                logger.error("Cannot init Data Holder.", e);
            }
        }

        return holder;
    }

    private String adminName;
    
    private String adminAccountName;
    
    private String adminAccountPassword;
    
    private String userName;
    
    private String userPassword;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    
    public String getAdminAccountName() {
        return adminAccountName;
    }

    public void setAdminAccountName(String adminAccountName) {
        this.adminAccountName = adminAccountName;
    }

    public String getAdminAccountPassword() {
        return adminAccountPassword;
    }

    public void setAdminAccountPassword(String adminAccountPassword) {
        this.adminAccountPassword = adminAccountPassword;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	/*
	 * MAPORTAL USERS----------------------------------------------------------------
	 */
	private String maportalUserName;
	private String maportalUserAccountName;
	private String maportalUserAccountPassword;

	public String getMaportalUserName() {
		return maportalUserName;
	}

	public void setMaportalUserName(String maportalUserName) {
		this.maportalUserName = maportalUserName;
	}

	public String getMaportalUserAccountName() {
		return maportalUserAccountName;
	}

	public void setMaportalUserAccountName(String maportalUserAccountName) {
		this.maportalUserAccountName = maportalUserAccountName;
	}

	public String getMaportalUserAccountPassword() {
		return maportalUserAccountPassword;
	}

	public void setMaportalUserAccountPassword(String maportalUserAccountPassword) {
		this.maportalUserAccountPassword = maportalUserAccountPassword;
	}
    /*
     * -------------------------------------------------------------------------------
     */
	
	/*
	 * SITEPORTAL USERS----------------------------------------------------------------
	 */
	private String siteportaUserName;
	private String siteportalUserAccountName;
	private String siteportalUserAccountPassword;

	public String getSiteportalUserName() {
		return siteportaUserName;
	}

	public void setSiteportalUserName(String siteportaUserName) {
		this.siteportaUserName = siteportaUserName;
	}

	public String getSiteportalUserAccountName() {
		return siteportalUserAccountName;
	}

	public void setSiteportalUserAccountName(String siteportalUserAccountName) {
		this.siteportalUserAccountName = siteportalUserAccountName;
	}

	public String getSiteportalUserAccountPassword() {
		return siteportalUserAccountPassword;
	}

	public void setSiteportalUserAccountPassword(String siteportalUserAccountPassword) {
		this.siteportalUserAccountPassword = siteportalUserAccountPassword;
	}
	/*
	 * --------------------------------------------------------------------------------
	 */
	
	/*
	 * ENVIRONMENT SETTINGS-----------------------------------------------------------
	 */
	private String environmentName;
	
	public String getEnvironmentName(){
		return environmentName;
	}
	
	public void setEnvironmentName(String environmentName){
		this.environmentName = environmentName;
	}
	/*
	 * ------------------------------------------------------------------------------
	 */
}