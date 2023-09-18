package ru.drmiki.help_for_capy.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.drmiki.help_for_capy.classes.Category;
import ru.drmiki.help_for_capy.repo.CategoryRepository;

public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory (Long id){
        return categoryRepository.getReferenceById(id);
    }
}
