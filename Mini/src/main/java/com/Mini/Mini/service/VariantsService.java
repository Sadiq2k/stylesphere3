package com.Mini.Mini.service;

import com.Mini.Mini.Entity.Variants;
import com.Mini.Mini.repository.VariantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantsService {

    @Autowired
    private VariantsRepository variantsRepository;

    public void saveVariants(Variants name) {
        variantsRepository.save(name);
    }

    public List<Variants> getAllVariantName() {
        return variantsRepository.findAll();
    }


    public List<Variants> getAllVariants() {
        return variantsRepository.findAll();
    }
}
