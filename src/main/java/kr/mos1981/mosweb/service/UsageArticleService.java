package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.dto.WriteArticleDTO;
import kr.mos1981.mosweb.entity.UsageArticle;
import kr.mos1981.mosweb.repository.UsageArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsageArticleService {

    private UsageArticleRepository usageArticleRepository;

    @Autowired
    public UsageArticleService(UsageArticleRepository usageArticleRepository){
        this.usageArticleRepository = usageArticleRepository;
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
        usageArticleRepository.delete(article);
    }
}
