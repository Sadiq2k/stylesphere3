package com.Mini.Mini.service;
import com.Mini.Mini.Entity.Rating;
import com.Mini.Mini.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    RatingRepository ratingRepository;

//    public Rating getRatingByProductId(Product productId) {
//        return ratingRepository.find
//    }

    public void saveRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public Object getAllRating() {
       return ratingRepository.findAll();
    }
    public List<Rating> getAllRatingsCount(){
        return ratingRepository.findAll();
    }
        public List<Rating> getRatingCountByProductId(Long id){
        return ratingRepository.findRatingByProductId(id);
        }
    public void removeRating(Long id) {
        ratingRepository.deleteById(id);
    }

//    public Rating getIdByUser(Long user) {
//        return ratingRepository.findById(user).get();
//    }


}
