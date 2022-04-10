package io.avreen.http.bomber;


import java.time.Duration;
import java.util.Set;

/**
 * The class Http message log properties.
 */
public final class HttpMsgBomberProperties {

    private String id;
    private String urlMatcherPattern;
    private boolean enable = true;
    private Set<String> bomberFields;
    private String bomberFieldsDelimiter = ",";
    private String cacheManagerName;
    private Duration    cacheTimeout = Duration.ofMinutes(10);
    private Duration    callDuration = Duration.ofSeconds(10);

    public String getUrlMatcherPattern() {
        return urlMatcherPattern;
    }

    public void setUrlMatcherPattern(String urlMatcherPattern) {
        this.urlMatcherPattern = urlMatcherPattern;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public String getBomberFieldsDelimiter() {
        return bomberFieldsDelimiter;
    }

    public void setBomberFieldsDelimiter(String bomberFieldsDelimiter) {
        this.bomberFieldsDelimiter = bomberFieldsDelimiter;
    }

    public String getCacheManagerName() {
        return cacheManagerName;
    }

    public void setCacheManagerName(String cacheManagerName) {
        this.cacheManagerName = cacheManagerName;
    }

    public Duration getCacheTimeout() {
        return cacheTimeout;
    }

    public void setCacheTimeout(Duration cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    public Duration getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Duration callDuration) {
        this.callDuration = callDuration;
    }

    public Set<String> getBomberFields() {
        return bomberFields;
    }

    public void setBomberFields(Set<String> bomberFields) {
        this.bomberFields = bomberFields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
