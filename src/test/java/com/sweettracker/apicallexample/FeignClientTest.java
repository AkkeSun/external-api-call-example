package com.sweettracker.apicallexample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.apicallexample.open_feign.TestFeignClient;
import com.sweettracker.apicallexample.test_controller.PostTestRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class FeignClientTest {

    @Autowired
    TestFeignClient testFeignClient;

    @Nested
    @DisplayName("feignClient 를 통한 GET API 호출 테스트")
    class Describe_feignClient_get {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            String username = "12345";

            // when
            String response = testFeignClient.getTest(username);

            // then
            assertThat(response).isEqualTo("hello 12345");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            //given
            String username = null;

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                testFeignClient.getTest(username));

            // then
            assertThat(exception.getMessage()).contains("에러 발생");
        }
    }


    @Nested
    @DisplayName("feignClient 를 통한 request-body POST API 호출 테스트")
    class Describe_feignClient_post_request_body {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            PostTestRequest request = PostTestRequest.builder()
                .username("test")
                .password("1234")
                .build();

            // when
            String response = testFeignClient.postTest(request);

            // then
            assertThat(response).isEqualTo("hello test");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            //given
            PostTestRequest request = PostTestRequest.builder()
                .password("1234")
                .build();

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                testFeignClient.postTest(request));

            // then
            assertThat(exception.getMessage()).contains("에러 발생");
        }
    }


    @Nested
    @DisplayName("feignClient 를 통한 form-data POST API 호출 테스트")
    class Describe_feignClient_post_form_data {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            String username = "test";
            String password = "1234";

            // when
            String response = testFeignClient.postTest2(username, password);

            // then
            assertThat(response).isEqualTo("hello test");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            // given
            String username = "test";
            String password = "";

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                testFeignClient.postTest2(username, password));

            // then
            assertThat(exception.getMessage()).contains("에러 발생");
        }
    }

    @Nested
    @DisplayName("feignClient 를 통한 multi-part POST API 호출 테스트")
    class Describe_feignClient_post_multi_part {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() throws IOException {
            // given
            InputStream inputStream = new FileInputStream("/Users/od/Desktop/test.txt");
            byte[] fileBytes = inputStream.readAllBytes();
            MultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                "text/plain",
                fileBytes
            );
            String username = "test";
            String password = "1234";

            // when
            String response = testFeignClient.postTest3(file, username, password);

            // then
            assertThat(response).isEqualTo("hello test");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() throws IOException {
            // given
            InputStream inputStream = new FileInputStream("/Users/od/Desktop/test.txt");
            byte[] fileBytes = inputStream.readAllBytes();
            MultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                "text/plain",
                fileBytes
            );
            String username = "test";
            String password = "";

            // when
            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testFeignClient.postTest3(file, username, password));

            // then
            assertThat(exception.getMessage()).contains("에러 발생");
        }
    }
}
