package ru.dev.numbers.generator.services.realisations;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.dev.numbers.generator.exceptions.NumberException;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.repositories.NumbersRepository;
import ru.dev.numbers.generator.services.templates.NumbersService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("NumbersService is working when:")
class NumbersServiceInsertImplTest {

    @Autowired
    private NumbersService numbersService;

    @MockBean
    private NumbersRepository numbersRepository;

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

    private final CarNumber THIRD_NUMBER = CarNumber.builder()
            .id(4L)
            .regionCode(116)
            .regNumber(3)
            .series("ААА")
            .build();

    private final CarNumber LIMIT_NUMBER = CarNumber.builder()
            .id(999L)
            .regionCode(116)
            .regNumber(999)
            .series("ААА")
            .build();

    private final CarNumber NEW_NUMBER = CarNumber.builder()
            .id(1000L)
            .regionCode(116)
            .regNumber(0)
            .series("ААВ")
            .build();

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
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(1, "ААА", 116))
                    .thenReturn(Optional.empty());
            when(numbersRepository.save(any())).thenReturn(FIRST_NUMBER);

            var result = numbersService.getNextNumber();
            assertThat(result.getId(), is(equalTo(FIRST_NUMBER.getId())));
            assertThat(result.getRegNumber(), is(equalTo(FIRST_NUMBER.getRegNumber())));
            assertThat(result.getSeries(), is(equalTo(FIRST_NUMBER.getSeries())));
            assertThat(result.getRegionCode(), is(equalTo(FIRST_NUMBER.getRegionCode())));
        }

        @Test
        void returns_correct_number_if_there_is_one_number() {
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(1, "ААА", 116))
                    .thenReturn(Optional.of(FIRST_NUMBER));
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(2, "ААА", 116))
                    .thenReturn(Optional.empty());
            when(numbersRepository.save(any())).thenReturn(SECOND_NUMBER);

            var result = numbersService.getNextNumber();
            assertThat(result.getId(), is(equalTo(SECOND_NUMBER.getId())));
            assertThat(result.getRegNumber(), is(equalTo(SECOND_NUMBER.getRegNumber())));
            assertThat(result.getSeries(), is(equalTo(SECOND_NUMBER.getSeries())));
            assertThat(result.getRegionCode(), is(equalTo(SECOND_NUMBER.getRegionCode())));
        }

        @Test
        void returns_correct_number_if_there_are_some_numbers() {
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(1, "ААА", 116))
                    .thenReturn(Optional.of(FIRST_NUMBER));
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(2, "ААА", 116))
                    .thenReturn(Optional.of(SECOND_NUMBER));
            when(numbersRepository.save(any())).thenReturn(THIRD_NUMBER);

            var result = numbersService.getNextNumber();
            assertThat(result.getId(), is(equalTo(THIRD_NUMBER.getId())));
            assertThat(result.getRegNumber(), is(equalTo(THIRD_NUMBER.getRegNumber())));
            assertThat(result.getSeries(), is(equalTo(THIRD_NUMBER.getSeries())));
            assertThat(result.getRegionCode(), is(equalTo(THIRD_NUMBER.getRegionCode())));
        }

        @Test
        void returns_correct_number_if_there_is_no_available_numbers_with_this_series() {
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(anyInt(), eq("ААА"), eq(116)))
                    .thenReturn(Optional.of(LIMIT_NUMBER));
            when(numbersRepository.findByRegNumberAndSeriesAndRegionCode(0, "ААВ", 116))
                    .thenReturn(Optional.empty());
            when(numbersRepository.save(any())).thenReturn(NEW_NUMBER);

            var result = numbersService.getNextNumber();
            assertThat(result.getId(), is(equalTo(NEW_NUMBER.getId())));
            assertThat(result.getRegNumber(), is(equalTo(NEW_NUMBER.getRegNumber())));
            assertThat(result.getSeries(), is(equalTo(NEW_NUMBER.getSeries())));
            assertThat(result.getRegionCode(), is(equalTo(NEW_NUMBER.getRegionCode())));
        }

        @Test
        void on_problems_with_amount_of_entities_throws_exception() {
            when(numbersRepository.countNumbers())
                    .thenReturn(1728000L);
            assertThrows(NumberException.class, () -> numbersService.getNextNumber(), "Available car numbers are over");
        }
    }
}
