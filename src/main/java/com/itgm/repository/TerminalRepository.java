package com.itgm.repository;

import com.itgm.domain.Terminal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Terminal entity.
 */
@SuppressWarnings("unused")
public interface TerminalRepository extends JpaRepository<Terminal,Long> {

}
