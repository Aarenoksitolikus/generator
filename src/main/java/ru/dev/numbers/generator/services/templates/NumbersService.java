package ru.dev.numbers.generator.services.templates;

import org.springframework.stereotype.Service;
import ru.dev.numbers.generator.models.CarNumber;

@Service
public interface NumbersService {
    CarNumber getRandomNumber();
    CarNumber getNextNumber() ;
}
