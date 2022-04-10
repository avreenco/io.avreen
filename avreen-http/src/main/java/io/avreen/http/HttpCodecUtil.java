package io.avreen.http;

import io.avreen.http.common.HttpMsg;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

public class HttpCodecUtil {
    private static final String SEMICOLON = ";";
    public static String getBasicUserName(String authorization) {
        String userName = null;
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            userName = values[0];
        }
        return userName;
    }

    public static String getBasicUserName(Map<String, String> header) {
        String authorization =getIgnoreCaseHeaderValue(header ,  HttpHeaderNames.AUTHORIZATION.toString());
        return getBasicUserName(authorization);
    }

    public static String getBasicUserPassword(Map<String, String> header) {
        String authorization =getIgnoreCaseHeaderValue(header ,  HttpHeaderNames.AUTHORIZATION.toString());
        return getBasicUserPassword(authorization);
    }
    public static String getIgnoreCaseHeaderValue(Map<String, String> header , String httpHeaderNames) {
        Set<String> strings = header.keySet();
        for (String string : strings) {
            if (string.equalsIgnoreCase(httpHeaderNames))
                return header.get(string);
        }
        return null;
    }
    public static String getMimeType(Map<String, String> header ) {
        String contentTypeValue = getIgnoreCaseHeaderValue(header,HttpHeaderNames.CONTENT_TYPE.toString());
        return contentTypeValue != null ? getMimeType(contentTypeValue) : null;
    }

    public static String getMimeType(String contentTypeValue) {
        int indexOfSemicolon = contentTypeValue.indexOf(SEMICOLON);
        if (indexOfSemicolon != -1) {
            return contentTypeValue.substring(0, indexOfSemicolon);
        } else {
            return contentTypeValue;
        }
    }

    public static String getBasicUserPassword(String authorization) {
        String password = null;
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            password = values[1];
        }
        return password;

    }
    public static String getRealIp(HttpMsg httpMsg) {
        String forwarder_for = null;
        Map headers = httpMsg.getHeaders();
        if (headers != null) {
            Set<String> headersKeySet = headers.keySet();
            for (String headerKey : headersKeySet) {
                if (headerKey.equalsIgnoreCase("X-FORWARDED-FOR")) {
                    forwarder_for = headers.get(headerKey).toString();
                    break;
                }
            }
        }
        if (forwarder_for == null && httpMsg.getRemoteAddress() != null)
            return httpMsg.getRemoteAddress().getHostString();
        if (forwarder_for == null)
            return null;
        String[] split = forwarder_for.split(",");
        return split[0].trim();
    }


}
