package com.itgm.repository;

import com.itgm.domain.Terminal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Terminal entity.
 */
@SuppressWarnings("unused")
public interface TerminalRepository extends JpaRepository<Terminal,Long> {
    @Query("select terminal from Terminal terminal where terminal.cenario.projeto.user.login = ?#{principal.username}")
    Page<Terminal> findByUserIsCurrentUser(Pageable pageable);
}
