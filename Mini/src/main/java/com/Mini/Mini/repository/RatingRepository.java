package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    List<Rating> findRatingByProductId(Long id);

}
