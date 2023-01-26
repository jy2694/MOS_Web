package kr.mos1981.mosweb.controller;


import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.UsageArticle;
import kr.mos1981.mosweb.service.MemberService;
import kr.mos1981.mosweb.service.UsageArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
public class UsageController {
    //족보 페이지 컨트롤러
    private UsageArticleService usageArticleService;
    private MemberService memberService;
    private SessionManager sessionManager;
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
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        if(id == null) return ResponseEntity.ok().body(usageArticleService.findAll());
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PutMapping("/usage")
    public ResponseEntity<Object> modifyArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(memberService.getPermissionLevel(data[0], data[2]) != 0)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        article = usageArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/usage")
    public ResponseEntity<Object> writeArticle(HttpServletRequest request,
                                               @RequestParam String title,
                                               @RequestParam String context,
                                               @RequestParam(required = false) MultipartFile[] files){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        if(memberService.getPermissionLevel(data[0], data[2]) != 0)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        WriteArticleDTO dto = new WriteArticleDTO(title, context, null, files);
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        UsageArticle article = usageArticleService.createArticle(dto);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.created(URI.create("/usage?id="+article.getId())).body(article);
    }

    @DeleteMapping("/usage")
    public ResponseEntity<String> deleteArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");;
        UsageArticle article = usageArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(memberService.getPermissionLevel(data[0], data[2]) != 0)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        usageArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
