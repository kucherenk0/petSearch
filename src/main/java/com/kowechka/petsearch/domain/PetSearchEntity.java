package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

/**
 * A PetSearchEntity.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_search_entity")
public class PetSearchEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_lost")
    private LocalDate dateOfLost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SearchStatus status;

    @Column(name = "adderss")
    private String adderss;

    @OneToMany(mappedBy = "search")
    @Builder.Default
    @JsonIgnoreProperties(value = { "dogs", "user", "search" }, allowSetters = true)
    private Set<Picture> pictures = new HashSet<>();

    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return (
            "PetSearchEntity{" +
            "id=" +
            (id == null ? "null" : id) +
            ", dateOfLost=" +
            (dateOfLost == null ? "null" : dateOfLost) +
            ", status=" +
            (status == null ? "null" : status) +
            ", adderss='" +
            (adderss == null ? "null" : adderss) +
            ", pictures=" +
            (pictures == null ? "null" : pictures) +
            ", user=" +
            (user == null ? "null" : user) +
            '}'
        );
    }
}
