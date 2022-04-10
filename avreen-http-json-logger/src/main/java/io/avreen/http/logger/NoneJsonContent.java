package io.avreen.http.logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoneJsonContent
{
    @JsonProperty("out-content")
    private String   outContent ;
    @JsonProperty("in-content")
    private String   inContent ;

    public String getOutContent() {
        return outContent;
    }

    public NoneJsonContent setOutContent(String outContent) {
        this.outContent = outContent;
        return this;
    }

    public String getInContent() {
        return inContent;
    }

    public NoneJsonContent setInContent(String inContent) {
        this.inContent = inContent;
        return this;
    }
}
