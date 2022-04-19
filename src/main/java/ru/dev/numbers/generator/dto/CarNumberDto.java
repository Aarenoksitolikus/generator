package ru.dev.numbers.generator.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dev.numbers.generator.models.CarNumber;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
public class CarNumberDto {
    public static String from(CarNumber number) {
        return number.toString();
    }

    public static List<String> from(List<CarNumber> numbers) {
        return numbers.stream()
                .map(CarNumber::toString)
                .collect(Collectors.toList());
    }

}
