package com.itgm.repository;

import com.itgm.domain.Projeto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Projeto entity.
 */
@SuppressWarnings("unused")
public interface ProjetoRepository extends JpaRepository<Projeto,Long> {

    @Query("select projeto from Projeto projeto where projeto.user.login = ?#{principal.username}")
    List<Projeto> findByUserIsCurrentUser();

}
