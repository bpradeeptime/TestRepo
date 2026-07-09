package com.storybuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitConfigDto {
    private String repoUrl;
    private String branch;
    private String commitMessage;
}
