package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.NoticeArticle;
import kr.mos1981.mosweb.service.NoticeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class NoticeController {
    //공지사항 페이지 컨트롤러

    private NoticeArticleService noticeArticleService;
    private SessionManager sessionManager;
    @Autowired
    public NoticeController(NoticeArticleService noticeArticleService,
                            SessionManager sessionManager){
        this.noticeArticleService = noticeArticleService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/notice")
    public ResponseEntity<Object> getArticle(@RequestParam(required = false) Long id){
        if(id == null) return ResponseEntity.ok().body(noticeArticleService.findAll());
        NoticeArticle article = noticeArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/notice")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        NoticeArticle article = noticeArticleService.createArticle(dto);
        return ResponseEntity.created(URI.create("/notice?id="+article.getId())).body(article);
    }

    @DeleteMapping("/notice")
    public ResponseEntity<String> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        NoticeArticle article = noticeArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        noticeArticleService.deleteArticle(id);
        return ResponseEntity.ok().body("DELETED");
    }
}
