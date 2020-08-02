package vn.com.irtech.eport.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Global configuration class
 * 
 * @author admin
 */
@Component
@ConfigurationProperties(prefix = "eport")
public class Global
{
    private static String name;

    private static String version;

    private static String copyrightYear;

    private static boolean demoEnabled;

    private static String profile;

    private static String apiUrl;

    private static boolean addressEnabled;

    public static String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        Global.name = name;
    }

    public static String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        Global.version = version;
    }

    public static String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        Global.copyrightYear = copyrightYear;
    }

    public static boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        Global.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        Global.profile = profile;
    }

    public void setApiUrl(String apiUrl) {
		Global.apiUrl = apiUrl;
	}

	public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        Global.addressEnabled = addressEnabled;
    }

    /**
     * Get avatar upload path
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * Get download path
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * Get upload path
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

	public static String getApiUrl() {
		return Global.apiUrl;
	}
}
