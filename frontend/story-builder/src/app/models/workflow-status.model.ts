export interface WorkflowStatus {
  currentStep: string;
  message: string;
  awaitingApproval: boolean;
  storyId: string;
  filesLikelyToChange: string[];
  proposedApproach: string;
  estimatedImpact: string;
  branchName: string;
  testResults: string[];
  gitDiff: string;
}

export interface FileChange {
  filename: string;
  content: string;
}

export interface ProgressMessage {
  type: string;
  message: string;
}
