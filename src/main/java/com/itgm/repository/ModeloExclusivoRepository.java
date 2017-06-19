package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.ModeloExclusivo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ModeloExclusivo entity.
 */
@SuppressWarnings("unused")
public interface ModeloExclusivoRepository extends JpaRepository<ModeloExclusivo,Long> {

    @Query("select modeloExclusivo from ModeloExclusivo modeloExclusivo where modeloExclusivo.modelo.user.login = ?#{principal.username}")
    Page<ModeloExclusivo> findByUserIsCurrentUser(Pageable pageable);

}
