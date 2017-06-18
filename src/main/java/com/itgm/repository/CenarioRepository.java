package com.itgm.repository;

import com.itgm.domain.Cenario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cenario entity.
 */
@SuppressWarnings("unused")
public interface CenarioRepository extends JpaRepository<Cenario,Long> {

}
