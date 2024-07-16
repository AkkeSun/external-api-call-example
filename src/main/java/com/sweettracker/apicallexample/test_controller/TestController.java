package com.sweettracker.apicallexample.test_controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> getTest(String username) {
        if (ObjectUtils.isEmpty(username)) {
            return ResponseEntity.badRequest().body("사용자 이름은 필수값 입니다.");
        }
        return ResponseEntity.ok("hello " + username);
    }

    @PostMapping("/test/request-body")
    public ResponseEntity<String> postTest(@RequestBody PostTestRequest request) {
        if (ObjectUtils.isEmpty(request.getUsername())) {
            return ResponseEntity.badRequest().body("사용자 이름은 필수값 입니다.");
        }
        if (ObjectUtils.isEmpty(request.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호는 필수값 입니다.");
        }
        return ResponseEntity.ok("hello " + request.getUsername());
    }

    @PostMapping("/test/form-data")
    public ResponseEntity<String> postTest2(PostTestRequest request) {
        if (ObjectUtils.isEmpty(request.getUsername())) {
            return ResponseEntity.badRequest().body("사용자 이름은 필수값 입니다.");
        }
        if (ObjectUtils.isEmpty(request.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호는 필수값 입니다.");
        }
        return ResponseEntity.ok("hello " + request.getUsername());
    }

    @PostMapping("/test/multi-part")
    public ResponseEntity<String> postTest3(
        @RequestPart MultipartFile file, PostTestRequest request) {
        System.out.println("call");
        if (ObjectUtils.isEmpty(request.getUsername())) {
            return ResponseEntity.badRequest().body("사용자 이름은 필수값 입니다.");
        }
        if (ObjectUtils.isEmpty(request.getPassword())) {
            return ResponseEntity.badRequest().body("사용자 비밀번호는 필수값 입니다.");
        }
        if (ObjectUtils.isEmpty(file)) {
            return ResponseEntity.badRequest().body("파일은 필수값 입니다.");
        }
        return ResponseEntity.ok("hello " + request.getUsername());
    }

}
