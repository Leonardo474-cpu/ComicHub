package com.comic.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.comic.hub.model.Comic;

public interface ComicRepository extends JpaRepository<Comic, Integer> {

}