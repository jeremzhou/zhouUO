package com.utstar.uapollo.custom.web.rest.util;

import org.springframework.http.HttpHeaders;

import com.utstar.uapollo.web.rest.util.HeaderUtil;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public final class HeaderCustomUtil {

    private static final String APPLICATION_NAME = "uapolloApp";

    private HeaderCustomUtil() {
    }

    public static HttpHeaders createEntityReleaseAlert(String entityName, String param) {
        return HeaderUtil.createAlert(APPLICATION_NAME + "." + entityName + ".released", param);
    }
}
