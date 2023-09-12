package ru.drmiki.help_for_capy.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "phrases")
public class Phrases {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String engName;
    private String rusName;

}
