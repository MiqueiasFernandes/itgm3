package com.itgm.repository.search;

import com.itgm.domain.Modelo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Modelo entity.
 */
public interface ModeloSearchRepository extends ElasticsearchRepository<Modelo, Long> {
}
