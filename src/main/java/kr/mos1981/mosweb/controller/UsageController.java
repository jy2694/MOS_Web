package kr.mos1981.mosweb.controller;


import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.UsageArticle;
import kr.mos1981.mosweb.service.MemberService;
import kr.mos1981.mosweb.service.UsageArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UsageController {
    //족보 페이지 컨트롤러
    private final UsageArticleService usageArticleService;
    private final MemberService memberService;
    private final SessionManager sessionManager;
    @Autowired
    public UsageController(UsageArticleService usageArticleService,
                              MemberService memberService,
                              SessionManager sessionManager){
        this.memberService = memberService;
        this.usageArticleService = usageArticleService;
        this.sessionManager = sessionManager;
    }

    /*
    보기 권한 : 회원
    쓰기 권한 : 관리자
    삭제 권한 : 관리자
     */
    @GetMapping("/usage")
    public ResponseEntity<Object> getArticle(HttpServletRequest request, @RequestParam(required = false) Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(id == null) return ResponseEntity.ok().body(usageArticleService.findAll());
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/usage")
    public ResponseEntity<Object> modifyArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        article = usageArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/usage")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        UsageArticle article = usageArticleService.createArticle(dto);
        if(article == null) ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.created(URI.create("/usage?id="+article.getId())).body(article);
    }

    @DeleteMapping("/usage")
    public ResponseEntity<Object> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        usageArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
