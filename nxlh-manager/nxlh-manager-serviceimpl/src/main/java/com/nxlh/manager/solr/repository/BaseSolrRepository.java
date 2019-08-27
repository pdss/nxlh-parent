package com.nxlh.manager.solr.repository;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface BaseSolrRepository<M extends Object> {

    void addOrUpdate(M m) throws Exception;

    void addOrUpdateBatch(Collection<M> m) throws  Exception;

    void removeById(String id) throws IOException, SolrServerException;

    List<M> query(SolrQuery query) throws IOException, SolrServerException;


//    List<M> query(String keyword, PageParameter page) throws IOException, SolrServerException;

}