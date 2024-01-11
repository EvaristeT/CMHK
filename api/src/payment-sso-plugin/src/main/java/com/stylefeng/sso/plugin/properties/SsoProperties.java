package com.stylefeng.sso.plugin.properties;

/**
 * sso配置
 *
 * @author fengshuonan
 * @date 2018-02-03 20:53
 */
public class SsoProperties {

    public static final String BEETLCONF_PREFIX = "spring.sso";

    /**
     * sso服务器的地址,以/结尾(不支持携带参数的url)
     */
    private String serverUrl;
    private String clientToServerUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

	public String getClientToServerUrl() {
		return clientToServerUrl;
	}

	public void setClientToServerUrl(String clientToServerUrl) {
		this.clientToServerUrl = clientToServerUrl;
	}


}
