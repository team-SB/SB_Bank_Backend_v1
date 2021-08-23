package com.example.sbbank.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountChargeRequestDto {
    private Integer money;

    @JsonProperty("sec_password")
    private String secPassword;
}
