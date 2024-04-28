package com.example.workflow.entity;

import lombok.Data;
import org.camunda.bpm.engine.task.Task;

@Data
public class TaskDTO {
    private String id;
    private String name;

    public static TaskDTO fromTask(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        return dto;
    }
}
