package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.entity.*;
import kr.mos1981.mosweb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AttachmentFileController {

    private AttachmentFileService attachmentFileService;
    private GalleryArticleService galleryArticleService;
    private NoticeArticleService noticeArticleService;
    private PastTestArticleService pastTestArticleService;
    private UsageArticleService usageArticleService;
    private AssignmentService assignmentService;

    @Autowired
    public AttachmentFileController(AttachmentFileService attachmentFileService,
                                    GalleryArticleService galleryArticleService,
                                    NoticeArticleService noticeArticleService,
                                    PastTestArticleService pastTestArticleService,
                                    UsageArticleService usageArticleService,
                                    AssignmentService assignmentService){
        this.attachmentFileService = attachmentFileService;
        this.galleryArticleService = galleryArticleService;
        this.noticeArticleService = noticeArticleService;
        this.pastTestArticleService = pastTestArticleService;
        this.usageArticleService = usageArticleService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/attached-files")
    public ResponseEntity<Object> getAttachedFiles(@RequestParam String category, @RequestParam Long id){
        switch(category){
            case "gallery":
                GalleryArticle galleryArticle = galleryArticleService.findById(id);
                if(galleryArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "notice":
                NoticeArticle noticeArticle = noticeArticleService.findById(id);
                if(noticeArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "pasttest":
                PastTestArticle pastTestArticle = pastTestArticleService.findById(id);
                if(pastTestArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "usage":
                UsageArticle usageArticle = usageArticleService.findById(id);
                if(usageArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "assignment":
                Assignment assignment = assignmentService.findById(id);
                if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
                break;
            case "submit":
                AssignmentSubmit submit = assignmentService.findSubmitById(id);
                if(submit == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제 제출이 존재하지 않습니다.");
                break;
            default:
                return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        }
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles(category, id);
        return ResponseEntity.ok().body(files);
    }

    @GetMapping("/attached-urls")
    public ResponseEntity<Object> getAttachedFileUrls(@RequestParam String category, @RequestParam Long id){
        switch(category){
            case "gallery":
                GalleryArticle galleryArticle = galleryArticleService.findById(id);
                if(galleryArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "notice":
                NoticeArticle noticeArticle = noticeArticleService.findById(id);
                if(noticeArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "pasttest":
                PastTestArticle pastTestArticle = pastTestArticleService.findById(id);
                if(pastTestArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "usage":
                UsageArticle usageArticle = usageArticleService.findById(id);
                if(usageArticle == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
                break;
            case "assignment":
                Assignment assignment = assignmentService.findById(id);
                if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
                break;
            case "submit":
                AssignmentSubmit submit = assignmentService.findSubmitById(id);
                if(submit == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제 제출이 존재하지 않습니다.");
                break;
            default:
                return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 게시글이 존재하지 않습니다.");
        }
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles(category, id);
        List<String> fileUrls = new ArrayList<>();
        for(AttachmentFile file : files){
            fileUrls.add("/files/" + file.getOriginPath());
        }
        return ResponseEntity.ok().body(fileUrls);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(HttpServletRequest request, @PathVariable String filename) {
        AttachmentFile attachments = attachmentFileService.findByOriginPath(filename);
        if(attachments == null) return ResponseEntity.notFound().build();
        Resource file = attachmentFileService.loadAsResource(filename);
        String userAgent = request.getHeader("User-Agent");
        if(userAgent.indexOf("Trident") > -1)
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(attachments.getFilePath(), StandardCharsets.UTF_8).replaceAll("\\+", "%20") + "\"").body(file);
        else
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + new String(attachments.getFilePath().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"").body(file);
    }
}
