package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.entity.NoticeArticle;
import kr.mos1981.mosweb.repository.NoticeArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeArticleService {

    private NoticeArticleRepository noticeArticleRepository;
    private AttachmentFileService attachmentFileService;

    @Autowired
    public NoticeArticleService(NoticeArticleRepository noticeArticleRepository,
                                AttachmentFileService attachmentFileService){
        this.noticeArticleRepository = noticeArticleRepository;
        this.attachmentFileService = attachmentFileService;
    }

    public List<NoticeArticle> findAll(){
        return noticeArticleRepository.findAll();
    }

    public NoticeArticle findById(Long id){
        Optional<NoticeArticle> noticeArticleOptional = noticeArticleRepository.findById(id);
        if(noticeArticleOptional.isEmpty()) return null;
        return noticeArticleOptional.get();
    }

    public NoticeArticle createArticle(WriteArticleDTO dto){
        NoticeArticle article = new NoticeArticle(dto);
        noticeArticleRepository.save(article);
        return article;
    }

    public void deleteArticle(Long id){
        NoticeArticle article = findById(id);
        if(article == null) return;
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("notice", id);
        for(AttachmentFile file : files)
            attachmentFileService.deleteAttachment(file.getId());
        noticeArticleRepository.delete(article);
    }
}
