package com.example.workflow.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

/**
 * Service Task's Java Class implementation
 * Java Class 填写类的全路径, 例如: com.example.workflow.service.OfferLoan
 * Delegate Expression 填写类的名称, 例如: ${offerLoan}
 */
@Slf4j
@Service
public class OfferLoan implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("申请贷款金额 {}", execution.getVariable("amount"));
    }
}
