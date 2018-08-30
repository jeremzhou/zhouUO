/**
 * created on 2018年5月23日 上午10:22:27
 */
package com.utstar.uapollo.custom.dto;

import com.utstar.uapollo.custom.service.enumeration.ReleasingStep;
import com.utstar.uapollo.custom.service.enumeration.ReleasingStepResult;

/**
 * @author UTSC0167
 * @date 2018年5月23日
 *
 */
public class ReleasingResult {

    private final ReleasingStep releasingStep;

    private final Long id;

    private ReleasingStepResult releasingStepResult;

    private Object detail;

    public ReleasingResult(ReleasingStep releasingStep, Long id) {
        super();
        this.releasingStep = releasingStep;
        this.id = id;
    }

    public ReleasingStepResult getReleasingStepResult() {
        return releasingStepResult;
    }

    public ReleasingResult setReleasingStepResult(ReleasingStepResult releasingStepResult) {
        this.releasingStepResult = releasingStepResult;
        return this;
    }

    public Object getDetail() {
        return detail;
    }

    public ReleasingResult setDetail(Object detail) {
        this.detail = detail;
        return this;
    }

    public ReleasingStep getReleasingStep() {
        return releasingStep;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ReleasingResult [releasingStep=" + releasingStep + ", id=" + id
                + ", releasingStepResult=" + releasingStepResult + ", detail=" + detail + "]";
    }
}
