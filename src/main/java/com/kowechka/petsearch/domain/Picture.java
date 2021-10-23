package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Data
@Builder
@NoArgsConstructor
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "has_dog")
    private Boolean hasDog;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "camera_id")
    private String cameraId;

    @Column(name = "date_of_shoot")
    private LocalDate dateOfShoot;

    @OneToMany(mappedBy = "picture")
    @JsonIgnoreProperties(value = { "picture" }, allowSetters = true)
    private Set<Dog> dogs = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pictures", "user" }, allowSetters = true)
    private PetSearchEntity search;
}
