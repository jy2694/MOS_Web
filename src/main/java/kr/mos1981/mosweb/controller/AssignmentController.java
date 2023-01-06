package kr.mos1981.mosweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.mos1981.mosweb.api.ResponseEntityEnum;
import kr.mos1981.mosweb.api.SessionManager;
import kr.mos1981.mosweb.dto.CreateAssignmentDTO;
import kr.mos1981.mosweb.dto.SubmitAssignmentDTO;
import kr.mos1981.mosweb.entity.Assignment;
import kr.mos1981.mosweb.entity.AssignmentSubmit;
import kr.mos1981.mosweb.service.AssignmentService;
import kr.mos1981.mosweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AssignmentController {
    //과제 제출 페이지 컨트롤러

    private final SessionManager sessionManager;
    private final AssignmentService assignmentService;
    private final MemberService memberService;

    @Autowired
    public AssignmentController(SessionManager sessionManager,
                                AssignmentService assignmentService,
                                MemberService memberService){
        this.sessionManager = sessionManager;
        this.assignmentService = assignmentService;
        this.memberService = memberService;
    }

    @GetMapping("/assignment")
    public ResponseEntity<Object> getAssignment(@RequestParam(required = false) Long id){
        if(id == null) return ResponseEntity.ok().body(assignmentService.findAll());
        Assignment assignment = assignmentService.findById(id);
        if(assignment == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(assignment);
    }

    @PostMapping("/assignment")
    public ResponseEntity<Object> createAssignment(HttpServletRequest request, CreateAssignmentDTO dto){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(memberService.getPermissionLevel(data[0], data[2]) != 2) return ResponseEntityEnum.NO_PERMISSION.getMsg();
        Assignment assignment = assignmentService.createAssignment(dto);
        if(assignment == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(assignment);
    }

    @PutMapping("/assignment")
    public ResponseEntity<Object> modifyAssignment(HttpServletRequest request, CreateAssignmentDTO dto, Long id, String[] idList){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(memberService.getPermissionLevel(data[0], data[2]) != 2) return ResponseEntityEnum.NO_PERMISSION.getMsg();
        Assignment assignment = assignmentService.findById(id);
        if(assignment == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        assignment = assignmentService.modifyAssignment(dto, id, idList);
        if(assignment == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(assignment);
    }

    @DeleteMapping("/assignment")
    public ResponseEntity<Object> deleteAssignment(HttpServletRequest request, Long id){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        if(memberService.getPermissionLevel(data[0], data[2]) != 2) return ResponseEntityEnum.NO_PERMISSION.getMsg();
        Assignment assignment = assignmentService.findById(id);
        if(assignment == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assignment/submit")
    public ResponseEntity<Object> submitAssignment(HttpServletRequest request, SubmitAssignmentDTO dto, @RequestParam(required = false) Boolean force){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        Assignment assignment = assignmentService.findById(dto.getAssignId());
        if(assignment == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        dto.setCreateBy(data[2] + "(" + data[0] + ")");
        AssignmentSubmit submit = assignmentService.submitAssignment(dto, force);
        if(submit == null) return ResponseEntityEnum.INTERNAL_SERVER_ERROR.getMsg();
        return ResponseEntity.ok().body(submit);
    }

    @GetMapping("/assignment/submit")
    public ResponseEntity<Object> getAssignmentSubmit(HttpServletRequest request, Long assignId, @RequestParam(required = false) String createBy){
        String[] data = (String[]) sessionManager.getSession(request);
        if(data == null) return ResponseEntityEnum.LOGIN_REQUIRED.getMsg();
        Assignment assignment = assignmentService.findById(assignId);
        if(assignment == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        if(createBy == null) createBy = data[2] + "(" + data[0] + ")";
        else if(memberService.getPermissionLevel(data[0], data[2]) != 2) return ResponseEntityEnum.NO_PERMISSION.getMsg();
        AssignmentSubmit assignmentSubmit = assignmentService.findSubmitByAssignIdAndCreateBy(assignId, createBy);
        if(assignmentSubmit == null) return ResponseEntityEnum.NOT_EXIST.getMsg();
        return ResponseEntity.ok().body(assignmentSubmit);
    }
}