package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    //메인 페이지 컨트롤러

    private SessionManager sessionManager;

    @Autowired
    public MainController(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    @GetMapping("/")
    public ResponseEntity<Object> getPersonalInformation(HttpServletRequest request){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        return ResponseEntity.ok().body(data);
    }
}
