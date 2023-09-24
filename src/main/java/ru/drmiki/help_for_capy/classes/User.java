package ru.drmiki.help_for_capy.classes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @OneToMany
    private List<Phrases> phrases;

    //For quiz
    @Column(name = "countOfAllQuesitions")
    private Integer countOfAllQuesitions;

    @Column(name = "countOfRightQuestions")
    private Integer countOfRightQuestions;


    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
    }
}