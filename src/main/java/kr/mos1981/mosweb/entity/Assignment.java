package kr.mos1981.mosweb.entity;

import jakarta.persistence.*;
import kr.mos1981.mosweb.dto.CreateAssignmentDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {
    //과제 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime deadLine;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String context;

    @Builder
    public Assignment(Long id, LocalDateTime createAt, LocalDateTime deadLine, String title, String context){
        this.id = id;
        this.createAt = createAt;
        this.deadLine = deadLine;
        this.title = title;
        this.context = context;
    }

    public Assignment(CreateAssignmentDTO dto){
        this.createAt = LocalDateTime.now();
        this.deadLine = dto.getDeadLine();
        this.title = dto.getTitle();
        this.context = dto.getContext();
    }
}
