/**
 * created on 2018年5月22日 上午10:15:27
 */
package com.utstar.uapollo.custom.config;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.utstar.uapollo.custom.dto.ReleasingApplication;
import com.utstar.uapollo.custom.dto.UapolloClientId;
import com.utstar.uapollo.custom.web.vo.UapolloConfig;

/**
 * @author UTSC0167
 * @date 2018年5月22日
 *
 */
public final class UapolloCache {

    private UapolloCache() {

    }

    // application meta config: key -> aplicationMetaId value -> default config
    // content
    public static final Map<Long, TreeMap<String, String>> APPLICATION_META_CONFIG_MAP = new ConcurrentHashMap<>();

    // global config: key -> aplicationMetaId value -> global config content
    public static final Map<Long, TreeMap<String, String>> GLOBAL_CONFIG_MAP = new ConcurrentHashMap<>();

    // node config: key -> aplicationMetaId sub-key -> nodeId value -> node config
    // content
    public static final Map<Long, Map<Long, TreeMap<String, String>>> NODE_CONFIG_MAP = new ConcurrentHashMap<>();

    // releasing applications: key -> threadid value -> relasing applications
    public static final Map<Integer, BlockingQueue<ReleasingApplication>> RELEASING_APPLICATIONS_MAP = new ConcurrentHashMap<>();

    // released application config cache
    public static final Map<UapolloClientId, UapolloConfig> RELEASED_APPLICATIONS_CACHE = new ConcurrentHashMap<>();
}
