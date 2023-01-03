package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.repository.UsageArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsageArticleService {

    private UsageArticleRepository usageArticleRepository;

    @Autowired
    public UsageArticleService(UsageArticleRepository usageArticleRepository){
        this.usageArticleRepository = usageArticleRepository;
    }
}
