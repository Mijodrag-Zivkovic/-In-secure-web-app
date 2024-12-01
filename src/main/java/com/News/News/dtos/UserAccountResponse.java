package com.News.News.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean isEnabled;


}