package com.nxlh.manager.solr.repository.imp;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.solr.repository.ShopSolrRepository;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShopSolrRepositoryImpl extends BaseSolrRepositoryImpl<ShopDTO> implements ShopSolrRepository {


    @Override
    public List<ShopDTO> query(SolrQuery query) throws IOException, SolrServerException {
        var resp = this.solrClient.query(query);
        return resp.getBeans(ShopDTO.class);
    }

    @Override
    public Pagination<String> queryByName(String name, PageParameter page, Map<String, SolrQuery.ORDER> sort) throws IOException, SolrServerException {
        return this.queryByName(name, null, page, sort);
    }

    @Override
    public Pagination<String> queryByName(String name, String[] filter, PageParameter page, Map<String, SolrQuery.ORDER> sort) throws IOException, SolrServerException {
        //分词器 分词
//        var list = TokenizerUtils.segment(name);
        var query = new SolrQuery("*:*");
        if (page.getPageIndex() < 1) {
            page.setPageIndex(1);
        }
        //启用edismax选项
        query.set("defType", "edismax");
        query.set("df", "shopname");
        //设置权重
        query.set("qf", "shopname^1");
        query.setSort("sort", SolrQuery.ORDER.desc);
        query.setQuery(name);
        query.setStart((page.getPageIndex() - 1) * page.getPageSize());
        query.setRows(page.getPageSize());
        query.setHighlight(true);
        if (ArrayUtils.isNotEmpty(filter)) {
            query.setFilterQueries(filter);
        }
        if (MapUtils.isNotEmpty(sort)) {
            sort.forEach((key, val) -> query.addSort(key, val));
        }
//        query.addHighlightField("shopname");
        var resp = this.solrClient.query(query);
        var ids = new ArrayList<String>();
        for (var doc : resp.getResults()) {
            ids.add(doc.getFieldValue("id").toString());
        }

        // var totalPages = resp.getResults().getNumFound() % page.getPageSize() == 0 ? resp.getResults().getNumFound() / page.getPageSize() : resp.getResults().getNumFound() / page.getPageSize() + 1;
        var pagination = new Pagination<String>(page.getPageIndex(), page.getPageSize(), (int) resp.getResults().getNumFound());
        pagination.setList(ids);
        return pagination;
    }
}
