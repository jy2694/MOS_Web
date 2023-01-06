package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
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

    private final GalleryArticleService galleryArticleService;
    private final MemberService memberService;
    private final SessionManager sessionManager;

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
        if(galleryArticle == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(galleryArticle);
    }

    @PutMapping("/gallery")
    public ResponseEntity<Object> modifyGalleryArticle(HttpServletRequest request, WriteArticleDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        GalleryArticle article = galleryArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
            && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        article = galleryArticleService.modifyArticle(dto, id, idList);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/gallery")
    public ResponseEntity<Object> writeGalleryArticle(HttpServletRequest request, WriteArticleDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        GalleryArticle article = galleryArticleService.createArticle(dto);
        if(article == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.created(URI.create("/gallery?id="+article.getId())).body(article);
    }

    @DeleteMapping("/gallery")
    public ResponseEntity<Object> deleteGalleryArticle(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        GalleryArticle article = galleryArticleService.findById(id);
        if(article == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(!article.getCreateBy().equals(data[2] + "(" + data[0] + ")")
                && memberService.getPermissionLevel(data[0], data[1]) != 0)
            return ResponseEntityEnum.NO_PERMISSION.getMsg();
        galleryArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
