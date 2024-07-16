package com.sweettracker.apicallexample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.apicallexample.test_controller.PostTestRequest;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class RestClientTest {

    ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
        .withConnectTimeout(Duration.ofSeconds(1))
        .withReadTimeout(Duration.ofSeconds(2));

    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:8080")
        .requestFactory(ClientHttpRequestFactories.get(settings))
        .defaultHeader("key", "val")
        .build();


    @Nested
    @DisplayName("restClient 를 통한 GET API 호출 테스트")
    class Describe_restClient_get {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            String username = "12345";

            // when
            String response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/test")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .body(String.class); // response type

            // then
            assertThat(response).isEqualTo("hello 12345");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> restClient.get()
                .uri("/test")
                .retrieve()
                // 상태값에 따른 예외 처리
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("예외 발생");
                })
                .body(String.class));

            // then
            assertThat(exception.getMessage()).contains("예외 발생");
        }
    }

    @Nested
    @DisplayName("restClient 를 통한 request-body POST API 호출 테스트")
    class Describe_restClient_post_request_body {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            PostTestRequest request = PostTestRequest.builder()
                .username("test")
                .password("1234")
                .build();

            // when
            String response = restClient.post()
                .uri("/test/request-body")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class); // response type

            // then
            assertThat(response).isEqualTo("hello " + request.getUsername());
        }


        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            // given
            PostTestRequest request = PostTestRequest.builder()
                .password("1234")
                .build();

            // when
            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restClient.post()
                    .uri("/test/request-body")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new RuntimeException("예외 발생");
                    })
                    .body(String.class)); // response type

            // then
            assertThat(exception.getMessage()).isEqualTo("예외 발생");
        }
    }

    @Nested
    @DisplayName("restClient 를 통한 form-data POST API 호출 테스트")
    class Describe_restClient_post_form_data {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", "test");
            formData.add("password", "1234");

            // when
            String response = restClient.post()
                .uri("/test/form-data")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class); // response type

            // then
            assertThat(response).isEqualTo("hello test");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            // given
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("password", "1234");

            // when
            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restClient.post()
                    .uri("/test/form-data")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new RuntimeException("오류 발생");
                    })
                    .body(String.class));

            // then
            assertThat(exception.getMessage()).contains("오류 발생");
        }
    }

    @Nested
    @DisplayName("restClient 를 통한 multipart-form-data POST API 호출 테스트")
    class Describe_restClient_post_multi_part_form_data {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            FileSystemResource fileResource = new FileSystemResource("/Users/od/Desktop/test.txt");
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("username", "test");
            formData.add("password", "test");
            formData.add("file", fileResource);

            // when
            String response = restClient.post()
                .uri("/test/multi-part")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(formData)
                .retrieve()
                .body(String.class);

            // then
            assertThat(response).isEqualTo("hello test");
        }

        @Test
        @DisplayName("[error] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            // given
            FileSystemResource fileResource = new FileSystemResource("/Users/od/Desktop/test.txt");
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("password", "test");
            formData.add("file", fileResource);

            // when
            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restClient.post()
                    .uri("/test/multi-part")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new RuntimeException("오류 발생");
                    })
                    .body(String.class));

            // then
            assertThat(exception.getMessage()).contains("오류 발생");
        }
    }
}
