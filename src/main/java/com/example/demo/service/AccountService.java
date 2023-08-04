package com.example.demo.service;

import com.example.demo.entity.AccountData;
import com.example.demo.entity.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    private final RestTemplate restTemplate;

    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponse bankVerification(AccountData accountData) {
        String authToken = "FLWSECK_TEST-d9dcac59b5401ba176cab3596e261d24-X";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);
        headers.setBearerAuth(authToken);

        HttpEntity<AccountData> apiRequestHttpEntity = new HttpEntity<>(accountData, headers);
        ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
                "https://api.flutterwave.com/v3/accounts/resolve",
                apiRequestHttpEntity,
                ApiResponse.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            // Handle error response or return a default error ApiResponse
            return new ApiResponse("error", "Failed to verify account", null);
        }
    }
}