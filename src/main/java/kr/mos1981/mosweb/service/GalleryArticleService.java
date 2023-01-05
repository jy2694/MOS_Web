package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.entity.GalleryArticle;
import kr.mos1981.mosweb.repository.GalleryArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GalleryArticleService {

    private GalleryArticleRepository galleryArticleRepository;
    private AttachmentFileService attachmentFileService;

    @Autowired
    public GalleryArticleService(GalleryArticleRepository galleryArticleRepository,
                                 AttachmentFileService attachmentFileService){
        this.galleryArticleRepository = galleryArticleRepository;
        this.attachmentFileService = attachmentFileService;
    }

    public List<GalleryArticle> findAll(){
        return galleryArticleRepository.findAll();
    }

    public GalleryArticle findById(Long id){
        Optional<GalleryArticle> galleryArticleOptional = galleryArticleRepository.findById(id);
        if(galleryArticleOptional.isEmpty()) return null;
        return galleryArticleOptional.get();
    }

    public GalleryArticle createArticle(WriteArticleDTO dto){
        GalleryArticle article = new GalleryArticle(dto);
        galleryArticleRepository.save(article);
        return article;
    }

    public void deleteArticle(Long id){
        GalleryArticle article = findById(id);
        if(article == null) return;
        List<AttachmentFile> files = attachmentFileService.getAttachmentFiles("gallery", id);
        for(AttachmentFile file : files)
            attachmentFileService.deleteAttachment(file.getId());
        galleryArticleRepository.delete(article);
    }
}
