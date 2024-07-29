package com.dropwizard.dto;

import com.dropwizard.constant.Gender;
import com.dropwizard.constant.PokemonType;
import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

public class PokemonDTO {

    private Long id;
    private String name;
    private Integer level;
    private String nickname;
    private Set<PokemonType> pokemonTypes;
    private Gender gender;
    private Date dateAdded;


    public PokemonDTO(Pokemon pokemon) {
        this.id = pokemon.getId();
        this.name = pokemon.getName();
        this.level = pokemon.getLevel();
        this.nickname = pokemon.getNickname();
        this.pokemonTypes = pokemon.getPokemonTypes();
        this.gender = pokemon.getGender();
        this.dateAdded = pokemon.getDateAdded();
    }

    public PokemonDTO() {}

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
                '}';
    }
}
