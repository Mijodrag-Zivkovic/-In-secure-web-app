package com.News.News.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean isEnabled;


}