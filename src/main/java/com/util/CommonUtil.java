package com.util;

import com.common.configuration.CaboryaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    private CaboryaConfig caboryaConfig;

    public String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public String getBaseMailUrl() {
        return "http://" + caboryaConfig.getSupport().getBaseUrl();
    }

    public String getSupportMailUrl() {
        return caboryaConfig.getSupport().getMail();
    }
}
