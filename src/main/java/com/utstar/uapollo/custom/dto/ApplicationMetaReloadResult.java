/**
 * created on 2018年5月7日 上午10:35:41
 */
package com.utstar.uapollo.custom.dto;

import com.utstar.uapollo.custom.service.enumeration.ReloadOperationResult;
import com.utstar.uapollo.custom.service.enumeration.ReloadType;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * @author UTSC0167
 * @date 2018年5月7日
 *
 */
public final class ApplicationMetaReloadResult {

    private final ReloadType reloadType;

    private final Operation operation;

    private final String name;

    private final String configFile;

    private final ReloadOperationResult operationResult;

    public ApplicationMetaReloadResult(ReloadType reloadType, Operation operation,
            String name, String configFile, ReloadOperationResult operationResult) {
        this.reloadType = reloadType;
        this.operation = operation;
        this.name = name;
        this.configFile = configFile;
        this.operationResult = operationResult;
    }

    public ReloadType getReloadType() {
        return reloadType;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getName() {
        return name;
    }

    public String getConfigFile() {
        return configFile;
    }

    public ReloadOperationResult getOperationResult() {
        return operationResult;
    }

    @Override
    public String toString() {
        return "ApplicationMetaReloadResult [reloadType=" + reloadType + ", operation=" + operation
                + ", name=" + name + ", configFile=" + configFile + ", operationResult="
                + operationResult + "]";
    }
}
