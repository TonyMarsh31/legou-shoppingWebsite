package com.lxs.legou.search.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "goods_legou", type = "docs_legou", shards = 1, replicas = 0)
public class Goods {

    @Id
    private Long id; //spuId
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; //一个综合字段,用来进行分词搜索，包括了标题，分类和品牌
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle; //卖点
    private Long brandId;// 品牌id
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间
    private List<Long> price;// 价格
    @Field(type = FieldType.Keyword, index = false)
    private String skus; //sku信息-> json结构
    private Map<String, Object> specs; //可搜索的规格参数,key:规格参数名，value：规格参数值(存在多个时为数组)

}
