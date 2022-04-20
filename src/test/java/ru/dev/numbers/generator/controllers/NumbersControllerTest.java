package ru.dev.numbers.generator.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dev.numbers.generator.exceptions.NumberException;
import ru.dev.numbers.generator.models.CarNumber;
import ru.dev.numbers.generator.services.templates.NumbersService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("NumbersController is working when:")
class NumbersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NumbersService numbersService;

    @BeforeEach
    void setUp() {
        when(numbersService.getNextNumber()).thenReturn(CarNumber.builder()
                .id(1L)
                .regNumber(0)
                .series("ААА")
                .regionCode(116)
                .build());

        when(numbersService.getRandomNumber()).thenReturn(CarNumber.builder()
                .id(2L)
                .regNumber(573)
                .series("ВХМ")
                .regionCode(116)
                .build());
    }

    @Nested
    @DisplayName("getNextNumber() is working")
    class getNextNumberTest {
        @Test
        void on_getNext_returns_correct_number() throws Exception {
            mockMvc.perform(get("/next"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string("А000АА 116 RUS"));

            when(numbersService.getNextNumber()).thenReturn(CarNumber.builder()
                    .id(2L)
                    .regNumber(1)
                    .series("ААА")
                    .regionCode(116)
                    .build());

            mockMvc.perform(get("/next"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string("А001АА 116 RUS"));
        }

        @Test
        void on_problems_throws_exception() throws Exception {

            doThrow(new NumberException("Available car numbers are over"))
                    .when(numbersService)
                            .getNextNumber();

            mockMvc.perform(get("/next"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("Available car numbers are over")));

            doThrow(new NumberException("Cannot increment car number"))
                    .when(numbersService)
                    .getNextNumber();

            mockMvc.perform(get("/next"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("Cannot increment car number")));
        }

        @Test
        void on_problems_with_registration_number_throws_exception() throws Exception {
            when(numbersService.getNextNumber()).thenReturn(CarNumber.builder()
                    .id(222L)
                    .regNumber(1001)
                    .regionCode(116)
                    .series("ВХС")
                    .build());

            mockMvc.perform(get("/next"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("Incorrect registration number")));
        }
    }

    @Nested
    @DisplayName("getRandomNumber() is working")
    class getRandomNumberTest {
        @Test
        void on_getRandom_returns_correct_number() throws Exception {
            mockMvc.perform(get("/random"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string("В573ХМ 116 RUS"));

            when(numbersService.getRandomNumber()).thenReturn(CarNumber.builder()
                    .id(3L)
                    .regNumber(492)
                    .series("ХОК")
                    .regionCode(116)
                    .build());

            mockMvc.perform(get("/random"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string("Х492ОК 116 RUS"));
        }

        @Test
        void on_problems_throws_exception() throws Exception {

            doThrow(new NumberException("Available car numbers are over"))
                    .when(numbersService)
                    .getRandomNumber();

            mockMvc.perform(get("/random"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("Available car numbers are over")));
        }

        @Test
        void on_problems_with_registration_number_throws_exception() throws Exception {
            when(numbersService.getRandomNumber()).thenReturn(CarNumber.builder()
                    .id(223L)
                    .regNumber(1001)
                    .regionCode(116)
                    .series("КАР")
                    .build());

            mockMvc.perform(get("/random"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(400)))
                    .andExpect(jsonPath("$.message", is("Incorrect registration number")));
        }
    }
}
