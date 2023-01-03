package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.repository.PastTestArticleRepository;
import org.springframework.stereotype.Service;

@Service
public class PastTestArticleService {

    private PastTestArticleRepository pastTestArticleRepository;

    public PastTestArticleService(PastTestArticleRepository pastTestArticleRepository){
        this.pastTestArticleRepository = pastTestArticleRepository;
    }
}
