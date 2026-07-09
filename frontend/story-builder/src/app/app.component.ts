import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JiraService, JiraConfig } from './services/jira.service';
import { WorkflowService, GitConfig } from './services/workflow.service';
import { JiraStory } from './models/jira-story.model';
import { WorkflowStatus, FileChange } from './models/workflow-status.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  // Config
  jiraConfig: JiraConfig = {
    jiraUrl: '',
    email: '',
    apiToken: '',
    projectKey: ''
  };
  
  gitConfig: GitConfig = {
    repoUrl: '',
    branch: 'master',
    commitMessage: ''
  };
  
  // State
  currentView: 'config' | 'stories' | 'workflow' = 'config';
  stories: JiraStory[] = [];
  selectedStory: JiraStory | null = null;
  workflowStatus: WorkflowStatus | null = null;
  fileChanges: FileChange[] = [];
  progressMessages: string[] = [];
  configSaved = false;
  
  // Feedback
  commitFeedback = '';
  showConfigForm = false;
  
  constructor(
    private jiraService: JiraService,
    private workflowService: WorkflowService
  ) {}
  
  ngOnInit(): void {
    this.loadConfigs();
  }
  
  ngOnDestroy(): void {
    this.workflowService.disconnect();
  }
  
  loadConfigs(): void {
    this.jiraService.getConfig().subscribe({
      next: (config) => {
        if (config.jiraUrl) {
          this.jiraConfig = config;
          this.loadStories();
        }
      },
      error: () => {}
    });
    
    this.workflowService.getGitConfig().subscribe({
      next: (config) => {
        if (config.repoUrl) {
          this.gitConfig = config;
        }
      },
      error: () => {}
    });
  }
  
  saveConfigs(): void {
    this.jiraService.setConfig(this.jiraConfig).subscribe();
    this.workflowService.setGitConfig(this.gitConfig).subscribe({
      next: () => {
        this.configSaved = true;
        this.showConfigForm = false;
        this.loadStories();
      }
    });
  }
  
  loadStories(): void {
    if (!this.jiraConfig.jiraUrl) return;
    this.jiraService.getStories().subscribe({
      next: (stories) => {
        this.stories = stories;
        this.currentView = 'stories';
      },
      error: (err) => {
        console.error('Error loading stories:', err);
        alert('Failed to load stories. Check your Jira configuration.');
      }
    });
  }
  
  selectStory(story: JiraStory): void {
    this.selectedStory = story;
    this.workflowStatus = null;
    this.fileChanges = [];
    this.progressMessages = [];
    this.currentView = 'workflow';
  }
  
  startWorkflow(): void {
    if (!this.selectedStory || !this.gitConfig.repoUrl) return;
    
    this.workflowService.startWorkflow(
      this.selectedStory.key,
      this.selectedStory.summary,
      this.selectedStory.description,
      this.gitConfig.repoUrl
    ).subscribe({
      next: (status) => {
        this.workflowStatus = status;
        this.connectWebSocket();
      }
    });
  }
  
  connectWebSocket(): void {
    this.workflowService.connect((message) => {
      if (message.type === 'progress') {
        this.progressMessages.push(message.data.message);
      } else if (message.type === 'file-change') {
        this.fileChanges.push(message.data);
      } else {
        this.workflowStatus = message;
      }
    });
  }
  
  approvePlan(): void {
    this.workflowService.approvePlan().subscribe({
      next: (status) => this.workflowStatus = status
    });
  }
  
  approveCommit(): void {
    this.workflowService.approveCommit().subscribe({
      next: (status) => this.workflowStatus = status
    });
  }
  
  rejectCommit(): void {
    if (!this.commitFeedback) return;
    this.workflowService.rejectCommit(this.commitFeedback).subscribe({
      next: (status) => {
        this.workflowStatus = status;
        this.commitFeedback = '';
      }
    });
  }
  
  reset(): void {
    this.workflowService.reset().subscribe();
    this.selectedStory = null;
    this.workflowStatus = null;
    this.fileChanges = [];
    this.progressMessages = [];
    this.currentView = 'stories';
  }
  
  goBack(): void {
    if (this.currentView === 'workflow' && this.workflowStatus?.currentStep === 'COMPLETED') {
      this.reset();
    } else {
      this.currentView = 'stories';
      this.selectedStory = null;
    }
  }
  
  isAwaitingPlanApproval(): boolean {
    return !!(this.workflowStatus?.awaitingApproval && 
           this.workflowStatus?.currentStep === 'AWAITING_PLAN_APPROVAL');
  }
  
  isAwaitingCommitApproval(): boolean {
    return !!(this.workflowStatus?.awaitingApproval && 
           this.workflowStatus?.currentStep === 'AWAITING_COMMIT_APPROVAL');
  }
  
  getStepIcon(step: string): string {
    const icons: { [key: string]: string } = {
      'IDLE': '⏸️',
      'CLONING': '📥',
      'ANALYZING': '🔍',
      'GENERATING_PLAN': '📝',
      'AWAITING_PLAN_APPROVAL': '⏳',
      'CODING': '💻',
      'STREAMING_CHANGES': '📝',
      'RUNNING_TESTS': '🧪',
      'SHOWING_RESULTS': '📊',
      'FIXING_FAILURES': '🔧',
      'SHOWING_DIFF': '📄',
      'AWAITING_COMMIT_APPROVAL': '⏳',
      'COMMITTING': '✅',
      'PUSHING': '⬆️',
      'CREATING_PR': '🔀',
      'COMPLETED': '🎉'
    };
    return icons[step] || '➡️';
  }
  
  getStatusColor(step: string): string {
    if (step === 'COMPLETED') return '#10b981';
    if (step === 'AWAITING_PLAN_APPROVAL' || step === 'AWAITING_COMMIT_APPROVAL') return '#f59e0b';
    if (step.startsWith('ERROR') || step === 'IDLE') return '#ef4444';
    return '#3b82f6';
  }
  
  formatStepName(step: string): string {
    return step.replace(/_/g, ' ');
  }
}
