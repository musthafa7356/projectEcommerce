package com.example.library.service.impl;

import com.example.library.model.Color;
import com.example.library.repository.ColorRepository;
import com.example.library.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ColorServiceImpl implements ColorService {

    private ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public List<Color> allColorVariations() {
        return colorRepository.findAll();
    }
}
