package ru.drmiki.help_for_capy.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.drmiki.help_for_capy.classes.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
