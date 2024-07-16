package com.sweettracker.apicallexample.http_interface;

import com.sweettracker.apicallexample.test_controller.PostTestRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/test")
public interface TestHttpInterface {

    @GetExchange
    String getTest(@RequestParam(required = false) String username);

    @PostExchange("/request-body")
    String postTest(@RequestBody PostTestRequest request);

    @PostExchange("/form-data")
    String postTest2(
        @RequestParam String username,
        @RequestParam String password
    );

    @PostExchange(value = "/multi-part", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    String postTest3(
        @RequestPart MultipartFile file,
        @RequestParam String username,
        @RequestParam String password
    );
}
