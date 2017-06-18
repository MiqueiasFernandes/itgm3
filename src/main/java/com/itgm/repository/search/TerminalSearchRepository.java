package com.itgm.repository.search;

import com.itgm.domain.Terminal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Terminal entity.
 */
public interface TerminalSearchRepository extends ElasticsearchRepository<Terminal, Long> {
}
