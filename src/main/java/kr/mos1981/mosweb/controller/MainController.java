package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.entity.Member;
import kr.mos1981.mosweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    //메인 페이지 컨트롤러

    private SessionManager sessionManager;
    private MemberService memberService;


    @Autowired
    public MainController(SessionManager sessionManager,
                          MemberService memberService){
        this.sessionManager = sessionManager;
        this.memberService = memberService;
    }

    @GetMapping("/userdata")
    public ResponseEntity<Object> getPersonalInformation(HttpServletRequest request){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null)
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/memberInfo")
    public ResponseEntity<Object> getMemberInfo(HttpServletRequest request, String studentId){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        int permission = memberService.getPermissionLevel(data[0], data[2]);
        if(permission != 0) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 부족합니다.");
        Member member = memberService.findById(studentId);
        if(member == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(member);
    }

    @GetMapping("/memberProfile")
    public ResponseEntity<Object> getMemberProfile(HttpServletRequest request, String studentId){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        int permission = memberService.getPermissionLevel(data[0], data[2]);
        if(permission != 0) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 부족합니다.");
        Member member = memberService.findById(studentId);
        if(member == null) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatusCode.valueOf(302)).body(memberService.getProfilePhoto(studentId));
    }

    @GetMapping("/permission")
    public ResponseEntity<Object> getMemberPermission(HttpServletRequest request,
                                                      @RequestParam(required=false) String category,
                                                      @RequestParam(required = false) Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        int permission = memberService.getPermissionLevel(data[0], data[2]);
        if(category != null && id != null){
            if(permission == 0) return ResponseEntity.ok().body(true);
            return ResponseEntity.ok().body(memberService.hasPermission(category, id, data[0], data[2]));
        } else {
            return ResponseEntity.ok().body(permission);
        }
    }
}
