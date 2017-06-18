package com.itgm.repository;

import com.itgm.domain.Customize;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Customize entity.
 */
@SuppressWarnings("unused")
public interface CustomizeRepository extends JpaRepository<Customize,Long> {

}
