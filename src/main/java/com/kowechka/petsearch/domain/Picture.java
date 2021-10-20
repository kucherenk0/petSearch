package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Picture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Picture externalId(String externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Boolean getHasDog() {
        return this.hasDog;
    }

    public Picture hasDog(Boolean hasDog) {
        this.setHasDog(hasDog);
        return this;
    }

    public void setHasDog(Boolean hasDog) {
        this.hasDog = hasDog;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Picture filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Picture streetAddress(String streetAddress) {
        this.setStreetAddress(streetAddress);
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCameraId() {
        return this.cameraId;
    }

    public Picture cameraId(String cameraId) {
        this.setCameraId(cameraId);
        return this;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public LocalDate getDateOfShoot() {
        return this.dateOfShoot;
    }

    public Picture dateOfShoot(LocalDate dateOfShoot) {
        this.setDateOfShoot(dateOfShoot);
        return this;
    }

    public void setDateOfShoot(LocalDate dateOfShoot) {
        this.dateOfShoot = dateOfShoot;
    }

    public Set<Dog> getDogs() {
        return this.dogs;
    }

    public void setDogs(Set<Dog> dogs) {
        if (this.dogs != null) {
            this.dogs.forEach(i -> i.setPicture(null));
        }
        if (dogs != null) {
            dogs.forEach(i -> i.setPicture(this));
        }
        this.dogs = dogs;
    }

    public Picture dogs(Set<Dog> dogs) {
        this.setDogs(dogs);
        return this;
    }

    public Picture addDog(Dog dog) {
        this.dogs.add(dog);
        dog.setPicture(this);
        return this;
    }

    public Picture removeDog(Dog dog) {
        this.dogs.remove(dog);
        dog.setPicture(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Picture user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Picture)) {
            return false;
        }
        return id != null && id.equals(((Picture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Picture{" +
            "id=" + getId() +
            ", externalId='" + getExternalId() + "'" +
            ", hasDog='" + getHasDog() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", cameraId='" + getCameraId() + "'" +
            ", dateOfShoot='" + getDateOfShoot() + "'" +
            "}";
    }
}
