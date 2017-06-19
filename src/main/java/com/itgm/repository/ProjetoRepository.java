package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Query("select projeto from Projeto projeto where projeto.user.login = ?#{principal.username}")
    Page<Projeto> findByUserIsCurrentUser(Pageable pageable);

}
