package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.configuration.StorageProperties;
import kr.mos1981.mosweb.entity.AttachmentFile;
import kr.mos1981.mosweb.repository.AttachmentFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentFileService {

    private final AttachmentFileRepository attachmentFileRepository;
    private final StorageProperties storageProperties;

    @Autowired
    public AttachmentFileService(AttachmentFileRepository attachmentFileRepository,
                                 StorageProperties storageProperties){
        this.attachmentFileRepository = attachmentFileRepository;
        this.storageProperties = storageProperties;
    }

    public AttachmentFile saveAttachmentFile(MultipartFile file, String category, Long boardId){
        if(file.isEmpty()) return null;
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String originPath = UUID.randomUUID().toString().replaceAll("-","") + fileExtension;

        Path destinationFile = Paths.get(storageProperties.getLocation()).resolve(
                        Paths.get(originPath))
                .normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e){
            return null;
        }
        AttachmentFile attachmentFile = new AttachmentFile(
                category,
                boardId,
                originPath,
                fileName,
                fileExtension
        );
        attachmentFileRepository.save(attachmentFile);
        return attachmentFile;
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

    public boolean isContainedAttachmentFile(String[] idList, AttachmentFile file){
        return Arrays.stream(idList)
                .filter(stringId -> Integer.parseInt(stringId) == file.getId())
                .findAny().isEmpty();
    }
}
