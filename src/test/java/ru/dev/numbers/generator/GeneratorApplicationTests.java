package ru.dev.numbers.generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dev.numbers.generator.controllers.NumbersController;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class GeneratorApplicationTests {

    @Autowired
    private NumbersController numbersController;

    @Test
    void contextLoads() {
        assertThat(numbersController, is(notNullValue()));
    }

}
