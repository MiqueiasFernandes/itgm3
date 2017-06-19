package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.Compartilhar;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Compartilhar entity.
 */
@SuppressWarnings("unused")
public interface CompartilharRepository extends JpaRepository<Compartilhar,Long> {

    @Query("select compartilhar from Compartilhar compartilhar where compartilhar.remetente.login = ?#{principal.username}")
    List<Compartilhar> findByRemetenteIsCurrentUser();

    @Query("select compartilhar from Compartilhar compartilhar where compartilhar.destinatario.login = ?#{principal.username}")
    List<Compartilhar> findByDestinatarioIsCurrentUser();

    @Query("select compartilhar from Compartilhar compartilhar where compartilhar.destinatario.login = ?#{principal.username}")
    Page<Compartilhar> findByDestinatarioIsCurrentUser(Pageable pageable);

    @Query("select compartilhar from Compartilhar compartilhar where compartilhar.remetente.login = ?#{principal.username}")
    Page<Compartilhar> findByRemetenteIsCurrentUser(Pageable pageable);

}
