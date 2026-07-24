package org.example.dietlog.repository;

import org.example.dietlog.domain.Category;
import org.example.dietlog.domain.FoodLog;
import org.example.dietlog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {

    Optional<FoodLog> findByIdAndUser(Long id, User user);

    @Query("SELECT f FROM FoodLog f WHERE f.user = :user " +
            "AND f.logDate BETWEEN :from AND :to " +
            "AND (:mealType IS NULL OR f.mealType = :mealType) " +
            "AND (:category IS NULL OR f.category = :category)")
    Page<FoodLog> search(@Param("user") User user,
                         @Param("from") LocalDate from,
                         @Param("to") LocalDate to,
                         @Param("mealType") org.example.dietlog.domain.MealType mealType,
                         @Param("category") Category category,
                         Pageable pageable);
}
