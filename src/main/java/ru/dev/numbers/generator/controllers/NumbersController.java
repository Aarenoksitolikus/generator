package ru.dev.numbers.generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dev.numbers.generator.dto.CarNumberDto;
import ru.dev.numbers.generator.services.templates.NumbersService;

@RestController
public class NumbersController {

    @Autowired
    private NumbersService numbersService;

    @GetMapping("/random")
    public String getRandomNumber() {
        return CarNumberDto.from(numbersService.getRandomNumber());
    }

    @GetMapping("/next")
    public String getNextNumber() {
        return CarNumberDto.from(numbersService.getNextNumber());
    }
}
