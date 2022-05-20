package com.lxs.legou.search.service;

import com.lxs.legou.item.po.Brand;
import com.lxs.legou.item.po.Category;
import com.lxs.legou.item.po.SpecParam;
import com.lxs.legou.search.client.BrandClient;
import com.lxs.legou.search.client.CategoryClient;
import com.lxs.legou.search.client.SpecParamClient;
import com.lxs.legou.search.dao.GoodsDao;
import com.lxs.legou.search.po.Goods;
import com.lxs.legou.search.po.SearchRequest;
import com.lxs.legou.search.po.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private final BrandClient brandClient;
    private final CategoryClient categoryClient;
    private final SpecParamClient specParamClient;
    private final GoodsDao goodsDao;

    public SearchService(@Qualifier("com.lxs.legou.search.client.BrandClient") BrandClient brandClient,
                         @Qualifier("com.lxs.legou.search.client.CategoryClient") CategoryClient categoryClient,
                         @Qualifier("com.lxs.legou.search.client.SpecParamClient") SpecParamClient specParamClient,
                         GoodsDao goodsDao) {
        this.brandClient = brandClient;
        this.categoryClient = categoryClient;
        this.specParamClient = specParamClient;
        this.goodsDao = goodsDao;
    }

    /**
     * 处理搜索请求,(根据key属性)进行全文检索
     *
     * @param searchRequest 前端传递的搜索请求,包含搜索条件
     * @return SearchResult类对象
     */
    public SearchResult search(SearchRequest searchRequest) {

        //region非空判断
        String key = searchRequest.getKey();
        if (key == null) {
            return null;
        }
        //endregion

        //创建elasticsearch查询对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //根据前后端约定,设置要返回的查询字段 (FetchSourceFilter的第一个参数为要保留的字段,第二个参数为要排除的字段)
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        //region向查询对象中添加过滤条件 (使用自定义的封装方法,其内部定义了具体的过滤条件)
        BoolQueryBuilder boolQueryBuilder = buildBasicQueryWithFilter(searchRequest);
        queryBuilder.withQuery(boolQueryBuilder);
        //endregion

        //region向查询对象中添加分页条件
        int page = searchRequest.getPage() - 1; //es分页从0开始
        Integer size = searchRequest.getSize();
        queryBuilder.withPageable(PageRequest.of(page, size));
        //endregion

        //region向查询对象中添加排序条件
        String sortBy = searchRequest.getSortBy();
        Boolean desc = searchRequest.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
        //endregion

        //region向查询对象中添加聚合条件
        /*业务约定,必定聚合查询品牌和分类数据,所以这里为静态代码,
        其余规格参数的聚合查询的实现位于自定义方法getSpecs中*/
        String brandAggsName = "brands";
        String categoryAggsName = "categories";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggsName).field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggsName).field("cid3"));
        //endregion

        //进行查询操作,得到包含聚合查询结果的结果集
        AggregatedPage<Goods> goodsResult = (AggregatedPage<Goods>) goodsDao.search(queryBuilder.build());

        //region对聚合结果进行处理
        //提取品牌和分类数据
        List<Brand> brands = getBrandAgg(brandAggsName, goodsResult);
        List<Category> categories = getCategoryAgg(categoryAggsName, goodsResult);
        //region提取其他的动态统计规格参数
        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
            //当返回的分类结果只有一个时，才统计规格参数
            specs = getSpecs(categories.get(0).getId(), boolQueryBuilder);
        }
        //endregion
        //endregion

        //region对分页数据进行处理 - 获取总页数total和总行数totalPages
        long total = goodsResult.getTotalElements(); //总行数
        long totalPages = goodsResult.getTotalPages(); //总页数
        //endregion

        //将elasticsearch搜索返回的信息封装
        return new SearchResult(total, totalPages, goodsResult.getContent(), categories, brands, specs);
    }

    /**
     * 创建elasticsearchQuery对象,
     * 默认根据searchRequest的key属性,对es数据集中的all属性进行全局检索
     * 如果searchRequest对象中存在filter属性,则进行解析然后添加其为搜索的过滤条件
     *
     * @param searchRequest 前端传递的搜索请求的反序列化对象
     * @return 构建好过滤条件的基础Query
     */
    private BoolQueryBuilder buildBasicQueryWithFilter(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //默认根据searchRequest的key属性,对es数据集中的all属性进行全局检索
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        //获取前端发送的过滤条件
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : searchRequest.getFilter().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //商品分类和品牌不需要加.keywords，因为在elasticsearch的index中已经做过mapping了
            // 而"spec."前缀的属性做的是动态映射，所以规格参数要加.keywords ,更多解释详见笔记
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            //加入termQuery聚合参数中
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        return boolQueryBuilder.filter(filterQueryBuilder);
    }

    /**
     * 品牌聚合数据处理
     *
     * @param brandAggsName 聚合查询中的terms名称
     * @param goodsResult   elasticsearch查询后的结果集
     * @return 从聚合结果中提取的Brand列表
     */
    private List<Brand> getBrandAgg(String brandAggsName, AggregatedPage<Goods> goodsResult) {
        LongTerms longTerms = (LongTerms) goodsResult.getAggregation(brandAggsName);
        List<Long> brandIds = new ArrayList<>();
        for (LongTerms.Bucket bucket : longTerms.getBuckets()) {
            //terms聚合查询返回的Bucket结构: {"key":value ,"doc_count":value}
            //业务中暂时只需要用到key,其value是BrandId
            brandIds.add(bucket.getKeyAsNumber().longValue());
        }
        return brandClient.selectBrandByIds(brandIds);
    }

    /**
     * 分类聚合数据的处理
     *
     * @param categoryAggsName 聚合查询中的terms名称
     * @param goodsResult      elasticsearch查询后的结果集
     * @return 从聚合结果中提取的Category列表
     */
    private List<Category> getCategoryAgg(String categoryAggsName, AggregatedPage<Goods> goodsResult) {
        LongTerms longTerms = (LongTerms) goodsResult.getAggregation(categoryAggsName);
        List<Long> categoryIds = new ArrayList<>();
        for (LongTerms.Bucket bucket : longTerms.getBuckets()) {
            //terms聚合查询返回的Bucket结构: {"key":value ,"doc_count":value}
            //业务中暂时只需要用到key,其value是CategoryId
            categoryIds.add(bucket.getKeyAsNumber().longValue());
        }
        //通过CategoryId查询分类名
        List<String> names = this.categoryClient.queryNameByIds(categoryIds);
        //将id和name进行封装后返回Category列表
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            Category category = new Category();
            category.setId(categoryIds.get(i));
            category.setTitle(names.get(i));
            categories.add(category);
        }
        return categories;
    }

    /**
     * 动态获取规格参数,进行elasticsearch聚合统计,返回自定义结果(规格参数)集合
     *
     * @param cid   分类信息主键
     * @param query elasticsearchQuery对象
     * @return 自定义结果(规格参数)集合, 结构参考Goods.specs
     */
    private List<Map<String, Object>> getSpecs(Long cid, QueryBuilder query) {
        /*
            - 首先根据分类id 查询该分类对应的用于搜索的的规格参数
            - 创建NativeQueryBuilder,使用一样的查询条件key等。。。
            - 把可搜索的规格参数，依次添加到聚合查询aggs参数中
            - 处理规格参数搜索的结果，k：搜索的参数名,options：聚合结果的数组
         */

        List<Map<String, Object>> specs = new ArrayList<>();

        SpecParam sp = new SpecParam();
        sp.setCid(cid);
        sp.setSearching(true);
        List<SpecParam> specParams = this.specParamClient.selectSpecParamApi(sp);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //在做聚合之前先做查询，只有符合条件的规格参数才应该被查出来
        queryBuilder.withQuery(query);

        for (SpecParam specParam : specParams) {
            String name = specParam.getName();//操作系统，cpu核数
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }

        AggregatedPage<Goods> aggs = (AggregatedPage<Goods>) this.goodsDao.search(queryBuilder.build());
        Map<String, Aggregation> stringAggregationMap = aggs.getAggregations().asMap();

        //聚合搜索返回的结果集处理
        for (SpecParam specParam : specParams) {
            Map<String, Object> spec = new HashMap<>();
            String name = specParam.getName();
            if (stringAggregationMap.get(name) instanceof StringTerms) {
                StringTerms stringTerms = (StringTerms) stringAggregationMap.get(name);
                List<String> val = new ArrayList<>();
                for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                    val.add(bucket.getKeyAsString());
                }
                spec.put("k", name);//内存，存储空间，屏幕尺寸
                spec.put("options", val);
                specs.add(spec);
            }
        }

        return specs;
    }

}