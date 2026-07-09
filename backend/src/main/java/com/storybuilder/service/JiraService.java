package com.storybuilder.service;

import com.storybuilder.dto.JiraConfigDto;
import com.storybuilder.model.JiraStory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class JiraService {
    
    private JiraConfigDto currentConfig;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    
    public void setConfig(JiraConfigDto config) {
        this.currentConfig = config;
    }
    
    public JiraConfigDto getConfig() {
        return currentConfig;
    }
    
    public List<JiraStory> getStories() {
        if (currentConfig == null) {
            return new ArrayList<>();
        }
        
        try {
            String auth = Base64.getEncoder().encodeToString(
                (currentConfig.getEmail() + ":" + currentConfig.getApiToken()).getBytes()
            );
            
            String jql = "project = " + currentConfig.getProjectKey() + " AND issuetype = Story AND status != Done ORDER BY created DESC";
            String encodedJql = java.net.URLEncoder.encode(jql, java.nio.charset.StandardCharsets.UTF_8);
            
            String url = currentConfig.getJiraUrl() + "/rest/api/3/search?jql=" + encodedJql + "&maxResults=50&fields=summary,description,status,assignee";
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + auth)
                .header("Accept", "application/json")
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            List<JiraStory> stories = new ArrayList<>();
            
            if (response.statusCode() == 200) {
                org.json.JSONObject jsonResponse = new org.json.JSONObject(response.body());
                org.json.JSONArray issues = jsonResponse.getJSONArray("issues");
                
                for (int i = 0; i < issues.length(); i++) {
                    org.json.JSONObject issue = issues.getJSONObject(i);
                    org.json.JSONObject fields = issue.getJSONObject("fields");
                    
                    JiraStory story = new JiraStory();
                    story.setId(issue.getString("id"));
                    story.setKey(issue.getString("key"));
                    story.setSummary(fields.optString("summary", ""));
                    story.setDescription(fields.optString("description", ""));
                    
                    if (fields.has("status")) {
                        story.setStatus(fields.getJSONObject("status").optString("name", ""));
                    }
                    
                    if (fields.has("assignee") && !fields.isNull("assignee")) {
                        story.setAssignee(fields.getJSONObject("assignee").optString("displayName", ""));
                    }
                    
                    story.setProjectKey(currentConfig.getProjectKey());
                    stories.add(story);
                }
            }
            
            return stories;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public JiraStory getStoryByKey(String key) {
        if (currentConfig == null) {
            return null;
        }
        
        try {
            String auth = Base64.getEncoder().encodeToString(
                (currentConfig.getEmail() + ":" + currentConfig.getApiToken()).getBytes()
            );
            
            String url = currentConfig.getJiraUrl() + "/rest/api/3/issue/" + key + "?fields=summary,description,status,assignee";
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + auth)
                .header("Accept", "application/json")
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                org.json.JSONObject jsonResponse = new org.json.JSONObject(response.body());
                org.json.JSONObject fields = jsonResponse.getJSONObject("fields");
                
                JiraStory story = new JiraStory();
                story.setId(jsonResponse.getString("id"));
                story.setKey(jsonResponse.getString("key"));
                story.setSummary(fields.optString("summary", ""));
                
                if (fields.has("description")) {
                    story.setDescription(extractTextFromDescription(fields.opt("description")));
                }
                
                if (fields.has("status")) {
                    story.setStatus(fields.getJSONObject("status").optString("name", ""));
                }
                
                if (fields.has("assignee") && !fields.isNull("assignee")) {
                    story.setAssignee(fields.getJSONObject("assignee").optString("displayName", ""));
                }
                
                story.setProjectKey(currentConfig.getProjectKey());
                return story;
            }
            
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String extractTextFromDescription(Object description) {
        if (description == null) return "";
        
        try {
            if (description instanceof String) {
                return (String) description;
            }
            
            org.json.JSONObject descObj = (org.json.JSONObject) description;
            org.json.JSONArray content = descObj.optJSONArray("content");
            
            if (content != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < content.length(); i++) {
                    org.json.JSONObject paragraph = content.getJSONObject(i);
                    org.json.JSONArray paragraphContent = paragraph.optJSONArray("content");
                    if (paragraphContent != null) {
                        for (int j = 0; j < paragraphContent.length(); j++) {
                            org.json.JSONObject text = paragraphContent.getJSONObject(j);
                            if (text.has("text")) {
                                sb.append(text.getString("text"));
                            }
                        }
                        sb.append("\n");
                    }
                }
                return sb.toString().trim();
            }
            
            return descObj.toString();
        } catch (Exception e) {
            return description.toString();
        }
    }
}
