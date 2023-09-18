package ru.drmiki.help_for_capy.classes;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "CATEGORY")
public class Category implements Comparable<Category>{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private List<Phrases> phrases;


    @Override
    public int compareTo(Category o) {
        return (int)(this.id - o.getId());
    }
}
