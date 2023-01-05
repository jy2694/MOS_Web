package kr.mos1981.mosweb.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SubmitAssignmentDTO {
    private Long assignId;
    private String createBy;
    private MultipartFile file;
}
