/**
 * created on 2018年6月25日 上午10:15:31
 */
package com.utstar.uapollo.custom.web.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.utstar.uapollo.custom.web.vo.UapolloConfigNotification;
import com.utstar.uapollo.custom.web.vo.UapolloNotificationMessages;

import io.undertow.util.BadRequestException;

/**
 * @author BenQ
 * @date 2018年6月25日
 *
 */

@RestController
@RequestMapping("/api/v1")
public class ClientNotificationController {

    private static final Logger log = LoggerFactory.getLogger(ClientNotificationController.class);

    private static final TypeReference<LinkedList<UapolloConfigNotification>> notificationsTypeReference = new TypeReference<LinkedList<UapolloConfigNotification>>() {
    };

    private final ApplicationConfigRelaseService applicationConfigRelaseService;

    public ClientNotificationController(
            ApplicationConfigRelaseService applicationConfigRelaseService) {
        super();
        this.applicationConfigRelaseService = applicationConfigRelaseService;
    }

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/uapollo-clients/notifications")
    public List<UapolloConfigNotification> pollNotification(
            @RequestParam(value = "appId", required = false) String appId,
            @RequestParam(value = "cluster", required = false) String clusterName,
            @RequestParam(value = "notifications", required = false) String notificationsAsString,
            @RequestParam(value = "dataCenter", required = false) String dataCenter,
            @RequestParam(value = "ip", required = false) String clientIp,
            HttpServletRequest request, HttpServletResponse response) throws BadRequestException {

        log.debug("pollNotification uri: {}", request.getRequestURI());

        log.info(
                "pollNotification begin for clientIp: {} clientPort: {} the requested ip: {} appId: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId);

        if (!UapolloConfigConsts.DEFAULT_CLUSTER.equals(clusterName)) {

            log.info(
                    "pollNotification for clientIp: {} clientPort: {} the requested ip: {} appId: {} clusterName: {} don't match default value, return {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId, clusterName,
                    HttpStatus.NOT_FOUND);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        if (Strings.isNullOrEmpty(clientIp)) {
            clientIp = applicationConfigRelaseService.tryGetClientIp(request);
            log.debug(
                    "pollNotification for clientIp: {} clientPort: {} tryGetClientIp get requested ip: {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp);
        }

        log.debug(
                "pollNotification for clientIp: {} clientPort: {} the requested ip: {} appId: {} notificationsAsString: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                notificationsAsString);
        Optional<Object> existingNotifications = UapolloUtil.jsonToObject(notificationsAsString,
                notificationsTypeReference);
        if (!existingNotifications.isPresent()) {
            log.info(
                    "pollNotification for clientIp: {} clientPort: {} the requested ip: {} appId: {} execute jsonToObject to convert notificationsAsString: {} is null, return {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                    notificationsAsString, HttpStatus.BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        final List<UapolloConfigNotification> notifications = (LinkedList<UapolloConfigNotification>) existingNotifications
                .get();

        // get uapolloConfig
        final Optional<UapolloConfig> existingUapolloConfig = getUapolloConfig(clientIp, appId);
        if (!existingUapolloConfig.isPresent()) {
            log.info(
                    "pollNotification for clientIp: {} clientPort: {} the requested ip: {} appId: {} execute getUapolloConfig is null, return {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                    HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
        final UapolloConfig uapolloConfig = existingUapolloConfig.get();
        log.debug(
                "pollNotification for clientIp: {} clientPort: {} the requested ip: {} appId: {} execute getUapolloConfig: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId, uapolloConfig);

        // handle notification
        if (isUapolloConfigModified(uapolloConfig, notifications)) {

            LinkedList<UapolloConfigNotification> responseNotifications = generateNotifications(
                    uapolloConfig);
            log.info(
                    "pollNotification end for clientIp: {} clientPort: {} the requested ip: {} appId: {} will return notifications: {}",
                    request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                    responseNotifications);
            return responseNotifications;
        }

        log.info(
                "pollNotification end for clientIp: {} clientPort: {} the requested ip: {} appId: {} the config don't be modified, return: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId,
                HttpStatus.NOT_MODIFIED);
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return null;
    }

    private final Optional<UapolloConfig> getUapolloConfig(String clientIp, String appId) {

        final UapolloClientId clientId = new UapolloClientId(clientIp, appId);
        Optional<UapolloConfig> existingUapolloConfig = Optional
                .ofNullable(UapolloCache.RELEASED_APPLICATIONS_CACHE.get(clientId));

        if (!existingUapolloConfig.isPresent()) {
            existingUapolloConfig = applicationConfigRelaseService
                    .updateReleasedApplicationConfigCache(clientIp, appId);
        }
        return existingUapolloConfig;
    }

    private final boolean isUapolloConfigModified(UapolloConfig uapolloConfig,
            List<UapolloConfigNotification> notifications) {

        for (UapolloConfigNotification notification : notifications) {

            if (!uapolloConfig.getNotificationId().equals(notification.getNotificationId())
                    || !uapolloConfig.getNamespaceName().equals(notification.getNamespaceName())) {

                return true;
            }
        }

        return false;
    }

    private final LinkedList<UapolloConfigNotification> generateNotifications(
            UapolloConfig uapolloConfig) {

        LinkedList<UapolloConfigNotification> responseNotifications = new LinkedList<>();
        UapolloConfigNotification notification = new UapolloConfigNotification(
                uapolloConfig.getNamespaceName(), uapolloConfig.getNotificationId());
        UapolloNotificationMessages notificationMessages = new UapolloNotificationMessages();
        notificationMessages.put(uapolloConfig.getAppId() + "+" + uapolloConfig.getCluster() + "+"
                + uapolloConfig.getNamespaceName(), uapolloConfig.getNotificationId());
        notification.setMessages(notificationMessages);
        responseNotifications.add(notification);
        return responseNotifications;
    }
}