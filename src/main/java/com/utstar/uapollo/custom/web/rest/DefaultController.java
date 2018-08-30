/**
 * created on 2017年11月14日 下午4:47:56
 */
package com.utstar.uapollo.custom.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.web.vo.NeHeartbeatRequest;
import com.utstar.uapollo.custom.web.vo.NeHeartbeatResponse;

/**
 * @author UTSC0167
 * @date 2017年11月14日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class DefaultController {

    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    @PostMapping("/neheartbeat")
    @Timed
    public NeHeartbeatResponse neHeartbeat(@RequestBody NeHeartbeatRequest heartbeatRequest,
            HttpServletRequest request) {
        log.debug("neHeartbeat begin for clientIp: {} clientPort: {} requestBody: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatRequest);

        NeHeartbeatResponse heartbeatResponse = new NeHeartbeatResponse();
        heartbeatResponse.setAppType(999);
        heartbeatResponse.setAppVersion("1.0.0");
        heartbeatResponse.setServiceStatus(0);
        log.debug("neHeartbeat end for clientIp: {} clientPort: {} heartbeatResponse: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatResponse);

        return heartbeatResponse;
    }
}
