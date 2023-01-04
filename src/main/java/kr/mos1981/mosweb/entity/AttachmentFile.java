package kr.mos1981.mosweb.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentFile {
    //첨부파일 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long boardId;

    //서버에 저장된 파일 이름
    @Column(nullable = false)
    private String originPath;

    //원래 파일 이름
    @Column(nullable = false)
    private String filePath;

    //파일 확장자
    @Column(nullable = false)
    private String fileExtension;

    @Builder
    public AttachmentFile(Long id, String category, Long boardId, String originPath, String filePath, String fileExtension){
        this.id = id;
        this.category = category;
        this.boardId = boardId;
        this.originPath = originPath;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }

    public AttachmentFile(String category, Long boardId, String originPath, String filePath, String fileExtension){
        this.category = category;
        this.boardId = boardId;
        this.originPath = originPath;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }
}
