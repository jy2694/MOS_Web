package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.api.WKUAPI;
import kr.mos1981.mosweb.dto.SignInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class LoginController {
    //로그인 페이지 컨트롤러

    private SessionManager sessionManager;

    @Autowired
    public LoginController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> processSignIn(HttpServletResponse response, @RequestBody Map<String, Object> map) throws IOException {
      System.out.println(map.get("userId") + " && " + map.get("userPw"));
        String[] data = WKUAPI.getRegistrationInformation(new SignInDTO(map.get("userId").toString(), map.get("userPw").toString()));
        if(data[0] == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 회원정보가 일치하지 않습니다.");
        }
        sessionManager.createSession(data, response);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> processLogout(HttpServletRequest request){
        String[] data = (String[])sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        sessionManager.expire(request);
        return ResponseEntity.ok().body("LOGOUT");
    }
}
