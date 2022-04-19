package ru.dev.numbers.generator.services.realisations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dev.numbers.generator.exceptions.NumberException;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.repositories.NumbersRepository;
import ru.dev.numbers.generator.services.templates.NumbersService;

@Service
public class NumbersServiceImpl implements NumbersService {

    @Autowired
    private NumbersRepository numbersRepository;

    private CarNumber lastOrdinalNumber;

    @Value("${default.region.code}")
    private int defaultRegionCode;


    public NumbersServiceImpl() {
        this.lastOrdinalNumber = CarNumber.builder()
                .regionCode(116)
                .regNumber(0)
                .series("ААА")
                .build();
        this.array = new char[]{'А', 'B', 'E', 'К', 'М', 'Н', 'О', 'Р', 'C', 'Т', 'У', 'Х'};
    }

    private final char[] array;

    @Override
    @Transactional
    public CarNumber getRandomNumber() {
        var current = getRandom(defaultRegionCode);
        var check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                current.getRegNumber(),
                current.getSeries(),
                current.getRegionCode()
        );
        while (check.isPresent()) {
            current = getRandom(defaultRegionCode);
            check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                    current.getRegNumber(),
                    current.getSeries(),
                    current.getRegionCode()
            );
        }
        return numbersRepository.save(current);
    }

    @Override
    @Transactional
    public CarNumber getNextNumber() {
        CarNumber result;
        var current = getNext(lastOrdinalNumber);
        var check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                current.getRegNumber(),
                current.getSeries(),
                current.getRegionCode()
        );
        while (check.isPresent()) {
            current = getNext(current);
            check = numbersRepository.findByRegNumberAndSeriesAndRegionCode(
                    current.getRegNumber(),
                    current.getSeries(),
                    current.getRegionCode()
            );
        }
        result = numbersRepository.save(current);
        lastOrdinalNumber = result;
        return result;
    }

    private CarNumber getNext(CarNumber number) {
        var regNumber = number.getRegNumber();
        var series = number.getSeries();
        var regionCode = number.getRegionCode();

        if (regNumber < 999) {
            regNumber++;
        } else {
            series = incrementSeries(series);
            regNumber = 0;
        }
        return CarNumber.builder()
                .series(series)
                .regionCode(regionCode)
                .regNumber(regNumber)
                .build();
    }

    private CarNumber getRandom(int regionCode) {
        StringBuilder series = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = (int) (Math.random() * array.length);
            series.append(array[index]);
        }
        int regNumber = (int) (Math.random() * 1000);
        return CarNumber.builder()
                .regNumber(regNumber)
                .series(series.toString())
                .regionCode(regionCode)
                .build();
    }

    private String incrementSeries(String series) {
        char[] letters = series.toCharArray();
        var last = letters[2];
        var middle = letters[1];
        var first = letters[0];
        if (last == array[array.length - 1]) {
            for (int i = 0; i < array.length; i++) {
                if (middle == array[i]) {
                    if (i != array.length - 1) {
                        middle = array[i + 1];
                        break;
                    }
                    middle = 'A';
                    for (int j = 0; j < array.length; j++) {
                        if (first == array[j]) {
                            if (j != array.length - 1) {
                                first = array[j + 1];
                                break;
                            } else {
                                throw new NumberException("Cannot increment series");
                            }
                        }
                    }
                }
            }
            last = 'А';
        } else {
            for (int i = 0; i < array.length - 1; i++) {
                if (last == array[i]) {
                    last = array[i + 1];
                    break;
                }
            }
        }
        letters[0] = first;
        letters[1] = middle;
        letters[2] = last;
        return String.valueOf(letters);
    }
}
