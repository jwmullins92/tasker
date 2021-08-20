package com.revature.Model;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.utils.BaseClass;

@Entity(tableName = "to_do")
public class ToDo extends BaseClass {

    @Column(columnName = "task")
    private String task;
    @Column(columnName = "priority_level")
    private int priority;
    @Column(columnName = "completed")
    private boolean completed;

    public ToDo() {
    }

    public ToDo(String task, int priority, boolean completed) {
        this.task = task;
        this.priority = priority;
        this.completed = completed;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
