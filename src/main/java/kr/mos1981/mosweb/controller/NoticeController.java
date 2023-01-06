package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.NoticeArticle;
import kr.mos1981.mosweb.service.MemberService;
import kr.mos1981.mosweb.service.NoticeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class NoticeController {
    //공지사항 페이지 컨트롤러

    private final NoticeArticleService noticeArticleService;
    private final MemberService memberService;
    private final SessionManager sessionManager;
    @Autowired
    public NoticeController(NoticeArticleService noticeArticleService,
                            MemberService memberService,
                            SessionManager sessionManager){
        this.memberService = memberService;
        this.noticeArticleService = noticeArticleService;
        this.sessionManager = sessionManager;
    }

    /*
    보기 권한 : 회원
    쓰기 권한 : 관리자
    삭제 권한 : 관리자
     */
    @GetMapping("/notice")
    public ResponseEntity<Object> getArticle(HttpServletRequest request, @RequestParam(required = false) Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(id == null) return ResponseEntity.ok().body(noticeArticleService.findAll());
        NoticeArticle article = noticeArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/notice")
    public ResponseEntity<Object> modifyArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        NoticeArticle article = noticeArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        article = noticeArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/notice")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        NoticeArticle article = noticeArticleService.createArticle(dto);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.created(URI.create("/notice?id="+article.getId())).body(article);
    }

    @DeleteMapping("/notice")
    public ResponseEntity<Object> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        NoticeArticle article = noticeArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        noticeArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
