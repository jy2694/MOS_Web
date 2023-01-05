package kr.mos1981.mosweb.entity;

import jakarta.persistence.*;
import kr.mos1981.mosweb.dto.SubmitAssignmentDTO;
import kr.mos1981.mosweb.service.AssignmentService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long assignId;

    @Column(nullable = false)
    private String createBy;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Builder
    public AssignmentSubmit(Long id, Long assignId, String createBy, LocalDateTime createAt){
        this.id = id;
        this.assignId = assignId;
        this.createAt = createAt;
        this.createBy = createBy;
    }

    public AssignmentSubmit(SubmitAssignmentDTO dto){
        this.assignId = dto.getAssignId();
        this.createBy = dto.getCreateBy();
        this.createAt = LocalDateTime.now();
    }
}
