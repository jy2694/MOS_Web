package kr.mos1981.mosweb.repository;

import kr.mos1981.mosweb.entity.AttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentFileRepository
        extends JpaRepository<AttachmentFile, Long>
        {
    List<AttachmentFile> findByCategoryAndBoardId(String category, Long boardId);

    Optional<AttachmentFile> findByOriginPath(String originPath);
}
