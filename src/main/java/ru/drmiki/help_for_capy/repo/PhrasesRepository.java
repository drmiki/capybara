package ru.drmiki.help_for_capy.repo;

import org.springframework.data.repository.CrudRepository;
import ru.drmiki.help_for_capy.classes.Phrases;

public interface PhrasesRepository extends CrudRepository <Phrases, Integer> {
}
