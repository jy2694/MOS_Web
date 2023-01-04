package kr.mos1981.mosweb.repository;

import kr.mos1981.mosweb.entity.PastTestArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastTestArticleRepository
        extends JpaRepository<PastTestArticle, Long>
{
}
