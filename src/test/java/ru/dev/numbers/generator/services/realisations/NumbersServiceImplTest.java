package ru.dev.numbers.generator.services.realisations;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.repositories.NumbersRepository;
import ru.dev.numbers.generator.services.templates.NumbersService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("NumbersService is working when:")
class NumbersServiceImplTest {

    @Autowired
    private NumbersService numbersService;

    @MockBean
    private NumbersRepository numbersRepository;

    private CarNumber currentNumber = CarNumber.builder()
            .id(1L)
            .regionCode(116)
            .regNumber(0)
            .series("ААА")
            .build();

    private final CarNumber FIRST_NUMBER = CarNumber.builder()
            .id(2L)
            .regionCode(116)
            .regNumber(1)
            .series("ААА")
            .build();
    private final CarNumber SECOND_NUMBER = CarNumber.builder()
            .id(3L)
            .regionCode(116)
            .regNumber(2)
            .series("ААА")
            .build();

    CarNumber incrementCarNumber() {
        currentNumber.setRegNumber(currentNumber.getRegNumber() + 1);
        currentNumber.setId(currentNumber.getId() + 1);
        return currentNumber;
    }

    @BeforeEach
    void setUp() {
        when(numbersRepository.save(any())).thenReturn(incrementCarNumber());
    }

    @Nested
    @DisplayName("getRandom() is working")
    class GetRandomNumberTest {

        @Test
        void returns() {
        }
    }

    @Nested
    @DisplayName("getNext() is working")
    class GetNextNumberTest {

        @Test
        void returns_correct_number_if_no_one_number_exist() {
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(0, "ААА", 116))
                    .thenReturn(Optional.empty());
            var a = numbersService.getNextNumber();
            assertThat(a.getId(), is(equalTo(FIRST_NUMBER.getId())));
            assertThat(a.getRegNumber(), is(equalTo(FIRST_NUMBER.getRegNumber())));
            assertThat(a.getSeries(), is(equalTo(FIRST_NUMBER.getSeries())));
            assertThat(a.getRegionCode(), is(equalTo(FIRST_NUMBER.getRegionCode())));
        }

        @Test
        void returns_correct_number_if_there_are_some_numbers() {
            var b = numbersService.getNextNumber();
            System.out.println(b);
            var a = numbersService.getNextNumber();
            System.out.println(a);
            assertThat(a.getId(), is(equalTo(FIRST_NUMBER.getId())));
            assertThat(a.getRegNumber(), is(equalTo(FIRST_NUMBER.getRegNumber())));
            assertThat(a.getSeries(), is(equalTo(FIRST_NUMBER.getSeries())));
            assertThat(a.getRegionCode(), is(equalTo(FIRST_NUMBER.getRegionCode())));
        }
    }
}
