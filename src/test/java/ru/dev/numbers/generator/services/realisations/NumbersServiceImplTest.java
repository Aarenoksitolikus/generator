package ru.dev.numbers.generator.services.realisations;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.repositories.NumbersRepository;
import ru.dev.numbers.generator.services.templates.NumbersService;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("NumbersService is working when:")
class NumbersServiceImplTest {

    private final NumbersService numbersService = new NumbersServiceImpl();

    @MockBean
    private NumbersRepository numbersRepository;

    private final CarNumber FIRST_NUMBER = CarNumber.builder()
            .id(1)
            .regionCode(116)
            .regNumber(0)
            .series("ААА")
            .build();

    @BeforeEach
    void setUp() {
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
            assertThat(numbersService.getNextNumber(), is(equalTo(FIRST_NUMBER)));
        }
    }
}
