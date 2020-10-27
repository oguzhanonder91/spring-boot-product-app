package com.common.configuration;

import com.common.configClasses.CaboryaMenu;
import com.common.configClasses.CaboryaSecurity;
import com.common.configClasses.CaboryaService;
import com.common.configClasses.CaboryaSupport;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "caborya")
public class CaboryaConfig {

    private CaboryaSecurity security;

    private CaboryaMenu menu;

    private CaboryaService service;

    private CaboryaSupport support;

    public CaboryaSecurity getSecurity() {
        return security;
    }

    public void setSecurity(CaboryaSecurity security) {
        this.security = security;
    }

    public CaboryaMenu getMenu() {
        return menu;
    }

    public void setMenu(CaboryaMenu menu) {
        this.menu = menu;
    }

    public CaboryaService getService() {
        return service;
    }

    public void setService(CaboryaService service) {
        this.service = service;
    }

    public CaboryaSupport getSupport() {
        return support;
    }

    public void setSupport(CaboryaSupport support) {
        this.support = support;
    }
}
