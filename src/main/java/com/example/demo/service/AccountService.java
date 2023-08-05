package com.example.demo.service;

import com.example.demo.entities.AccountData;
import com.example.demo.entities.ApiRequest;
import com.example.demo.entities.ApiResponse;
import com.example.demo.entities.CustomApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CustomApiResponse bankVerification(ApiRequest apiRequest) {
        String authToken = "FLWSECK_TEST-d9dcac59b5401ba176cab3596e261d24-X";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<ApiRequest> apiRequestHttpEntity = new HttpEntity<>(apiRequest, headers);

        try {
            ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
                    "https://api.flutterwave.com/v3/accounts/resolve",
                    apiRequestHttpEntity, ApiResponse.class);

            System.out.println("API Response Body: " + responseEntity.getBody());

            ApiResponse response = responseEntity.getBody();

            if ("success".equals(response.getStatus())) {
                AccountData accountData = response.getData();
                if (accountData != null && accountData.getAccount_name() != null) {
                    CustomApiResponse customResponse = new CustomApiResponse("success", "Account details fetched");
                    customResponse.setStatus("success");
                    customResponse.setMessage("Account details fetched");
                    customResponse.setAccountNumber(accountData.getAccount_number());
                    customResponse.setAccountOwner(accountData.getAccount_name());
                    return customResponse;
                } else {
                    return new CustomApiResponse("error", "Account owner not available");
                }
            } else if ("error".equals(response.getStatus())) {
                if (response.getMessage().contains("invalid account")) {
                    return new CustomApiResponse("error", "Sorry, that account number is invalid, please check and try again");
                }
            } else if ("error".equals(response.getStatus())) {
                String errorMessage = response.getMessage();
                if (errorMessage.contains("invalid account")) {
                    return new CustomApiResponse("error", "Sorry, that account number is invalid, please check and try again");
                } else {
                    return new CustomApiResponse("error", errorMessage);
                }
            } else {
                return new CustomApiResponse("error", response.getMessage());
            }
        } catch (HttpClientErrorException e) {
            return new CustomApiResponse("error", e.getMessage());
        } catch (RestClientException e) {
            return new CustomApiResponse("error", "Failed to communicate with the API");
        }
        return new CustomApiResponse("error", "Sorry, that account number is invalid, please check and try again");
    }
}

