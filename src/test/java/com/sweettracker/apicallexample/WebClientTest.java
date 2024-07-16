package com.sweettracker.apicallexample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.apicallexample.test_controller.PostTestRequest;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
public class WebClientTest {

    WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080")
        .defaultHeader("key", "val")
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
        .build();

    @Nested
    @DisplayName("webClient 를 통한 GET API 호출 테스트")
    class Describe_webClient_get {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // give
            String username = "12345";

            // when
            String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/test")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(String.class) // response type
                .block();

            // then
            assertThat(response).isEqualTo("hello " + username);
        }

        @Test
        @DisplayName("[success] api 호출 중 예외 발생시 예외 처리에 성공하는지 확인한다.")
        void error() {
            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> webClient.get()
                .uri("/test")
                .retrieve()
                // 상태값에 따른 예외 처리
                .onStatus(HttpStatusCode::isError,
                    res -> res.bodyToMono(String.class).flatMap(body ->
                        Mono.error(new RuntimeException(body)))
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new RuntimeException("TimeOut"))
                .block());

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }

    @Nested
    @DisplayName("webClient 를 통한 request-body POST API 호출 테스트")
    class Describe_webClient_post_request_body {

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            PostTestRequest request = PostTestRequest.builder()
                .username("test")
                .password("1234")
                .build();

            // when
            String response = webClient.post()
                .uri("/test/request-body")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class) // response type
                .block();

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
            RuntimeException exception = assertThrows(RuntimeException.class, () -> webClient.post()
                .uri("/test/request-body")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                // 상태값에 따른 예외 처리
                .onStatus(HttpStatusCode::isError,
                    res -> res.bodyToMono(String.class).flatMap(body ->
                        Mono.error(new RuntimeException(body)))
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new RuntimeException("TimeOut"))
                .block());

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }

    @Nested
    @DisplayName("webClient 를 통한 form-data POST API 호출 테스트")
    class Describe_webClient_post_form_data {

        /*
            [ CAUTION ]
            ModelAttribute 는 @Setter 로 요청정보를 받습니다.
            @Setter 를 선언하고 싶지 않다면 ControllerAdvice @InitBinder 설정이 필요합니다.
         */

        @Test
        @DisplayName("[success] api 호출에 성공할 때 body 값을 응답하는지 확인한다.")
        void success() {
            // given
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", "test");
            formData.add("password", "1234");

            // when
            String response = webClient.post()
                .uri("/test/form-data")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class) // response type
                .block();

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
            RuntimeException exception = assertThrows(RuntimeException.class, () -> webClient.post()
                .uri("/test/form-data")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                    res -> res.bodyToMono(String.class).flatMap(body ->
                        Mono.error(new RuntimeException(body)))
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new RuntimeException("TimeOut"))
                .block());

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }

    @Nested
    @DisplayName("webClient 를 통한 multipart-form-data POST API 호출 테스트")
    class Describe_webClient_post_multi_part_form_data {

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
            String response = webClient.post()
                .uri("/test/multi-part")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(String.class) // response type
                .block();

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
            RuntimeException exception = assertThrows(RuntimeException.class, () -> webClient.post()
                .uri("/test/multi-part")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                    res -> res.bodyToMono(String.class).flatMap(body ->
                        Mono.error(new RuntimeException(body)))
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new RuntimeException("TimeOut"))
                .block());

            // then
            assertThat(exception.getMessage()).contains("사용자 이름은 필수값 입니다.");
        }
    }
}
