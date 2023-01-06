package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.GalleryArticle;
import kr.mos1981.mosweb.service.GalleryArticleService;
import kr.mos1981.mosweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class GalleryController {
    //갤러리 페이지 컨트롤러

    private GalleryArticleService galleryArticleService;
    private MemberService memberService;
    private SessionManager sessionManager;

    @Autowired
    public GalleryController(GalleryArticleService galleryArticleService,
                             MemberService memberService,
                             SessionManager sessionManager){
        this.galleryArticleService = galleryArticleService;
        this.memberService = memberService;
        this.sessionManager = sessionManager;
    }

    /*
    보기 권한 : 비회원
    쓰기 권한 : 회원
    삭제 권한 : 회원
     */
    @GetMapping("/gallery")
    public ResponseEntity<Object> getGalleryArticle(@RequestParam(required = false) Long id){
        if(id == null) return ResponseEntity.ok().body(galleryArticleService.findAll());
        GalleryArticle galleryArticle = galleryArticleService.findById(id);
        if(galleryArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        return ResponseEntity.ok().body(galleryArticle);
    }

    @PutMapping("/gallery")
    public ResponseEntity<Object> modifyGalleryArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        GalleryArticle article = galleryArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
            && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        article = galleryArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/gallery")
    public ResponseEntity<Object> writeGalleryArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        GalleryArticle article = galleryArticleService.createArticle(dto);
        if(article == null) return ResponseEntity.internalServerError().body("ERROR : 파일 업로드 중 오류가 발생하였습니다.");
        return ResponseEntity.created(URI.create("/gallery?id="+article.getId())).body(article);
    }

    @DeleteMapping("/gallery")
    public ResponseEntity<String> deleteGalleryArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("ERROR : 로그인이 필요합니다.");
        GalleryArticle article = galleryArticleService.findById(id);
        if(article == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
                && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 권한이 없습니다.");
        galleryArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
