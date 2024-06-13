package com.example.workflow.controller;

import com.example.workflow.entity.ProcessDefinitionDTO;
import com.example.workflow.entity.TaskDTO;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProcessController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;

    @Autowired
    public ProcessController(RuntimeService runtimeService,
                             RepositoryService repositoryService,
                             TaskService taskService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
    }

    /**
     * 获取流程
     *
     * @return List<ProcessDefinitionDTO>
     */
    @GetMapping("/get-processes")
    public List<ProcessDefinitionDTO> getProcessDefinitions() {
        List<ProcessDefinition> processes = repositoryService.createProcessDefinitionQuery().list();
        return processes.stream()
                .map(ProcessDefinitionDTO::fromProcessDefinition)
                .collect(Collectors.toList());
    }

    /**
     * 启动流程
     *
     * @param amount 贷款金额
     * @param guarantor 担保人
     */
    @PostMapping("/start-process")
    public String startProcess(@RequestParam("amount") long amount,
                               @RequestParam("guarantor") String guarantor,
                               @RequestParam("name") String name,
                               @RequestParam("processDefinitionKey") String processDefinitionKey) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", amount);
        map.put("guarantor", guarantor);
        map.put("name", name);

        // 启动流程实例，并传递变量
        Execution execution = runtimeService.startProcessInstanceByKey(processDefinitionKey, map);
        return "实例启动成功，实例ID：" + execution.getProcessInstanceId();
    }

    /**
     * 获取任务
     *
     * @return List<Task>
     */
    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        List<Task> taskList = taskService.createTaskQuery().initializeFormKeys().list();
        return taskList.stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
    }

    /**
     * 审核任务
     *
     * @param taskId 任务ID
     * @param approve 是否批准
     * @return String
     */
    @PostMapping("/complete-task")
    public String approveTask(@RequestParam("taskId") String taskId,
                              @RequestParam("finalAmount") long finalAmount,
                              @RequestParam("approve") boolean approve) {
        List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
        if (!CollectionUtils.isEmpty(tasks)) {
            Map<String, Object> approveVariables = taskService.getVariables(taskId);
            approveVariables.put("approve", approve);
            approveVariables.put("finalAmount", finalAmount);
            taskService.complete(taskId, approveVariables);
            return "任务审核完成，审核"
                    + (approve ? "通过" : "拒绝");
        }
        return "无任务可审核";
    }

    @PostMapping("/getVariesByTaskId")
    public Object getVariesByTaskId(@RequestParam("taskId") String taskId) {
        return taskService.getVariables(taskId);
    }
}
