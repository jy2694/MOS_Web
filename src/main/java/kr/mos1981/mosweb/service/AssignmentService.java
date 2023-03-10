package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.CreateAssignmentDTO;
import kr.mos1981.mosweb.dto.SubmitAssignmentDTO;
import kr.mos1981.mosweb.entity.Assignment;
import kr.mos1981.mosweb.entity.AssignmentSubmit;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.repository.AssignmentRepository;
import kr.mos1981.mosweb.repository.AssignmentSubmitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private AssignmentRepository assignmentRepository;
    private AssignmentSubmitRepository assignmentSubmitRepository;
    private AttachmentFileService attachmentFileService;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssignmentSubmitRepository assignmentSubmitRepository,
                             AttachmentFileService attachmentFileService){
        this.assignmentRepository = assignmentRepository;
        this.assignmentSubmitRepository = assignmentSubmitRepository;
        this.attachmentFileService = attachmentFileService;
    }

    public Assignment findById(Long id){
        Optional<Assignment> assignment = null;
        return (assignment = assignmentRepository.findById(id)).isEmpty() ? null : assignment.get();
    }

    public AssignmentSubmit findSubmitById(Long id){
        Optional<AssignmentSubmit> assignmentSubmit = null;
        return (assignmentSubmit = assignmentSubmitRepository.findById(id)).isEmpty() ? null : assignmentSubmit.get();
    }

    public List<AssignmentSubmit> findAllSubmitByAssignId(Long assignId){
        return assignmentSubmitRepository.findByAssignId(assignId);
    }

    public List<AttachmentFile> findAttachmentFileByAssignId(Long assignId){
        return attachmentFileService.getAttachmentFiles("assignment", assignId);
    }

    public List<AttachmentFile> findAttachmentFileBySubmit(AssignmentSubmit assignmentSubmit){
        return attachmentFileService.getAttachmentFiles("submit", assignmentSubmit.getId());
    }

    public AssignmentSubmit findSubmitByAssignIdAndCreateBy(Long assignId, String createBy){
        Optional<AssignmentSubmit> assignmentSubmit = null;
        return (assignmentSubmit = assignmentSubmitRepository.findByAssignIdAndCreateBy(assignId, createBy)).isEmpty() ? null : assignmentSubmit.get();
    }

    public List<Assignment> findAll(){
        return assignmentRepository.findAll();
    }

    public Assignment createAssignment(CreateAssignmentDTO dto){
        Assignment assignment = new Assignment(dto);
        assignmentRepository.save(assignment);
        //????????? ????????? ?????????
        if(dto.getFiles() != null){
            //?????? ?????? ??? ????????? ????????? ?????????. ?????? ?????? ????????? ?????? ??? ?????? ????????? ????????? ????????? ??????.
            List<AttachmentFile> saveSuccessFiles = new ArrayList<>();
            for(MultipartFile file : dto.getFiles()){
                //?????? ??????
                AttachmentFile saved = attachmentFileService.saveAttachmentFile(file, "assignment", assignment.getId());
                if(saved == null){
                    //?????? ?????? ??? ?????? ????????? ????????? ??????
                    for(AttachmentFile saveSuccessFile : saveSuccessFiles)
                        attachmentFileService.deleteAttachment(saveSuccessFile.getId());
                    return null;
                }
            }
        }
        return assignment;
    }

    public void deleteAssignment(Long id){
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        if(assignment.isEmpty()) return;
        List<AssignmentSubmit> submits = assignmentSubmitRepository.findByAssignId(id);
        for(AssignmentSubmit submit : submits){
            List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("submit", submit.getId());
            for(AttachmentFile file : files)
                attachmentFileService.deleteAttachment(file.getId());
            assignmentSubmitRepository.delete(submit);
        }
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("assignment", id);
        for(AttachmentFile file : files)
            attachmentFileService.deleteAttachment(file.getId());
        assignmentRepository.delete(assignment.get());
    }

    public AssignmentSubmit submitAssignment(SubmitAssignmentDTO dto, Boolean force){
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(dto.getAssignId());
        if(assignmentOptional.isEmpty()) return null;
        AssignmentSubmit assignmentSubmit = findSubmitByAssignIdAndCreateBy(dto.getAssignId(), dto.getCreateBy());
        if(assignmentSubmit != null){
            if(force) {
                List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("submit", assignmentSubmit.getId());
                for(AttachmentFile file : files)
                    attachmentFileService.deleteAttachment(file.getId());
                assignmentSubmitRepository.delete(assignmentSubmit);
            }
            else return null;
        }
        assignmentSubmit = new AssignmentSubmit(dto);
        assignmentSubmitRepository.save(assignmentSubmit);
        if(dto.getFile() != null)
            attachmentFileService.saveAttachmentFile(dto.getFile(), "submit", assignmentSubmit.getId());
        return assignmentSubmit;
    }

    public Assignment modifyAssignment(CreateAssignmentDTO dto, Long id, String[] idList){
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        if(assignmentOptional.isEmpty()) return null;
        Assignment assignment = assignmentOptional.get();
        if(dto.getFiles() != null) { //????????? ??????????????? ?????? ??? ??????
            List<AttachmentFile> savedFiles = new ArrayList<>();
            for (MultipartFile file : dto.getFiles()) {
                AttachmentFile saved = attachmentFileService.saveAttachmentFile(file, "assignment", id);
                if(saved == null){
                    for(AttachmentFile f : savedFiles)
                        attachmentFileService.deleteAttachment(f.getId());
                    return null;
                }
            }
        }
        //?????? ???????????? ???????????? idList??? ?????? ????????? ?????? ?????? ??????
        List<AttachmentFile> attachmentFiles = attachmentFileService.getAttachmentFiles("assignment", id);
        for(AttachmentFile file : attachmentFiles)
            if(!attachmentFileService.isContainedAttachmentFile(idList, file))
                attachmentFileService.deleteAttachment(file.getId());
        if(dto.getContext() != null && !dto.getContext().equals(assignment.getContext()))
            assignment.setTitle(dto.getTitle());
        if(dto.getTitle() != null && !dto.getTitle().equals(assignment.getTitle()))
            assignment.setContext(dto.getContext());
        if(dto.getDeadLine() != null && !dto.getDeadLine().isEqual(assignment.getDeadLine()))
            assignment.setDeadLine(dto.getDeadLine());
        return assignment;
    }


}
