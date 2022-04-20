package ru.dev.numbers.generator.services.realisations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dev.numbers.generator.exceptions.NumberException;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.repositories.NumbersRepository;
import ru.dev.numbers.generator.services.templates.NumbersService;
import ru.dev.numbers.generator.utils.CarNumberBuilder;

@Service
@Profile("search")
public class NumbersServiceSearchImpl implements NumbersService {
    @Autowired
    private NumbersRepository numbersRepository;

    @Autowired
    private CarNumberBuilder carNumberBuilder;

    private CarNumber lastOrdinalNumber;
    private Long amountOfNumbers;

    @Value("${default.max.numbers}")
    private long defaultMaxAmountOfNumbers;

    public NumbersServiceSearchImpl() {
        this.lastOrdinalNumber = CarNumber.builder()
                .regionCode(116)
                .regNumber(0)
                .series("ААА")
                .build();
        this.amountOfNumbers = 0L;
    }

    @Override
    @Transactional
    public CarNumber getRandomNumber() {
        var free = numbersRepository.findAllByIsFreeTrue();
        if (free.size() == 0) {
            throw new NumberException("Available car numbers are over");
        }
        var check = false;
        var id = (long) (Math.random() * free.size());
        CarNumber number = null;
        while (!check) {
            var current = numbersRepository.findById(id);
            if (current.isPresent()) {
                if (current.get().getIsFree()) {
                    check = true;
                    number = current.get();
                    number.setIsFree(false);
                    numbersRepository.save(number);
                }
            } else {
                throw new NumberException("Expected value does not exist");
            }
        }
        return number;
    }

    @Override
    @Transactional
    public CarNumber getNextNumber() {
        amountOfNumbers = numbersRepository.countNumbers();
        if (amountOfNumbers == defaultMaxAmountOfNumbers) {
            throw new NumberException("Available car numbers are over");
        }
        CarNumber result;
        var current = numbersRepository.findNextFree(lastOrdinalNumber.getRegNumber(),
                lastOrdinalNumber.getSeries(),
                lastOrdinalNumber.getRegionCode());
        if (current.isPresent()) {
            result = current.get();
            result.setIsFree(false);
            lastOrdinalNumber = numbersRepository.save(result);
            return result;
        }
        throw new NumberException("Expected value does not exist");
    }
}
