package com.itgm.repository;

import com.itgm.domain.Card;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
public interface CardRepository extends JpaRepository<Card,Long> {

    @Query("select card from Card card where card.cenario.projeto.user.login = ?#{principal.username}")
    Page<Card> findByUserIsCurrentUser(Pageable pageable);

}
