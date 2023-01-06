package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.api.WKUAPI;
import kr.mos1981.mosweb.dto.SignInDTO;
import kr.mos1981.mosweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController {
    //로그인 페이지 컨트롤러

    private final SessionManager sessionManager;
    private final MemberService memberService;

    @Autowired
    public LoginController(SessionManager sessionManager,
                           MemberService memberService){
        this.sessionManager = sessionManager;
        this.memberService = memberService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> processSignIn(HttpServletResponse response,HttpServletRequest request, SignInDTO dto) throws IOException {
        if(sessionManager.getSession(request) != null) return ResponseEntityEnum.ALREADY_LOGIN.getMsg();
        String[] data = WKUAPI.getRegistrationInformation(dto);
        if(data[0] == null) return ResponseEntityEnum.INCORRECT_USER_INFO.getMsg();
        int permission = memberService.getPermissionLevel(data[0], data[2]);
        if(permission == 2) return ResponseEntityEnum.NOT_MEMBER.getMsg();
        sessionManager.createSession(data, response);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> processLogout(HttpServletRequest request){
        String[] data = (String[])sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        sessionManager.expire(request);
        return ResponseEntity.noContent().build();
    }
}
