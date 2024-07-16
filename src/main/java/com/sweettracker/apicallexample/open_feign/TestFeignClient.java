package com.sweettracker.apicallexample.open_feign;

import com.sweettracker.apicallexample.test_controller.PostTestRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "test", url = "http://localhost:8080")
public interface TestFeignClient {

    @GetMapping("/test")
    String getTest(@RequestParam String username);

    @PostMapping("/test/request-body")
    String postTest(@RequestBody PostTestRequest request);

    /*
        feignClient 는 기본적으로 json 직렬화를 하기 때문이
        PostMapping formData 를 전송하려면 각각의 파라미터를 모두 지정해주어야한다
    */
    @PostMapping("/test/form-data")
    String postTest2(
        @RequestParam String username,
        @RequestParam String password
    );

    @PostMapping(value = "/test/multi-part", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String postTest3(
        @RequestPart MultipartFile file,
        @RequestParam String username,
        @RequestParam String password
    );
}
