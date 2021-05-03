package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Tag;
import com.skyletto.startappbackend.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    public List<Tag> getRandomTags(){
        List<Tag> tags = getAllTags();
        if (tags.size()<=7) return tags;
        Collections.shuffle(tags);
        return tags.subList(0,8);
    }

    public List<Tag> getSimilarTags(String str){
        List<Tag> tags = tagRepository.findTagsByNameStartsWith(str);
        if (tags.size()==0) return tagRepository.findTagsByNameContains(str);
        return tags;
    }

    public int saveTags(Set<Tag> tags){
        return tagRepository.saveAll(tags).size();
    }
}
