package com.revature.Model;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.utils.BaseClass;

@Entity(tableName = "tasks")
public class Tasc extends BaseClass {

    @Column(columnName = "task")
    private String task;
    @Column(columnName = "description")
    private String description;
    @Column(columnName = "priority")
    private int priority;
//    @Column(columnName = "about")
//    private String notes;
    @Column(columnName = "completed")
    private boolean completed;
//    @ForeignKey(colName = "user_id", refClass = User.class)
//    private User user;

    public Tasc() {
    }

    public Tasc(String task, String description, int priority, boolean completed) {
        this.task = task;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
