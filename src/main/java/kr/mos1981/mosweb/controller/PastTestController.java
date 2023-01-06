package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.PastTestArticle;
import kr.mos1981.mosweb.service.MemberService;
import kr.mos1981.mosweb.service.PastTestArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class PastTestController {
    //족보 공유 페이지 컨트롤러
    private final PastTestArticleService pastTestArticleService;
    private final MemberService memberService;
    private final SessionManager sessionManager;
    @Autowired
    public PastTestController(PastTestArticleService pastTestArticleService,
                            MemberService memberService,
                            SessionManager sessionManager){
        this.pastTestArticleService = pastTestArticleService;
        this.memberService = memberService;
        this.sessionManager = sessionManager;
    }

    /*
    보기 권한 : 회원
    쓰기 권한 : 회원
    삭제 권한 : 회원
     */
    @GetMapping("/pasttest")
    public ResponseEntity<Object> getArticle(HttpServletRequest request, @RequestParam(required = false) Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(id == null) return ResponseEntity.ok().body(pastTestArticleService.findAll());
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/pasttest")
    public ResponseEntity<Object> modifyArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
                && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        article = pastTestArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/pasttest")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        PastTestArticle article = pastTestArticleService.createArticle(dto);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.created(URI.create("/pasttest?id="+article.getId())).body(article);
    }

    @DeleteMapping("/pasttest")
    public ResponseEntity<Object> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
                && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        pastTestArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
