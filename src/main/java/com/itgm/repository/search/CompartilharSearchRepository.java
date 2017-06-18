package com.itgm.repository.search;

import com.itgm.domain.Compartilhar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Compartilhar entity.
 */
public interface CompartilharSearchRepository extends ElasticsearchRepository<Compartilhar, Long> {
}
