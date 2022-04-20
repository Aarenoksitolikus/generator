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
@Profile("insert")
public class NumbersServiceInsertImpl implements NumbersService {

    @Autowired
    private NumbersRepository numbersRepository;

    @Autowired
    private CarNumberBuilder carNumberBuilder;

    private CarNumber lastOrdinalNumber;
    private Long amountOfNumbers;

    @Value("${default.region.code}")
    private int defaultRegionCode;

    @Value("${default.max.numbers}")
    private long defaultMaxAmountOfNumbers;


    public NumbersServiceInsertImpl() {
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
        amountOfNumbers = numbersRepository.countNumbers();
        if (amountOfNumbers == defaultMaxAmountOfNumbers) {
            throw new NumberException("Available car numbers are over");
        }

        var current = carNumberBuilder.getRandom(defaultRegionCode);
        var check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                current.getRegNumber(),
                current.getSeries(),
                current.getRegionCode()
        );

        while (check.isPresent()) {
            current = carNumberBuilder.getRandom(defaultRegionCode);
            check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                    current.getRegNumber(),
                    current.getSeries(),
                    current.getRegionCode()
            );
        }
        current.setIsFree(false);
        return numbersRepository.save(current);
    }

    @Override
    @Transactional
    public CarNumber getNextNumber() {
        amountOfNumbers = numbersRepository.countNumbers();
        if (amountOfNumbers == defaultMaxAmountOfNumbers) {
            throw new NumberException("Available car numbers are over");
        }

        CarNumber result;
        CarNumber current = CarNumberBuilder.getNext(lastOrdinalNumber);
        var check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                current.getRegNumber(),
                current.getSeries(),
                current.getRegionCode()
        );
        while (check.isPresent()) {
            current = CarNumberBuilder.getNext(current);
            check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                    current.getRegNumber(),
                    current.getSeries(),
                    current.getRegionCode()
            );
        }
        current.setIsFree(false);
        result = numbersRepository.save(current);
        lastOrdinalNumber = result;
        return result;
    }
}
