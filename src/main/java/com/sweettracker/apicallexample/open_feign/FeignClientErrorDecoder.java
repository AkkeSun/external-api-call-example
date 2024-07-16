package com.sweettracker.apicallexample.open_feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private List<String> checkPathList;

    public FeignClientErrorDecoder() {
        this.checkPathList = Arrays
            .asList("/test", "/test/form-data", "/test/request-body", "/test/multi-part");
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            URI uri = new URI(response.request().url());
            String path = uri.getPath();
            if (checkPathList.contains(path)) {
                System.out.println("======================");
                System.out.println("URL: " + response.request().url());
                System.out.println("STATUS: " + response.status());
                System.out.println("======================");
                return new RuntimeException("에러 발생");
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
