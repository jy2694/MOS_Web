package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.entity.PastTestArticle;
import kr.mos1981.mosweb.repository.PastTestArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PastTestArticleService {

    private PastTestArticleRepository pastTestArticleRepository;
    private AttachmentFileService attachmentFileService;
    @Autowired
    public PastTestArticleService(PastTestArticleRepository pastTestArticleRepository,
                                  AttachmentFileService attachmentFileService){
        this.pastTestArticleRepository = pastTestArticleRepository;
        this.attachmentFileService = attachmentFileService;
    }

    public List<PastTestArticle> findAll(){
        return pastTestArticleRepository.findAll();
    }

    public PastTestArticle findById(Long id){
        Optional<PastTestArticle> PastTestArticleOptional = pastTestArticleRepository.findById(id);
        if(PastTestArticleOptional.isEmpty()) return null;
        return PastTestArticleOptional.get();
    }

    public PastTestArticle createArticle(WriteArticleDTO dto){
        PastTestArticle article = new PastTestArticle(dto);
        pastTestArticleRepository.save(article);
        return article;
    }

    public void deleteArticle(Long id){
        PastTestArticle article = findById(id);
        if(article == null) return;
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("pasttest", id);
        for(AttachmentFile file : files)
            attachmentFileService.deleteAttachment(file.getId());
        pastTestArticleRepository.delete(article);
    }
}
