package ru.drmiki.help_for_capy.classes;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "phrases")
public class Phrases {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String engName;
    private String rusName;

    @ManyToMany
    private List<Category> category;

    @ManyToOne
    private User user;
}
