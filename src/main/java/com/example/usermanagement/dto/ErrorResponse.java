package com.example.usermanagement.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    
    private String message;
    private int status;
    private String error;
    private String path;
    private LocalDateTime timeStamp;
    
    // get value from construcutor
    public ErrorResponse(int status, String error, String message, String Path) {
        this.path = Path;
        this.message = message;
        this.status = status;
        this.error = error;
        this.timeStamp = LocalDateTime.now();
    }

    // getter and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

}
