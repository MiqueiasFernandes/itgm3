package com.itgm.repository.search;

import com.itgm.domain.Customize;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Customize entity.
 */
public interface CustomizeSearchRepository extends ElasticsearchRepository<Customize, Long> {
}
