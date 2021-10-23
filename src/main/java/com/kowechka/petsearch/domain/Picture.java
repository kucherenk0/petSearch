package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    //TODO: костыль, не знаю почему свзяь не работает
    @Column(name = "pet_search_id")
    private Long petSearchId;

    @Column(name = "date_of_shoot")
    private LocalDate dateOfShoot;

    @OneToMany(mappedBy = "picture")
    @Builder.Default
    @JsonIgnoreProperties(value = { "picture" }, allowSetters = true)
    private Set<Dog> dogs = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pictures", "user" }, allowSetters = true)
    private PetSearchEntity search;

    @Override
    public String toString() {
        return (
            "Picture{" +
            "id=" +
            (id == null ? "null" : id) +
            ", externalId='" +
            (externalId == null ? "null" : externalId) +
            '\'' +
            ", hasDog=" +
            (hasDog == null ? "null" : hasDog) +
            ", filePath='" +
            (filePath == null ? "null" : filePath) +
            '\'' +
            ", streetAddress='" +
            (streetAddress == null ? "null" : streetAddress) +
            '\'' +
            ", cameraId='" +
            (cameraId == null ? "null" : cameraId) +
            '\'' +
            ", dateOfShoot=" +
            (dateOfShoot == null ? "null" : dateOfShoot) +
            ", dogs=" +
            (dogs == null ? "null" : dogs) +
            ", user=" +
            (user == null ? "null" : user) +
            '}'
        );
    }
}
