package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.Modelo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Modelo entity.
 */
@SuppressWarnings("unused")
public interface ModeloRepository extends JpaRepository<Modelo,Long> {

    @Query("select modelo from Modelo modelo where modelo.user.login = ?#{principal.username}")
    List<Modelo> findByUserIsCurrentUser();

    @Query("select modelo from Modelo modelo where modelo.user.login = ?#{principal.username}")
    Page<Modelo> findByUserIsCurrentUser(Pageable pageable);

}
