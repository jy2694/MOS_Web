package kr.mos1981.mosweb.entity;

import jakarta.persistence.*;
import kr.mos1981.mosweb.dto.WriteArticleDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PastTestArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String createBy;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String context;

    @Builder
    public PastTestArticle(Long id, String createBy, LocalDateTime createAt, String title, String context){
        this.id = id;
        this.createAt = createAt;
        this.createBy = createBy;
        this.title = title;
        this.context = context;
    }

    public PastTestArticle(WriteArticleDTO dto){
        this.createBy = dto.getCreateBy();
        this.createAt = LocalDateTime.now();
        this.title = dto.getTitle();
        this.context = dto.getContext();
    }
}
