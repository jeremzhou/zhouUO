package com.utstar.uapollo.custom.web.client;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.utstar.uapollo.custom.config.UapolloCache;
import com.utstar.uapollo.custom.config.UapolloConfigConsts;
import com.utstar.uapollo.custom.dto.UapolloClientId;
import com.utstar.uapollo.custom.service.ApplicationConfigRelaseService;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.custom.web.vo.UapolloConfig;
import com.utstar.uapollo.custom.web.vo.UapolloNotificationMessages;

/**
 * @author UTSC0167
 * @date 2018年8月17日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ClientConfigController {

    private static final Logger log = LoggerFactory.getLogger(ClientConfigController.class);

    private final ApplicationConfigRelaseService applicationConfigRelaseService;

    public ClientConfigController(ApplicationConfigRelaseService applicationConfigRelaseService) {
        super();
        this.applicationConfigRelaseService = applicationConfigRelaseService;
    }

    /**
     * @param appId
     * @param clusterName
     * @param namespace
     * @param dataCenter
     * @param clientSideReleaseKey
     * @param clientIp
     * @param messagesAsString
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/uapollo-clients/configs/{appId}/{clusterName}/{namespace:.+}")
    public UapolloConfig queryConfig(@PathVariable String appId, @PathVariable String clusterName,
            @PathVariable String namespace,
            @RequestParam(value = "dataCenter", required = false) String dataCenter,
            @RequestParam(value = "releaseKey", defaultValue = "-1") String clientSideReleaseKey,
            @RequestParam(value = "ip", required = false) String clientIp,
            @RequestParam(value = "messages", required = false) String jsonMessages,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("queryConfig begin for clientIp: {} clientPort: {} the requested ip: {} appId: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId);

        if (!UapolloConfigConsts.DEFAULT_CLUSTER.equals(clusterName)
                || !UapolloConfigConsts.DEFAULT_NAMESPACE.equals(namespace)) {

            log.info(
                    "queryConfig for clientIp: {} clientPort: {} the requested ip: {} appId: {} clusterName: {} namespace: {} don't match default value, return {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId, clusterName,
                    namespace, HttpStatus.NOT_FOUND);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        if (Strings.isNullOrEmpty(clientIp)) {
            clientIp = applicationConfigRelaseService.tryGetClientIp(request);
            log.info(
                    "queryConfig for clientIp: {} clientPort: {} tryGetClientIp get requested ip: {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp);
        }

        // handle notificationMessages
        final UapolloClientId clientId = new UapolloClientId(clientIp, appId);
        UapolloConfig uapolloConfig = UapolloCache.RELEASED_APPLICATIONS_CACHE.get(clientId);
        final boolean[] results = handleNotificationMessages(clientIp, appId, clientId,
                uapolloConfig, clientSideReleaseKey, jsonMessages, clusterName, namespace, request,
                response);
        final boolean isReturn = results[2];
        if (isReturn) {
            return null;
        }
        final boolean isNeedUpate = results[0];
        final boolean isModified = results[1];

        if (isNeedUpate) {
            Optional<UapolloConfig> existingUapolloConfig = applicationConfigRelaseService
                    .updateReleasedApplicationConfigCache(clientIp, appId);
            if (existingUapolloConfig.isPresent()) {
                uapolloConfig = existingUapolloConfig.get();
                log.info(
                        "queryConfig for requested ip: {} appId: {} the clientId: {} execute updateReleasedApplicationConfigCache to get uapolloConfig: {}",
                        clientIp, appId, clientId, uapolloConfig);
            } else {
                log.info(
                        "queryConfig for clientIp: {} clientPort: {} the requested ip: {} appId: {} execute updateReleasedApplicationConfigCache to get uapolloConfig is null, return {}",
                        request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                        jsonMessages, HttpStatus.INTERNAL_SERVER_ERROR);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return null;
            }
        }

        if (isModified) {
            log.info(
                    "queryConfig for clientIp: {} clientPort: {} requested ip: {} appId: {} get uapolloConfig: {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                    uapolloConfig);
            return uapolloConfig;
        } else {
            log.info(
                    "queryConfig for clientIp: {} clientPort: {} requested ip: {} appId: {} the nothing is changed, return {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                    HttpStatus.NOT_MODIFIED);
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
            return null;
        }
    }

    private final Optional<UapolloNotificationMessages> transformMessages(String jsonMessages) {

        Optional<UapolloNotificationMessages> exitingNotificationMessages = Optional.empty();
        if (!Strings.isNullOrEmpty(jsonMessages)) {
            Optional<Object> existingObj = UapolloUtil.jsonToObject(jsonMessages,
                    new TypeReference<UapolloNotificationMessages>() {
                    });
            if (existingObj.isPresent())
                exitingNotificationMessages = Optional
                        .of((UapolloNotificationMessages) existingObj.get());

        }
        return exitingNotificationMessages;
    }

    private final boolean[] handleNotificationMessages(String clientIp, String appId,
            UapolloClientId clientId, UapolloConfig uapolloConfig, String clientSideReleaseKey,
            String jsonMessages, String clusterName, String namespace, HttpServletRequest request,
            HttpServletResponse response) {

        boolean isNeedUpate = true;
        boolean isModified = true;
        boolean isReturn = false;
        if (uapolloConfig != null) {
            log.info(
                    "handleNotificationMessages for requested ip: {} appId: {} the clientId: {} from RELEASED_APPLICATIONS_CACHE get uapolloConfig: {}",
                    clientIp, appId, clientId, uapolloConfig);
            if (clientSideReleaseKey != null && jsonMessages != null) {

                log.info(
                        "handleNotificationMessages for requested ip: {} appId: {} handle otificationMessages clientSideReleaseKey: {} jsonMessages: {}",
                        clientIp, appId, clientSideReleaseKey, jsonMessages);
                Optional<UapolloNotificationMessages> existingNotificationMessages = transformMessages(
                        jsonMessages);
                if (!existingNotificationMessages.isPresent()) {
                    log.info(
                            "handleNotificationMessages for clientIp: {} clientPort: {} the requested ip: {} appId: {} execute transformMessages to convert jsonMessages: {} is null, return {}",
                            request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                            jsonMessages, HttpStatus.BAD_REQUEST);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    isReturn = true;
                    boolean[] returnResults = { isNeedUpate, isModified, isReturn };
                    return returnResults;
                }

                final UapolloNotificationMessages notificationMessages = existingNotificationMessages
                        .get();
                final String detailsKey = appId + "+" + clusterName + "+" + namespace;
                final Long notificationId = notificationMessages.get(detailsKey);

                if (uapolloConfig.getReleaseKey().equals(clientSideReleaseKey)
                        && uapolloConfig.getNotificationId().equals(notificationId)) {
                    log.info(
                            "handleNotificationMessages for requested ip: {} appId: {} the cachedReleaseKey: {} equals clientSideReleaseKey: {} and cachedNotificationId: {} equals notificationId: {}",
                            clientIp, appId, uapolloConfig.getReleaseKey(), clientSideReleaseKey,
                            uapolloConfig.getNotificationId(), notificationId);
                    isNeedUpate = false;
                    isModified = false;
                } else {
                    log.info(
                            "handleNotificationMessages for requested ip: {} appId: {} the cachedReleaseKey: {} don't equals clientSideReleaseKey: {} or cachedNotificationId: {} don't equals notificationId: {}",
                            clientIp, appId, uapolloConfig.getReleaseKey(), clientSideReleaseKey,
                            uapolloConfig.getNotificationId(), notificationId);
                }
            } else {
                log.info(
                        "handleNotificationMessages for requested ip: {} appId: {} the clientSideReleaseKey: {} or jsonMessages: {} is null.",
                        clientIp, appId, clientSideReleaseKey, jsonMessages);

                isNeedUpate = false;
                isModified = true;
            }
        } else {
            log.info(
                    "handleNotificationMessages for requested ip: {} appId: {} the clientId: {} from RELEASED_APPLICATIONS_CACHE get uapolloConfig is null.",
                    clientIp, appId, clientId);
        }

        boolean[] results = { isNeedUpate, isModified, isReturn };
        return results;
    }
}
