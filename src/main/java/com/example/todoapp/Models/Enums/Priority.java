package com.example.todoapp.Models.Enums;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("Low"),
    NORMAL("Normal"),
    HIGH("High");

    private final String priority;

    Priority(String priority) {
        this.priority = priority;
    }
}
