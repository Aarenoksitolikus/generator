package ru.dev.numbers.generator.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dev.numbers.generator.models.CarNumber;

import java.util.Optional;

@Repository
@Profile("jpa")
public interface NumbersRepository extends JpaRepository<CarNumber, Long> {
    Optional<CarNumber> findByRegNumberAndSeriesAndRegionCode(int regNumber, String series, int regionCode);

    @Query(value = "SELECT COUNT(*) FROM car_number",
            nativeQuery = true)
    Long countNumbers();
}
