package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.repository.AttachmentFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentFileService {

    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    public AttachmentFileService(AttachmentFileRepository attachmentFileRepository){
        this.attachmentFileRepository = attachmentFileRepository;
    }

    public List<AttachmentFile> getAttachmentFiles(String category, Long id){
        return attachmentFileRepository.findByCategoryAndBoardId(category, id);
    }
}
