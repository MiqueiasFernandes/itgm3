package com.itgm.repository;

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

}
