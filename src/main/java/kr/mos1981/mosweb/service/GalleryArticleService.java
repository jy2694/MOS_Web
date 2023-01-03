package kr.mos1981.mosweb.service;

import kr.mos1981.mosweb.repository.GalleryArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GalleryArticleService {

    private GalleryArticleRepository galleryArticleRepository;

    @Autowired
    public GalleryArticleService(GalleryArticleRepository galleryArticleRepository){
        this.galleryArticleRepository = galleryArticleRepository;
    }
}
