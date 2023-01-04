package kr.mos1981.mosweb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAssignmentDTO {
    private String title;
    private String context;
    private LocalDateTime deadLine;
}
