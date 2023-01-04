package kr.mos1981.mosweb.repository;

import kr.mos1981.mosweb.entity.NoticeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeArticleRepository
        extends JpaRepository<NoticeArticle, Long>
{
}
