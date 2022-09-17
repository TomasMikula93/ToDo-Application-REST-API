package com.example.todoapp.Models.Enums;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("low"),
    NORMAL("normal"),
    HIGH("high");

    private final String priority;

    Priority(String priority) {
        this.priority = priority;
    }
}
