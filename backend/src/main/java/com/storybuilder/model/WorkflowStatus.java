package com.storybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStatus {
    private String currentStep;
    private String message;
    private boolean awaitingApproval;
    private String storyId;
    private List<String> filesLikelyToChange;
    private String proposedApproach;
    private String estimatedImpact;
    private String branchName;
    private List<String> testResults;
    private String gitDiff;
    
    public enum Step {
        IDLE("Idle"),
        CLONING("Cloning Repository"),
        ANALYZING("Analyzing Project"),
        GENERATING_PLAN("Generating Implementation Plan"),
        AWAITING_PLAN_APPROVAL("Awaiting Plan Approval"),
        CODING("Implementing Code"),
        STREAMING_CHANGES("Streaming File Changes"),
        RUNNING_TESTS("Running Tests"),
        SHOWING_RESULTS("Showing Test Results"),
        FIXING_FAILURES("Fixing Test Failures"),
        SHOWING_DIFF("Showing Git Diff"),
        AWAITING_COMMIT_APPROVAL("Awaiting Commit Approval"),
        COMMITTING("Committing Changes"),
        PUSHING("Pushing Changes"),
        CREATING_PR("Creating Pull Request"),
        COMPLETED("Completed");
        
        private final String displayName;
        
        Step(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
