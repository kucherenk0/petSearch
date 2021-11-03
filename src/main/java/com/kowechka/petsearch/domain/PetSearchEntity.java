package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime dateOfLost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SearchStatus status;

    @Column(name = "address")
    private String address;

    @Column(name = "color")
    private int color;

    @Column(name = "tail")
    private int tail;

    @Column(name = "radius")
    private int radius;

    @ManyToOne
    private User user;
}
