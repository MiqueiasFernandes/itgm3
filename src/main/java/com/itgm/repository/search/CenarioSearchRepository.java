package com.itgm.repository.search;

import com.itgm.domain.Cenario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cenario entity.
 */
public interface CenarioSearchRepository extends ElasticsearchRepository<Cenario, Long> {
}
