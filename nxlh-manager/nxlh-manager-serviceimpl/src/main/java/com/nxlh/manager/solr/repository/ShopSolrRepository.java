package com.nxlh.manager.solr.repository;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.model.dto.ShopDTO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Map;


public  interface ShopSolrRepository extends BaseSolrRepository<ShopDTO> {

    Pagination<String> queryByName(String name, PageParameter page, Map<String, SolrQuery.ORDER> sort) throws IOException, SolrServerException;


    Pagination<String> queryByName(String name,String[]  filter, PageParameter page, Map<String, SolrQuery.ORDER> sort) throws IOException, SolrServerException;
}
