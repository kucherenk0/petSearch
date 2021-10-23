package com.kowechka.petsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Dog.
 */
@Entity
@Table(name = "dog")
public class Dog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "dog_breed")
    private String dogBreed;

    @Column(name = "long_tail")
    private Boolean longTail;

    @Column(name = "has_leash")
    private Boolean hasLeash;

    @Column(name = "coordinates")
    private String coordinates;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dogs", "user", "search" }, allowSetters = true)
    private Picture picture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public Dog color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDogBreed() {
        return this.dogBreed;
    }

    public Dog dogBreed(String dogBreed) {
        this.setDogBreed(dogBreed);
        return this;
    }

    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }

    public Boolean getLongTail() {
        return this.longTail;
    }

    public Dog longTail(Boolean longTail) {
        this.setLongTail(longTail);
        return this;
    }

    public void setLongTail(Boolean longTail) {
        this.longTail = longTail;
    }

    public Boolean getHasLeash() {
        return this.hasLeash;
    }

    public Dog hasLeash(Boolean hasLeash) {
        this.setHasLeash(hasLeash);
        return this;
    }

    public void setHasLeash(Boolean hasLeash) {
        this.hasLeash = hasLeash;
    }

    public String getCoordinates() {
        return this.coordinates;
    }

    public Dog coordinates(String coordinates) {
        this.setCoordinates(coordinates);
        return this;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Dog picture(Picture picture) {
        this.setPicture(picture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dog)) {
            return false;
        }
        return id != null && id.equals(((Dog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dog{" +
            "id=" + getId() +
            ", color='" + getColor() + "'" +
            ", dogBreed='" + getDogBreed() + "'" +
            ", longTail='" + getLongTail() + "'" +
            ", hasLeash='" + getHasLeash() + "'" +
            ", coordinates='" + getCoordinates() + "'" +
            "}";
    }
}
