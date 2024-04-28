package com.example.workflow.controller;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CamundaController {

    private final RepositoryService repositoryService;

    @Autowired
    public CamundaController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping("/deployProcessDefinition")
    public String deployProcessDefinition(@RequestParam("bpmnXml") String bpmnXml) {
        try {
            // 解析 XML 文件并部署流程定义
            Deployment deployment = repositoryService.createDeployment()
                    .addString("process.bpmn", bpmnXml)
                    .deploy();
            return "Process deployed successfully with ID: " + deployment.getId();
        } catch (Exception e) {
            return "Error deploying process: " + e.getMessage();
        }
    }
}
