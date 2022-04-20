package ru.dev.numbers.generator.models;

import lombok.*;
import ru.dev.numbers.generator.exceptions.NumberException;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 3)
    private int regNumber;
    @Column(nullable = false, length = 3)
    private String series;
    @Column(nullable = false, length = 3)
    private int regionCode;
    @Column(nullable = false)
    private Boolean isFree;

    @Override
    public String toString() {
        String regNumber;
        if (this.regNumber < 0 || this.regNumber > 999) {
            throw new NumberException("Incorrect registration number");
        }
        if (this.regNumber < 10) {
            regNumber = "00" + this.regNumber;
        } else if (this.regNumber < 100) {
            regNumber = "0" + this.regNumber;
        } else {
            regNumber = "" + this.regNumber;
        }
        return "" + series.charAt(0) + regNumber + series.substring(1) + " " + regionCode + " RUS";
    }
}
