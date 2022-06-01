package com.shop.share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-" + applicationName + "-alert", message);
        try {
            httpHeaders.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));

        } catch (UnsupportedEncodingException e) {

        }
        return httpHeaders;
    }

    public static HttpHeaders createEntityCreationAlert(String applicaionName, boolean enableTranslation, String entityName, String param) {

        String message = enableTranslation ? applicaionName + "." + entityName + ".created"
                : "a new " + entityName + " is created with identifier " + param;
        return createAlert(applicaionName, message, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".updated"
                : "a new " + entityName + " is updated with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders deleteEntityUpdateAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".deleted"
                : "a new " + entityName + " is deleted with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createFailureAlert(String applicationName, boolean enableTranslation, String entityName, String errorKey, String defaultMessage) {

        log.error("Entity processing failded {} :", defaultMessage);
        String message = enableTranslation ? "error." + errorKey : defaultMessage;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-" + applicationName + "-error", message);
        httpHeaders.add("X-" + applicationName + "-params", entityName);
        return httpHeaders;
    }
}
