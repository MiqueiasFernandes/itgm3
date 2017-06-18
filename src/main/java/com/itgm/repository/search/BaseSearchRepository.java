package com.itgm.repository.search;

import com.itgm.domain.Base;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Base entity.
 */
public interface BaseSearchRepository extends ElasticsearchRepository<Base, Long> {
}
