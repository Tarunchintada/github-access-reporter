package com.github.accessreport.model;

import java.util.List;

// represents which repositories a user has access to
public record UserAccess(String username, List<String> repositories) {

}