package com.revature.Model;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.utils.BaseClass;

@Entity(tableName = "users")
public class User extends BaseClass {

    @Column(columnName = "first_name")
    private String firstName;
    @Column(columnName = "last_name")
    private String lastName;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
