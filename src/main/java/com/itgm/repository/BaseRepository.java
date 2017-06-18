package com.itgm.repository;

import com.itgm.domain.Base;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Base entity.
 */
@SuppressWarnings("unused")
public interface BaseRepository extends JpaRepository<Base,Long> {

}
