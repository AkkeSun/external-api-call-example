package com.sweettracker.apicallexample.test_controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PostTestRequest {

    private String username;
    private String password;

    @Builder
    public PostTestRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
