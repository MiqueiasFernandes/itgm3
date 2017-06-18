package com.itgm.repository;

import com.itgm.domain.Card;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
public interface CardRepository extends JpaRepository<Card,Long> {

}
