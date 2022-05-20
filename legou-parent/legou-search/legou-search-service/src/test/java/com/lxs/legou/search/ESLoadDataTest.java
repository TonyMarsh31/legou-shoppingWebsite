package com.lxs.legou.search;

import com.lxs.legou.item.po.Spu;
import com.lxs.legou.search.client.SpuClient;
import com.lxs.legou.search.dao.GoodsDao;
import com.lxs.legou.search.po.Goods;
import com.lxs.legou.search.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class ESLoadDataTest {

    @Autowired
    private IndexService indexService;
    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void loadData() {
        List<Spu> spus = spuClient.selectAll();

        //spu转换成goods
        List<Goods> goods = spus.stream().map(spu -> this.indexService.buildGoods(spu)).collect(Collectors.toList());
        //System.out.println(goods);

        //导入索引库
        goodsDao.saveAll(goods);

    }

}

