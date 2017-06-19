package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.Cenario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cenario entity.
 */
@SuppressWarnings("unused")
public interface CenarioRepository extends JpaRepository<Cenario,Long> {

    @Query("select cenario from Cenario cenario where cenario.projeto.user.login = ?#{principal.username}")
    Page<Cenario> findByUserIsCurrentUser(Pageable pageable);

}
