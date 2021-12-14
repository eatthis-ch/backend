package ch.eatthis.backend.recipes.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String image_id;
    private int energy_cal;
    private int protein_g;
    private int fat_g;
    private int carbohydrate_g;
    private String ingredients;
    private String procedure;

    // TODO: Sum up properties with same type

}
