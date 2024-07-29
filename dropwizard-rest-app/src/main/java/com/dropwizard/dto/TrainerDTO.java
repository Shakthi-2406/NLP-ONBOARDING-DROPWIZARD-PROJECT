package com.dropwizard.dto;

import com.dropwizard.model.Pokemon;
import com.dropwizard.model.Trainer;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

public class TrainerDTO {
    private Long id;
    private String name;
    private Integer age;
    private Date dateAdded;

    public TrainerDTO() {}

    public TrainerDTO(Trainer trainer) {
        this.id = trainer.getId();
        this.name = trainer.getName();
        this.age = trainer.getAge();
        this.dateAdded = trainer.getDateAdded();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
