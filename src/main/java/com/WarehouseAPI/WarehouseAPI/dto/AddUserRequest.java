package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private InformationRequest information;


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InformationRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String role; // Should be validated in controller/service
        private String picture; // Can be null
    }
}
