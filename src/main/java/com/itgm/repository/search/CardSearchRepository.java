package com.itgm.repository.search;

import com.itgm.domain.Card;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Card entity.
 */
public interface CardSearchRepository extends ElasticsearchRepository<Card, Long> {
}
