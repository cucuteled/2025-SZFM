package com.project.sfm2025.entities;

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
@Table(name = "etterem")
public class Etterem {

    @Id
    private Integer id; // ez a User id-ja lesz

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String cegNev;

    private String description;
}
