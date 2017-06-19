package com.itgm.repository.search;

import com.itgm.domain.Projeto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Projeto entity.
 */
public interface ProjetoSearchRepository extends ElasticsearchRepository<Projeto, Long> {
}
