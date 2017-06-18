package com.itgm.repository.search;

import com.itgm.domain.ModeloExclusivo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ModeloExclusivo entity.
 */
public interface ModeloExclusivoSearchRepository extends ElasticsearchRepository<ModeloExclusivo, Long> {
}
