package org.launchcode.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {


    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Cheese> cheeses = new ArrayList<>();

    @Id
    @GeneratedValue
    private int id;


    @NotNull
    @Size(min=3, max=15, message = "Minimum = 3 characters, Maximum = 15 characters")
    private String name;


    public Category(){}

    public Category(String name){
        this.name=name;
    }




    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}