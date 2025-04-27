package com.mi.teamarket.controller;

import com.mi.teamarket.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageMapper imageMapper;
}
