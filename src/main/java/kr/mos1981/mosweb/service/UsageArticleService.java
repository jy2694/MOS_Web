package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.entity.UsageArticle;
import kr.mos1981.mosweb.repository.UsageArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsageArticleService {

    private UsageArticleRepository usageArticleRepository;
    private AttachmentFileService attachmentFileService;

    @Autowired
    public UsageArticleService(UsageArticleRepository usageArticleRepository,
                               AttachmentFileService attachmentFileService){
        this.usageArticleRepository = usageArticleRepository;
        this.attachmentFileService = attachmentFileService;
    }

    public List<UsageArticle> findAll(){
        return usageArticleRepository.findAll();
    }

    public UsageArticle findById(Long id){
        Optional<UsageArticle> UsageArticleOptional = usageArticleRepository.findById(id);
        if(UsageArticleOptional.isEmpty()) return null;
        return UsageArticleOptional.get();
    }

    public UsageArticle createArticle(WriteArticleDTO dto){
        UsageArticle article = new UsageArticle(dto);
        usageArticleRepository.save(article);
        return article;
    }

    public void deleteArticle(Long id){
        UsageArticle article = findById(id);
        if(article == null) return;
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("usage", id);
        for(AttachmentFile file : files)
            attachmentFileService.deleteAttachment(file.getId());
        usageArticleRepository.delete(article);
    }
}
