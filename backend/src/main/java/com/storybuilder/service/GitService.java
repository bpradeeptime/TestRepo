package com.storybuilder.service;

import com.storybuilder.dto.GitConfigDto;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
public class GitService {
    
    private String workingDir;
    private GitConfigDto currentConfig;
    
    public void setConfig(GitConfigDto config) {
        this.currentConfig = config;
    }
    
    public GitConfigDto getConfig() {
        return currentConfig;
    }
    
    public String getWorkingDir() {
        return workingDir;
    }
    
    public String cloneRepository(String repoUrl) throws Exception {
        workingDir = System.getProperty("java.io.tmpdir") + "/story-builder-" + UUID.randomUUID().toString().substring(0, 8);
        Files.createDirectories(Paths.get(workingDir));
        
        ProcessBuilder pb = new ProcessBuilder("git", "clone", repoUrl, workingDir);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git clone failed: " + output);
        }
        
        return workingDir;
    }
    
    public String createBranch(String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "-C", workingDir, "checkout", "-b", branchName);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git branch creation failed: " + output);
        }
        
        return branchName;
    }
    
    public String commit(String message) throws Exception {
        ProcessBuilder addPb = new ProcessBuilder("git", "-C", workingDir, "add", "-A");
        addPb.redirectErrorStream(true);
        Process addProcess = addPb.start();
        addProcess.waitFor();
        
        ProcessBuilder pb = new ProcessBuilder("git", "-C", workingDir, "commit", "-m", message);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git commit failed: " + output);
        }
        
        return output.toString();
    }
    
    public String push(String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "-C", workingDir, "push", "-u", "origin", branchName);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git push failed: " + output);
        }
        
        return output.toString();
    }
    
    public String getDiff() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "-C", workingDir, "diff", "--staged");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        process.waitFor();
        return output.toString();
    }
    
    public void writeFile(String relativePath, String content) throws Exception {
        Path filePath = Paths.get(workingDir, relativePath);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content);
    }
    
    public String readFile(String relativePath) throws Exception {
        Path filePath = Paths.get(workingDir, relativePath);
        return Files.readString(filePath);
    }
    
    public String getCurrentBranch() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "-C", workingDir, "rev-parse", "--abbrev-ref", "HEAD");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }
        
        process.waitFor();
        return output.toString().trim();
    }
}
