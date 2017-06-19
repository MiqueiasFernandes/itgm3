package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.Base;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Base entity.
 */
@SuppressWarnings("unused")
public interface BaseRepository extends JpaRepository<Base,Long> {

    @Query("select base from Base base where base.projeto.user.login = ?#{principal.username}")
    Page<Base> findByUserIsCurrentUser(Pageable pageable);

}
