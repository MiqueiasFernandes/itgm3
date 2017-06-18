package com.itgm.repository;

import com.itgm.domain.ModeloExclusivo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ModeloExclusivo entity.
 */
@SuppressWarnings("unused")
public interface ModeloExclusivoRepository extends JpaRepository<ModeloExclusivo,Long> {

}
