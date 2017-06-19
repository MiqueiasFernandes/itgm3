package com.itgm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itgm.domain.Customize;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Customize entity.
 */
@SuppressWarnings("unused")
public interface CustomizeRepository extends JpaRepository<Customize,Long> {

    @Query("select customize from Customize customize where customize.user.login = ?#{principal.username}")
    Page<Customize> findByUserIsCurrentUser(Pageable pageable);

}
