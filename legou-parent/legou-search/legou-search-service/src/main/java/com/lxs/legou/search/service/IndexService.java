package com.lxs.legou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lxs.legou.common.utils.JsonUtils;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.SpecParam;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.item.po.SpuDetail;
import com.lxs.legou.search.client.CategoryClient;
import com.lxs.legou.search.client.SkuClient;
import com.lxs.legou.search.client.SpecParamClient;
import com.lxs.legou.search.client.SpuDetailClient;
import com.lxs.legou.search.dao.GoodsDao;
import com.lxs.legou.search.po.Goods;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 管理索引库
 * - 把spu转换成goods
 * - 删除索引库
 */
@Service
public class IndexService {

    private final CategoryClient categoryClient;
    private final SkuClient skuClient;
    private final SpuDetailClient spuDetailClient;
    private final SpecParamClient specParamClient;
    private final GoodsDao goodsDao;

    public IndexService(@Qualifier("com.lxs.legou.search.client.CategoryClient") CategoryClient categoryClient,
                        @Qualifier("com.lxs.legou.search.client.SkuClient") SkuClient skuClient,
                        @Qualifier("com.lxs.legou.search.client.SpuDetailClient") SpuDetailClient spuDetailClient,
                        @Qualifier("com.lxs.legou.search.client.SpecParamClient") SpecParamClient specParamClient,
                        GoodsDao goodsDao) {
        this.categoryClient = categoryClient;
        this.skuClient = skuClient;
        this.spuDetailClient = spuDetailClient;
        this.specParamClient = specParamClient;
        this.goodsDao = goodsDao;
    }

    /**
     * 根据spu构件索引类型实体类goods
     */
    public Goods buildGoods(Spu spu) {
        Long id = spu.getId();
        //准备数据

        //商品分类名称
        List<String> names = categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String all = spu.getTitle() + " " + StringUtils.join(names, " ");

        //原始skus数据
        List<Sku> skus = skuClient.selectSkusBySpuId(spu.getId());
        //将价格单独取出存放，便于展示
        List<Long> prices = new ArrayList<>();
        //存放其他剩余的sku属性的列表
        List<Map<String, Object>> skuList = new ArrayList<>();
        //处理流程
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            skuMap.put("price", sku.getPrice());
            skuList.add(skuMap);
        }

        //specs
        Map<String, Object> specs = new HashMap<>();
        //spuDetail
        SpuDetail spuDetail = spuDetailClient.edit(spu.getId());

        //通过JsonUtils初始化
        //通用规格参数值
        Map<String, String> genericMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        //sku特有规格参数的值
        Map<String, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<String, List<String>>>() {
                });

        // region 规格参数的处理流程
        //查询分类对应的规格参数
        SpecParam specParam = new SpecParam();
        specParam.setCid(spu.getCid3());
        // 查询规格中，searching属性为True的字段，用于导入索引进行搜索
        specParam.setSearching(true);
        List<SpecParam> params = specParamClient.selectSpecParamApi(specParam);
        // 过滤规格模板,把所有可搜索的信息保存到Map中
        for (SpecParam param : params) {
            //今后显示的名称
            String name = param.getName();//品牌，机身颜色
            //通用参数
            Object value = null;
            if (param.getGeneric()) {
                //通用参数
                value = genericMap.get(name) /*== null ? "暂无信息" : genericMap.get(name)*/;
                if (param.getNumeric()) {
                    //数值类型需要加分段
                    value = this.chooseSegment(value.toString(), param) /*== null ? "暂无信息" : this.chooseSegment(value.toString(), param)*/;
                }
            } else {//特有参数
                value = specialMap.get(name) /*== null ? "暂无信息" : specialMap.get(name)*/;
            }
            if (value == null || value == "") {
                value = "其他";
            }
            specs.put(name, value);
        }
        //endregion

        // region 把相关数据存入goods
        Goods goods = new Goods();
        goods.setId(spu.getId());
        //这里如果要加品牌，可以再写个BrandClient，根据id查品牌
        goods.setAll(all);
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);
        //endregion

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();//4.5  4-5英寸
                }
                break;
            }
        }
        return result;
    }

    /**
     * 根据id删除索引
     *
     * @param id SpuId
     */
    public void deleteIndex(Long id) {
        goodsDao.deleteById(id);
    }

}
