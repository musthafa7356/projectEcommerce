package com.example.library.service;

import com.example.library.model.Size;

import java.util.List;

public interface SizeService {

    List<Size> allSizeVariations();

    Size findById(long id);
}
