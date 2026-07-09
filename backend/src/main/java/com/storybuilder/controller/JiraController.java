package com.storybuilder.controller;

import com.storybuilder.dto.JiraConfigDto;
import com.storybuilder.model.JiraStory;
import com.storybuilder.service.JiraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jira")
@CrossOrigin(origins = "*")
public class JiraController {
    
    private final JiraService jiraService;
    
    public JiraController(JiraService jiraService) {
        this.jiraService = jiraService;
    }
    
    @PostMapping("/config")
    public ResponseEntity<Void> setConfig(@RequestBody JiraConfigDto config) {
        jiraService.setConfig(config);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/config")
    public ResponseEntity<JiraConfigDto> getConfig() {
        return ResponseEntity.ok(jiraService.getConfig());
    }
    
    @GetMapping("/stories")
    public ResponseEntity<List<JiraStory>> getStories() {
        List<JiraStory> stories = jiraService.getStories();
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/stories/{key}")
    public ResponseEntity<JiraStory> getStory(@PathVariable String key) {
        JiraStory story = jiraService.getStoryByKey(key);
        if (story != null) {
            return ResponseEntity.ok(story);
        }
        return ResponseEntity.notFound().build();
    }
}
