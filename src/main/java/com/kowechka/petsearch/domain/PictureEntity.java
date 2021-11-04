package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@ToString
public class PictureEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "picture_pk_sequence")
    @SequenceGenerator(name = "picture_pk_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "download_url", nullable = false)
    private String downloadUrl;

    @Column(name = "processed")
    private Boolean processed = false;

    @Column(name = "has_dog")
    private Boolean hasDog = false;

    @Column(name = "has_animal")
    private Boolean hasAnimal = false;

    @Column(name = "has_owner")
    private Boolean hasOwner = false;

    @Column(name = "color")
    private Integer color = 0;

    @Column(name = "tail")
    private Integer tail = 0;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lon")
    private String lon;

    @Column(name = "camera_uid")
    private String cameraUid;

    @Column(name = "date")
    private LocalDateTime date;

    @ToString.Exclude
    @ManyToOne
    @JsonIgnoreProperties(value = { "pictures", "user" }, allowSetters = true)
    private PicturesUploading picturesUploading;

    @ManyToOne
    private User user;
}
