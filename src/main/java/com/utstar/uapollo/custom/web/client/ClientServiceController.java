package com.utstar.uapollo.custom.web.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.custom.web.vo.UapolloServiceDTO;

@RestController
@RequestMapping("/api/v1")
public class ClientServiceController {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceController.class);

    private static final String LOCALHOST_IPV6_IP = "0:0:0:0:0:0:0:1";

    private static final String LOCALHOST = "localhost";

    private final UapolloCustomConfiguration customConfiguration;

    public ClientServiceController(UapolloCustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    @GetMapping("/client-services/config")
    @Timed
    public List<UapolloServiceDTO> getConfigService(
            @RequestParam(value = "appId", required = false) String appId,
            @RequestParam(value = "ip", required = false) String clientIp,
            HttpServletRequest request) throws JsonProcessingException {

        log.info(
                "getConfigService begin for clientIp: {} clientPort: {} the requested ip: {} appId: {}",
                request.getRemoteAddr(), request.getRemotePort(), clientIp, appId);

        List<UapolloServiceDTO> configServiceList = new LinkedList<>();
        UapolloServiceDTO serviceDTO = new UapolloServiceDTO();
        serviceDTO.setAppName(customConfiguration.getApplicationName().toUpperCase());

        String hostName = System.getProperty("HOSTNAME");
        if (Strings.isNullOrEmpty(hostName)) {
            Optional<String> existingHostName = UapolloUtil.getLocalHostName();
            if (existingHostName.isPresent())
                hostName = existingHostName.get();
        }
        serviceDTO.setInstanceId(hostName + ":" + customConfiguration.getApplicationName()
                + "-configservice:" + request.getLocalPort());

        String ip = request.getLocalAddr();
        if (LOCALHOST_IPV6_IP.equals(ip))
            ip = LOCALHOST;
        serviceDTO.setHomepageUrl("http://" + ip + ":" + request.getLocalPort() + "/");
        configServiceList.add(serviceDTO);

        log.info("getConfigService end for clientIp: {} clientPort: {} the configServiceList:\n{}",
                request.getRemoteAddr(), request.getRemotePort(),
                UapolloUtil.objectToPrettyJson(configServiceList).get());
        return configServiceList;
    }
}
