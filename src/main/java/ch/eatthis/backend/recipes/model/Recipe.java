package ch.eatthis.backend.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe {

    @Id
    private String id;
    private String title, description, image_id, ingredients, procedure, source;
    private int energy_cal, protein_g, fat_g, carbohydrate_g;
    private double fat_percent, protein_percent, carbohydrate_percent;

}
