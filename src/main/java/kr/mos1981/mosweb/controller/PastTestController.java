package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.NoticeArticle;
import kr.mos1981.mosweb.entity.PastTestArticle;
import kr.mos1981.mosweb.service.PastTestArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class PastTestController {
    //족보 공유 페이지 컨트롤러
    private PastTestArticleService pastTestArticleService;
    private SessionManager sessionManager;
    @Autowired
    public PastTestController(PastTestArticleService pastTestArticleService,
                            SessionManager sessionManager){
        this.pastTestArticleService = pastTestArticleService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/pasttest")
    public ResponseEntity<Object> getArticle(@RequestParam(required = false) Long id){
        if(id == null) return ResponseEntity.ok().body(pastTestArticleService.findAll());
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/pasttest")
    public ResponseEntity<Object> modifyArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        article = pastTestArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/pasttest")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        PastTestArticle article = pastTestArticleService.createArticle(dto);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.created(URI.create("/pasttest?id="+article.getId())).body(article);
    }

    @DeleteMapping("/pasttest")
    public ResponseEntity<String> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        PastTestArticle article = pastTestArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");;
        pastTestArticleService.deleteArticle(id);
        return ResponseEntity.ok().body("DELETED");
    }
}
