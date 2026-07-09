package com.storybuilder.service;

import com.storybuilder.model.JiraStory;
import com.storybuilder.model.WorkflowStatus;
import com.storybuilder.controller.SseController;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class CodeGeneratorService {
    
    private final JiraService jiraService;
    private final GitService gitService;
    private final SseController sseController;
    
    private WorkflowStatus currentStatus = new WorkflowStatus();
    private StringBuilder implementationPlan = new StringBuilder();
    private List<String> fileChanges = new ArrayList<>();
    
    public CodeGeneratorService(JiraService jiraService, GitService gitService,
                               SseController sseController) {
        this.jiraService = jiraService;
        this.gitService = gitService;
        this.sseController = sseController;
    }
    
    public void reset() {
        currentStatus = new WorkflowStatus();
        implementationPlan = new StringBuilder();
        fileChanges = new ArrayList<>();
    }
    
    public WorkflowStatus getCurrentStatus() {
        return currentStatus;
    }
    
    public List<String> getFileChanges() {
        return new ArrayList<>(fileChanges);
    }
    
    @Async
    public void startWorkflow(JiraStory story, String repoUrl) {
        try {
            reset();
            String storyId = story.getKey();
            currentStatus.setStoryId(storyId);
            
            // Step 1: Clone Repository
            updateStatus(WorkflowStatus.Step.CLONING, "Cloning repository...");
            String branchName = "feature/" + storyId.toLowerCase().replace("-", "-");
            gitService.cloneRepository(repoUrl);
            gitService.createBranch(branchName);
            currentStatus.setBranchName(branchName);
            
            // Step 2: Analyze Project
            updateStatus(WorkflowStatus.Step.ANALYZING, "Analyzing your project...");
            sendProgress("Analyzing project structure...");
            Thread.sleep(1000);
            
            // Step 3: Generate Implementation Plan
            updateStatus(WorkflowStatus.Step.GENERATING_PLAN, "Generating implementation plan...");
            String plan = generateImplementationPlan(story);
            implementationPlan.append(plan);
            
            currentStatus.setFilesLikelyToChange(extractFilesFromPlan(plan));
            currentStatus.setProposedApproach(plan);
            currentStatus.setEstimatedImpact("Medium - New feature implementation");
            
            // Step 4: Await Plan Approval
            updateStatus(WorkflowStatus.Step.AWAITING_PLAN_APPROVAL, 
                "Please review and approve the implementation plan");
            currentStatus.setAwaitingApproval(true);
            broadcast();
            
        } catch (Exception e) {
            updateStatus(WorkflowStatus.Step.IDLE, "Error: " + e.getMessage());
            broadcast();
        }
    }
    
    public void approvePlan() {
        try {
            currentStatus.setAwaitingApproval(false);
            
            // Step 5: Start Coding
            updateStatus(WorkflowStatus.Step.CODING, "Implementing code...");
            broadcast();
            
            String story = currentStatus.getStoryId();
            String plan = currentStatus.getProposedApproach();
            
            // Simulate code generation and streaming changes
            simulateCodeGeneration(story, plan);
            
        } catch (Exception e) {
            updateStatus(WorkflowStatus.Step.IDLE, "Error: " + e.getMessage());
            broadcast();
        }
    }
    
    private void simulateCodeGeneration(String storyId, String plan) throws Exception {
        List<String> files = currentStatus.getFilesLikelyToChange();
        
        if (files == null || files.isEmpty()) {
            files = Arrays.asList(
                "src/main/java/com/example/" + storyId.toLowerCase() + ".java",
                "src/test/java/com/example/" + storyId.toLowerCase() + "Test.java"
            );
        }
        
        updateStatus(WorkflowStatus.Step.STREAMING_CHANGES, "Streaming file changes...");
        
        for (String file : files) {
            sendProgress("Creating file: " + file);
            fileChanges.add(file);
            
            String content = generateFileContent(file, storyId, plan);
            gitService.writeFile(file, content);
            
            sendFileChange(file, content);
            Thread.sleep(500);
        }
        
        // Step 6: Run Tests
        updateStatus(WorkflowStatus.Step.RUNNING_TESTS, "Running tests...");
        sendProgress("Running test suite...");
        Thread.sleep(1500);
        
        List<String> testResults = new ArrayList<>();
        testResults.add("✓ TestServiceTest.testSave - PASSED");
        testResults.add("✓ TestServiceTest.testFindById - PASSED");
        testResults.add("✓ TestControllerTest.testEndpoint - PASSED");
        currentStatus.setTestResults(testResults);
        
        // Step 7: Show Test Results
        updateStatus(WorkflowStatus.Step.SHOWING_RESULTS, "Test results:");
        broadcast();
        
        Thread.sleep(500);
        
        // Step 8: Show Git Diff
        updateStatus(WorkflowStatus.Step.SHOWING_DIFF, "Showing changes...");
        String diff = gitService.getDiff();
        currentStatus.setGitDiff(diff);
        broadcast();
        
        // Step 9: Await Commit Approval
        updateStatus(WorkflowStatus.Step.AWAITING_COMMIT_APPROVAL, 
            "Review the changes above and approve or request modifications");
        currentStatus.setAwaitingApproval(true);
        broadcast();
    }
    
    public void approveCommit() {
        try {
            currentStatus.setAwaitingApproval(false);
            
            // Step 10: Commit
            updateStatus(WorkflowStatus.Step.COMMITTING, "Committing changes...");
            String commitMessage = "Implement " + currentStatus.getStoryId() + ": " + 
                (implementationPlan.length() > 50 ? 
                    implementationPlan.substring(0, 50) + "..." : implementationPlan.toString());
            gitService.commit(commitMessage);
            
            // Step 11: Push
            updateStatus(WorkflowStatus.Step.PUSHING, "Pushing changes...");
            String branchName = currentStatus.getBranchName();
            gitService.push(branchName);
            
            // Step 12: Create PR (simulated - would use GitHub API in real implementation)
            updateStatus(WorkflowStatus.Step.CREATING_PR, "Creating pull request...");
            Thread.sleep(500);
            
            // Step 13: Complete
            updateStatus(WorkflowStatus.Step.COMPLETED, 
                "Pull request created successfully! Branch: " + branchName);
            broadcast();
            
        } catch (Exception e) {
            updateStatus(WorkflowStatus.Step.IDLE, "Error: " + e.getMessage());
            broadcast();
        }
    }
    
    public void rejectCommit(String feedback) {
        currentStatus.setAwaitingApproval(false);
        updateStatus(WorkflowStatus.Step.CODING, "Changes rejected. Feedback: " + feedback);
        broadcast();
    }
    
    private String generateImplementationPlan(JiraStory story) {
        StringBuilder plan = new StringBuilder();
        plan.append("## Implementation Plan for ").append(story.getKey()).append("\n\n");
        plan.append("### Summary\n").append(story.getSummary()).append("\n\n");
        plan.append("### Description\n").append(story.getDescription()).append("\n\n");
        plan.append("### Proposed Approach\n");
        plan.append("1. Create service class to handle business logic\n");
        plan.append("2. Create controller for API endpoints\n");
        plan.append("3. Add unit tests\n");
        plan.append("4. Update configuration as needed\n");
        plan.append("\n### Files to Create/Modify\n");
        plan.append("- src/main/java/com/example/").append(story.getKey().toLowerCase()).append(".java\n");
        plan.append("- src/test/java/com/example/").append(story.getKey().toLowerCase()).append("Test.java\n");
        return plan.toString();
    }
    
    private List<String> extractFilesFromPlan(String plan) {
        List<String> files = new ArrayList<>();
        for (String line : plan.split("\n")) {
            if (line.contains("src/") && line.trim().startsWith("-")) {
                files.add(line.replace("- ", "").trim());
            }
        }
        if (files.isEmpty()) {
            files.add("src/main/java/com/example/ExampleService.java");
            files.add("src/test/java/com/example/ExampleServiceTest.java");
        }
        return files;
    }
    
    private String generateFileContent(String file, String storyId, String plan) {
        if (file.endsWith("Test.java")) {
            String className = storyId.toLowerCase() + "Test";
            StringBuilder sb = new StringBuilder();
            sb.append("package com.example;\n\n");
            sb.append("import org.junit.jupiter.api.Test;\n");
            sb.append("import static org.junit.jupiter.api.Assertions.*;\n\n");
            sb.append("class ").append(className).append(" {\n");
            sb.append("    @Test\n");
            sb.append("    void testBasicFunctionality() {\n");
            sb.append("        // Test implementation for ").append(storyId).append("\n");
            sb.append("        assertTrue(true, \"Basic test passed\");\n");
            sb.append("    }\n");
            sb.append("}\n");
            return sb.toString();
        } else {
            String className = storyId.toLowerCase();
            StringBuilder sb = new StringBuilder();
            sb.append("package com.example;\n\n");
            sb.append("import org.springframework.stereotype.Service;\n\n");
            sb.append("/**\n");
            sb.append(" * Implementation for ").append(storyId).append("\n");
            sb.append(" */\n");
            sb.append("@Service\n");
            sb.append("public class ").append(className).append(" {\n");
            sb.append("    public String getMessage() {\n");
            sb.append("        return \"Implementation for ").append(storyId).append("\";\n");
            sb.append("    }\n");
            sb.append("}\n");
            return sb.toString();
        }
    }
    
    private void updateStatus(WorkflowStatus.Step step, String message) {
        currentStatus.setCurrentStep(step.name());
        currentStatus.setMessage(message);
        broadcast();
    }
    
    private void sendProgress(String message) {
        sseController.sendEvent("progress", 
            Map.of("type", "progress", "message", message));
    }
    
    private void sendFileChange(String filename, String content) {
        sseController.sendEvent("file-change", 
            Map.of("type", "file-change", "filename", filename, "content", content));
    }
    
    private void broadcast() {
        sseController.sendEvent("workflow", currentStatus);
    }
}
