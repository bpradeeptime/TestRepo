import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JiraStory } from '../models/jira-story.model';

export interface JiraConfig {
  jiraUrl: string;
  email: string;
  apiToken: string;
  projectKey: string;
}

@Injectable({
  providedIn: 'root'
})
export class JiraService {
  private apiUrl = 'http://localhost:8080/api/jira';

  constructor(private http: HttpClient) {}

  setConfig(config: JiraConfig): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/config`, config);
  }

  getConfig(): Observable<JiraConfig> {
    return this.http.get<JiraConfig>(`${this.apiUrl}/config`);
  }

  getStories(): Observable<JiraStory[]> {
    return this.http.get<JiraStory[]>(`${this.apiUrl}/stories`);
  }

  getStory(key: string): Observable<JiraStory> {
    return this.http.get<JiraStory>(`${this.apiUrl}/stories/${key}`);
  }
}
