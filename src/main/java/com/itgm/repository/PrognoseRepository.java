package com.itgm.repository;

import com.itgm.domain.Prognose;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prognose entity.
 */
@SuppressWarnings("unused")
public interface PrognoseRepository extends JpaRepository<Prognose,Long> {

}
