package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.web.rest.dto.PictureDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

/**
 * A PicturesUploading.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pictures_uploading")
public class PicturesUploading implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "processed_pictures")
    private Integer processedPictures = 0;

    @Column(name = "number_of_pictures")
    private Integer numberOfPictures = 0;

    @ManyToOne
    private User user;
}
