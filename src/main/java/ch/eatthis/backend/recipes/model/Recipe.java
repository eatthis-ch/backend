package ch.eatthis.backend.recipes.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "recipe")
public class Recipe {

    @Id
    private String id;
    private String title;
    private String description;
    private String image_id;
    private int energy_cal;
    private int protein_g;
    private int fat_g;
    private int carbohydrate_g;
    private String ingredients;
    private String procedure;

    // TODO: Sum up properties with same type

}
