package com.itgm.repository.search;

import com.itgm.domain.Prognose;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prognose entity.
 */
public interface PrognoseSearchRepository extends ElasticsearchRepository<Prognose, Long> {
}
