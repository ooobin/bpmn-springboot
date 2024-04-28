package com.example.workflow.entity;

import lombok.Data;
import org.camunda.bpm.engine.repository.ProcessDefinition;

@Data
public class ProcessDefinitionDTO {
    private String id;
    private String key;
    private String name;

    public static ProcessDefinitionDTO fromProcessDefinition(ProcessDefinition processDefinition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(processDefinition.getId());
        dto.setKey(processDefinition.getKey());
        dto.setName(processDefinition.getName());
        return dto;
    }
}
