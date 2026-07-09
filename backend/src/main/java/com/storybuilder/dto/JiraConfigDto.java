package com.storybuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraConfigDto {
    private String jiraUrl;
    private String email;
    private String apiToken;
    private String projectKey;
}
