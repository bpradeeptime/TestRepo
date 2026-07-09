package com.storybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraStory {
    private String id;
    private String key;
    private String summary;
    private String description;
    private String status;
    private String assignee;
    private String projectKey;
}
