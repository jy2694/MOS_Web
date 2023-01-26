package kr.mos1981.mosweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInDTO {
    private String userId;
    private String userPw;
}
