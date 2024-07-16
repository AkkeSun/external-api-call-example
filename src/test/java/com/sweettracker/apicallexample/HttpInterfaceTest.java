package com.sweettracker.apicallexample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.apicallexample.http_interface.TestHttpInterface;
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
public class HttpInterfaceTest {

    @Autowired
    TestHttpInterface testHttpInterface;

    @Nested
    @DisplayName("httpInterface 를 통한 GET API 호출 테스트")
    class Describe_httpInterface_get {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            String username = "12345";

            // when
            String response = testHttpInterface.getTest(username);

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
                testHttpInterface.getTest(username));

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }


    @Nested
    @DisplayName("httpInterface 를 통한 request-body POST API 호출 테스트")
    class Describe_httpInterface_post_request_body {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            PostTestRequest request = PostTestRequest.builder()
                .username("test")
                .password("1234")
                .build();

            // when
            String response = testHttpInterface.postTest(request);

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
                testHttpInterface.postTest(request));

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }


    @Nested
    @DisplayName("httpInterface 를 통한 form-data POST API 호출 테스트")
    class Describe_httpInterface_post_form_data {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            String username = "test";
            String password = "1234";

            // when
            String response = testHttpInterface.postTest2(username, password);

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
                testHttpInterface.postTest2(username, password));

            // then
            assertThat(exception.getMessage()).contains("비밀번호는 필수값 입니다.");
        }
    }

    @Nested
    @DisplayName("httpInterface 를 통한 multi-part POST API 호출 테스트")
    class Describe_httpInterface_post_multi_part {

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
            String response = testHttpInterface.postTest3(file, username, password);

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
                () -> testHttpInterface.postTest3(file, username, password));

            // then
            assertThat(exception.getMessage()).contains("비밀번호는 필수값 입니다.");
        }
    }
}
