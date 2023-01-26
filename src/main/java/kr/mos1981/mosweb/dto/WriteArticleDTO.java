package kr.mos1981.mosweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class WriteArticleDTO {
    private String title;
    private String context;
    private String createBy;
    private MultipartFile[] files;
}
