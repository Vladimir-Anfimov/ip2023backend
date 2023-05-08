package org.api.dealshopper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "score")
    private Float score;

    @Column(name = "review")
    private String review;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;
}
/*
@JoinColumn(name = "restaurant_id", referencedColumnName = "id") specifies that the join
column is named restaurant_id, but also sets the referencedColumnName attribute
 to id. This means that the restaurant_id column in the reviews table references
  the id column in the restaurants table.
 */