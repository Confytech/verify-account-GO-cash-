package com.example.demo.controller;

import com.example.demo.entity.AccountData;
import com.example.demo.entity.ApiResponse;
import com.example.demo.service.AccountService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/")
public class AccountVerification {
    private final AccountService accountService;
    public AccountVerification(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/accountVerify/")
    public ResponseEntity<ApiResponse> bankVerification(@RequestBody AccountData accountData){
    ApiResponse apiResponse = accountService.bankVerification(accountData);

        if (apiResponse != null) {
            return ResponseEntity.ok(apiResponse);
        } else {
            // Handle error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
