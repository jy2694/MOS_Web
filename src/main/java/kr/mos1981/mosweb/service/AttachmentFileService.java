package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.configuration.StorageProperties;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.repository.AttachmentFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentFileService {

    private AttachmentFileRepository attachmentFileRepository;
    private StorageProperties storageProperties;

    @Autowired
    public AttachmentFileService(AttachmentFileRepository attachmentFileRepository,
                                 StorageProperties storageProperties){
        this.attachmentFileRepository = attachmentFileRepository;
        this.storageProperties = storageProperties;
    }

    public List<AttachmentFile> getAttachmentFiles(String category, Long id){
        return attachmentFileRepository.findByCategoryAndBoardId(category, id);
    }

    public void deleteAttachment(Long id){
        Optional<AttachmentFile> optionalAttachment = attachmentFileRepository.findById(id);
        if(optionalAttachment.isEmpty())return;
        Path destinationFile = Paths.get(storageProperties.getLocation()).resolve(
                        Paths.get(optionalAttachment.get().getOriginPath()))
                .normalize().toAbsolutePath();
        destinationFile.toFile().delete();
        attachmentFileRepository.deleteById(id);
    }

    public AttachmentFile findByOriginPath(String path){
        Optional<AttachmentFile> attachments = attachmentFileRepository.findByOriginPath(path);
        if(attachments.isEmpty()) return null;
        return attachments.get();
    }

    public Resource loadAsResource(String filename){
        try {
            Path file = Paths.get(storageProperties.getLocation()).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                return null;
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
