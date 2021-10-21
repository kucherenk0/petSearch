package com.kowechka.petsearch.repository;

import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Picture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Query("select picture from Picture picture where picture.user.login = ?#{principal.username}")
    List<Picture> findByUserIsCurrentUser();

    List<Picture> findAllByUser(User user);
}
