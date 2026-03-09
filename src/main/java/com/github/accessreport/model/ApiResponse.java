package com.github.accessreport.model;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String status;
    private final String timestamp;
    private final T data;

    public ApiResponse(String status,String timestamp,T data){
        this.status = status;
        this.timestamp = timestamp;
        this.data = data;
    }

}