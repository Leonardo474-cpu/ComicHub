package com.comic.hub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comic.hub.model.Comic;
import com.comic.hub.repository.ComicRepository;
import com.comic.hub.service.ComicService;

@Service
public class ComicServiceImpl implements ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Override
    public List<Comic> listarComics() {
        return comicRepository.findAll();
    }
}