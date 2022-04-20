package ru.dev.numbers.generator.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dev.numbers.generator.models.CarNumber;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public interface NumbersRepository extends JpaRepository<CarNumber, Long> {
    Optional<CarNumber> findByRegNumberAndSeriesAndRegionCode(int regNumber, String series, int regionCode);

    @Query(value = "SELECT id FROM car_number WHERE is_free = true",
            nativeQuery = true)
    List<Long> findAllByIsFreeTrue();

    @Query(value = "SELECT * FROM car_number " +
            "WHERE is_free = true AND reg_number >= ?1 AND series >= ?2 AND region_code = ?3 " +
            "ORDER BY series, reg_number LIMIT 1",
            nativeQuery = true)
    Optional<CarNumber> findNextFree(int regNumber, String series, int regionCode);

    @Query(value = "SELECT COUNT(*) FROM car_number",
            nativeQuery = true)
    Long countNumbers();
}
