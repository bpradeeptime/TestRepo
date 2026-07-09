package com.storybuilder.controller;

import com.storybuilder.dto.GitConfigDto;
import com.storybuilder.model.JiraStory;
import com.storybuilder.model.WorkflowStatus;
import com.storybuilder.service.CodeGeneratorService;
import com.storybuilder.service.GitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class WorkflowController {
    
    private final CodeGeneratorService codeGeneratorService;
    private final GitService gitService;
    
    public WorkflowController(CodeGeneratorService codeGeneratorService, GitService gitService) {
        this.codeGeneratorService = codeGeneratorService;
        this.gitService = gitService;
    }
    
    @PostMapping("/git-config")
    public ResponseEntity<Void> setGitConfig(@RequestBody GitConfigDto config) {
        gitService.setConfig(config);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/git-config")
    public ResponseEntity<GitConfigDto> getGitConfig() {
        return ResponseEntity.ok(gitService.getConfig());
    }
    
    @PostMapping("/start")
    public ResponseEntity<WorkflowStatus> startWorkflow(
            @RequestBody Map<String, String> request) {
        String storyKey = request.get("storyKey");
        String repoUrl = request.get("repoUrl");
        
        JiraStory story = new JiraStory();
        story.setKey(storyKey);
        story.setSummary(request.getOrDefault("summary", ""));
        story.setDescription(request.getOrDefault("description", ""));
        
        codeGeneratorService.startWorkflow(story, repoUrl);
        return ResponseEntity.ok(codeGeneratorService.getCurrentStatus());
    }
    
    @GetMapping("/status")
    public ResponseEntity<WorkflowStatus> getStatus() {
        return ResponseEntity.ok(codeGeneratorService.getCurrentStatus());
    }
    
    @GetMapping("/changes")
    public ResponseEntity<List<String>> getFileChanges() {
        return ResponseEntity.ok(codeGeneratorService.getFileChanges());
    }
    
    @PostMapping("/approve-plan")
    public ResponseEntity<WorkflowStatus> approvePlan() {
        codeGeneratorService.approvePlan();
        return ResponseEntity.ok(codeGeneratorService.getCurrentStatus());
    }
    
    @PostMapping("/approve-commit")
    public ResponseEntity<WorkflowStatus> approveCommit() {
        codeGeneratorService.approveCommit();
        return ResponseEntity.ok(codeGeneratorService.getCurrentStatus());
    }
    
    @PostMapping("/reject-commit")
    public ResponseEntity<WorkflowStatus> rejectCommit(@RequestBody Map<String, String> request) {
        codeGeneratorService.rejectCommit(request.get("feedback"));
        return ResponseEntity.ok(codeGeneratorService.getCurrentStatus());
    }
    
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        codeGeneratorService.reset();
        return ResponseEntity.ok().build();
    }
}
