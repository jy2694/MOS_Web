package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.repository.NoticeArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeArticleService {

    private NoticeArticleRepository noticeArticleRepository;

    @Autowired
    public NoticeArticleService(NoticeArticleRepository noticeArticleRepository){
        this.noticeArticleRepository = noticeArticleRepository;
    }
}
