package ru.dev.numbers.generator.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dev.numbers.generator.services.templates.NumbersService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("NumbersController is working when:")
class NumbersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NumbersService numbersService;

    @BeforeEach
    void setUp() {

    }
}
