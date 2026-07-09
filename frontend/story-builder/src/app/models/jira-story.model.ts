export interface JiraStory {
  id: string;
  key: string;
  summary: string;
  description: string;
  status: string;
  assignee: string;
  projectKey: string;
}
