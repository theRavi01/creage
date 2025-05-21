package com.creage.SecurityConfig;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private List<String> publicUrls;
    private List<String> securedUrls;
    private List<String> corsAllowedOrigins;

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public void setPublicUrls(List<String> publicUrls) {
        this.publicUrls = publicUrls;
    }

    public List<String> getSecuredUrls() {
        return securedUrls;
    }

    public void setSecuredUrls(List<String> securedUrls) {
        this.securedUrls = securedUrls;
    }

    public List<String> getCorsAllowedOrigins() {
        return corsAllowedOrigins;
    }

    public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) {
        this.corsAllowedOrigins = corsAllowedOrigins;
    }
}
