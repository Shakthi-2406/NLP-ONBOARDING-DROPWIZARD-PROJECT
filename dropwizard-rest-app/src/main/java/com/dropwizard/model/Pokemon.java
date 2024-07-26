package com.dropwizard.model;

import com.dropwizard.constant.Gender;
import com.dropwizard.constant.PokemonType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "nickname")
    private String nickname;

    @ElementCollection(targetClass = PokemonType.class)
    @CollectionTable(name = "pokemon_types", joinColumns = @JoinColumn(name = "pokemon_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Set<PokemonType> pokemonTypes;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_added")
    private Date dateAdded;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    // Constructors, getters, setters, and toString() methods


    public Pokemon(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    public Pokemon() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<PokemonType> getPokemonTypes() {
        return pokemonTypes;
    }

    public void setPokemonTypes(Set<PokemonType> pokemonTypes) {
        this.pokemonTypes = pokemonTypes;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", nickname='" + nickname + '\'' +
                ", pokemonTypes=" + pokemonTypes +
                ", gender=" + gender +
                ", dateAdded=" + dateAdded +
                ", trainer=" + trainer +
                '}';
    }
}
