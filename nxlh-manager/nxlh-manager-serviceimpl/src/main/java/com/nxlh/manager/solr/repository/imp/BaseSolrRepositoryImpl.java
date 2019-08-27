package com.nxlh.manager.solr.repository.imp;

import com.nxlh.manager.solr.repository.BaseSolrRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
public abstract class BaseSolrRepositoryImpl<M extends Object> implements BaseSolrRepository<M> {

    @Autowired
    protected SolrClient solrClient;

    @Override
    public void addOrUpdate(Object o) throws IOException, SolrServerException {
            this.solrClient.addBean(o);
            this.solrClient.commit();
    }

    @Override
    public void addOrUpdateBatch(Collection<M> m) throws Exception {
          this.solrClient.addBeans(m);
          this.solrClient.commit();
    }

    @Override
    public void removeById(String id) throws IOException, SolrServerException {
            this.solrClient.deleteById(id);
            this.solrClient.commit();
    }

    @Override
    public abstract List<M> query(SolrQuery query) throws IOException, SolrServerException;
}
