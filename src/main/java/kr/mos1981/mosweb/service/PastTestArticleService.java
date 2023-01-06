package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.entity.PastTestArticle;
import kr.mos1981.mosweb.repository.PastTestArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        //저장할 파일이 있다면
        if(dto.getFiles() != null){
            //여러 파일 중 저장이 완료된 파일들. 특정 파일 업로드 오류 시 이미 완료된 파일을 지우기 위함.
            List<AttachmentFile> saveSuccessFiles = new ArrayList<>();
            for(MultipartFile file : dto.getFiles()){
                //파일 저장
                AttachmentFile saved = attachmentFileService.saveAttachmentFile(file, "pasttest", article.getId());
                if(saved == null){
                    //저장 실패 시 이미 저장된 파일들 삭제
                    for(AttachmentFile saveSuccessFile : saveSuccessFiles)
                        attachmentFileService.deleteAttachment(saveSuccessFile.getId());
                    return null;
                }
            }
        }
        return article;
    }

    public PastTestArticle modifyArticle(WriteArticleDTO dto, Long id, String[] idList){
        Optional<PastTestArticle> pastTestArticleOptional = pastTestArticleRepository.findById(id);
        if(pastTestArticleOptional.isEmpty()) return null;
        PastTestArticle article = pastTestArticleOptional.get();
        if(dto.getFiles() != null) { //추가된 첨부파일이 있을 시 저장
            List<AttachmentFile> savedFiles = new ArrayList<>();
            for (MultipartFile file : dto.getFiles()) {
                AttachmentFile saved = attachmentFileService.saveAttachmentFile(file, "pasttest", id);
                if(saved == null){
                    for(AttachmentFile f : savedFiles)
                        attachmentFileService.deleteAttachment(f.getId());
                    return null;
                }
            }
        }
        //기존 파일들과 대조하여 idList에 없는 파일은 영구 삭제 처리
        List<AttachmentFile> attachmentFiles = attachmentFileService.getAttachmentFiles("pasttest", id);
        for(AttachmentFile file : attachmentFiles)
            if(!attachmentFileService.isContainedAttachmentFile(idList, file))
                attachmentFileService.deleteAttachment(file.getId());
        if(dto.getContext() != null && !dto.getContext().equals(article.getContext()))
            article.setTitle(dto.getTitle());
        if(dto.getTitle() != null && !dto.getTitle().equals(article.getTitle()))
            article.setContext(dto.getContext());
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
