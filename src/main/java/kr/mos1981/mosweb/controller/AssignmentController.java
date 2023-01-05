package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.CreateAssignmentDTO;
import kr.mos1981.mosweb.dto.SubmitAssignmentDTO;
import kr.mos1981.mosweb.entity.Assignment;
import kr.mos1981.mosweb.entity.AssignmentSubmit;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AssignmentController {
    //과제 제출 페이지 컨트롤러

    private SessionManager sessionManager;
    private AssignmentService assignmentService;

    @Autowired
    public AssignmentController(SessionManager sessionManager,
                                AssignmentService assignmentService){
        this.sessionManager = sessionManager;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/assignment")
    public ResponseEntity<Object> getAssignment(@RequestParam(required = false) Long id){
        if(id == null) return ResponseEntity.ok().body(assignmentService.findAll());
        Assignment assignment = assignmentService.findById(id);
        if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
        return ResponseEntity.ok().body(assignment);
    }

    @PostMapping("/assignment")
    public ResponseEntity<Object> createAssignment(HttpServletRequest request, CreateAssignmentDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        Assignment assignment = assignmentService.createAssignment(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(assignment);
    }

//    @PutMapping("/assignment")
//    public ResponseEntity<Object> modifyAssignment(HttpServletRequest request, CreateAssignmentDTO dto){
//TODO - Modify
//    }

    @DeleteMapping("/assignment")
    public ResponseEntity<Object> deleteAssignment(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        Assignment assignment = assignmentService.findById(id);
        if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok().body("DELETED");
    }

    @PostMapping("/assignment/submit")
    public ResponseEntity<Object> submitAssignment(HttpServletRequest request, SubmitAssignmentDTO dto, @RequestParam(required = false) Boolean force){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        Assignment assignment = assignmentService.findById(dto.getAssignId());
        if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        AssignmentSubmit submit = assignmentService.submitAssignment(dto, force);
        if(submit == null) return ResponseEntity.ok().body("ERROR : 이미 제출한 과제입니다.");
        return ResponseEntity.ok().body(submit);
    }

    @GetMapping("/assignment/show")
    public ResponseEntity<Object> getAssignmentSubmit(HttpServletRequest request, Long assignId, @RequestParam(required = false) String createBy){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        Assignment assignment = assignmentService.findById(assignId);
        if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
        if(createBy == null) createBy = data[2] + "(" + data[0] + ")";
        AssignmentSubmit assignmentSubmit = assignmentService.findSubmitByAssignIdAndCreateBy(assignId, createBy);
        if(assignmentSubmit == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 제출한 과제가 없습니다.");
        return ResponseEntity.ok().body(assignmentSubmit);
    }

    @GetMapping("/assignment/attached")
    public ResponseEntity<Object> getAssignmentSubmitAttachmentFile(HttpServletRequest request, Long assignId, @RequestParam(required = false) String createBy){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ERROR : 로그인이 필요합니다.");
        Assignment assignment = assignmentService.findById(assignId);
        if(assignment == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 과제가 존재하지 않습니다.");
        if(createBy == null) createBy = data[2] + "(" + data[0] + ")";
        AssignmentSubmit assignmentSubmit = assignmentService.findSubmitByAssignIdAndCreateBy(assignId, createBy);
        if(assignmentSubmit == null) return ResponseEntity.status(HttpStatusCode.valueOf(410)).body("ERROR : 제출한 과제가 없습니다.");
        List<AttachmentFile> files = assignmentService.findAttachmentFileBySubmit(assignmentSubmit);
        List<String> fileUrls = new ArrayList<>();
        for(AttachmentFile file : files) fileUrls.add("/files/" + file.getOriginPath());
        return ResponseEntity.ok().body(fileUrls);
    }
}