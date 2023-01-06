package kr.mos1981.mosweb.api;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public enum ResponseEntityEnum {

    LOGIN_REQUIRED(ResponseEntity.status(401).body("ERROR : Login Required")),
    ALREADY_LOGIN(ResponseEntity.status(403).body("ERROR : Already Login")),
    NOT_MEMBER(ResponseEntity.status(403).body("ERROR : Not member")),
    NO_PERMISSION(ResponseEntity.status(403).body("ERROR : permission denied")),
    NOT_EXIST(ResponseEntity.status(410).body("ERROR : Not exist")),
    NOT_FOUND(ResponseEntity.status(404).body("ERROR : Not found")),
    INCORRECT_USER_INFO(ResponseEntity.status(401).body("ERROR : incorrect user ID or PW")),
    INTERNAL_SERVER_ERROR(ResponseEntity.status(401).body("ERROR : Internal Server Error"));

    private final ResponseEntity<Object> msg;

    ResponseEntityEnum(ResponseEntity<Object> msg){
        this.msg = msg;
    }
}
