package com.github.accessreport.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;
@Data
@Getter

public class AccessReport {

    private String organization;
    private int totalRepositories;
    private int totalUsers;
    private List<UserAccess> users;

    public AccessReport(String organization, int totalRepositories, int totalUsers, List<UserAccess> users) {
        this.organization = organization;
        this.totalRepositories = totalRepositories;
        this.totalUsers = totalUsers;
        this.users = users;
    }

}
