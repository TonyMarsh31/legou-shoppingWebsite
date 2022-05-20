package com.lxs.legou.search.dao;

import com.lxs.legou.search.po.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsDao extends ElasticsearchRepository<Goods, Long> {

}
