import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkflowStatus, FileChange, ProgressMessage } from '../models/workflow-status.model';

export interface GitConfig {
  repoUrl: string;
  branch: string;
  commitMessage: string;
}

@Injectable({
  providedIn: 'root'
})
export class WorkflowService implements OnDestroy {
  private apiUrl = 'http://localhost:8080/api/workflow';
  private sseUrl = 'http://localhost:8080/api/events/stream';
  private eventSource: EventSource | null = null;

  constructor(private http: HttpClient) {}

  setGitConfig(config: GitConfig): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/git-config`, config);
  }

  getGitConfig(): Observable<GitConfig> {
    return this.http.get<GitConfig>(`${this.apiUrl}/git-config`);
  }

  startWorkflow(storyKey: string, summary: string, description: string, repoUrl: string): Observable<WorkflowStatus> {
    return this.http.post<WorkflowStatus>(`${this.apiUrl}/start`, {
      storyKey,
      summary,
      description,
      repoUrl
    });
  }

  getStatus(): Observable<WorkflowStatus> {
    return this.http.get<WorkflowStatus>(`${this.apiUrl}/status`);
  }

  getFileChanges(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/changes`);
  }

  approvePlan(): Observable<WorkflowStatus> {
    return this.http.post<WorkflowStatus>(`${this.apiUrl}/approve-plan`, {});
  }

  approveCommit(): Observable<WorkflowStatus> {
    return this.http.post<WorkflowStatus>(`${this.apiUrl}/approve-commit`, {});
  }

  rejectCommit(feedback: string): Observable<WorkflowStatus> {
    return this.http.post<WorkflowStatus>(`${this.apiUrl}/reject-commit`, { feedback });
  }

  reset(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/reset`, {});
  }

  connect(onMessage: (message: any) => void): void {
    if (this.eventSource) {
      return;
    }

    this.eventSource = new EventSource(this.sseUrl);

    this.eventSource.addEventListener('workflow', (event) => {
      onMessage(JSON.parse(event.data));
    });

    this.eventSource.addEventListener('progress', (event) => {
      onMessage({ type: 'progress', data: JSON.parse(event.data) });
    });

    this.eventSource.addEventListener('file-change', (event) => {
      onMessage({ type: 'file-change', data: JSON.parse(event.data) });
    });

    this.eventSource.onerror = (error) => {
      console.error('SSE Error:', error);
    };
  }

  disconnect(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
