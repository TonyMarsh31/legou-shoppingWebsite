package com.lxs.legou.page.service.impl;

import com.lxs.legou.common.utils.JsonUtils;
import com.lxs.legou.item.po.Category;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.item.po.SpuDetail;
import com.lxs.legou.page.client.SkuClient;
import com.lxs.legou.page.client.SpuClient;
import com.lxs.legou.page.client.SpuDetailClient;
import com.lxs.legou.page.service.PageService;
import com.lxs.legou.page.client.CategoryClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    private final SpuClient spuClient;
    private final CategoryClient categoryClient;
    private final SpuDetailClient spuDetailClient;
    private final SkuClient skuClient;

    @Value("${pagepath}")
    private String pagepath;

    private final TemplateEngine templateEngine;

    public PageServiceImpl(@Qualifier("com.lxs.legou.page.client.SpuClient") SpuClient spuClient,
                           @Qualifier("com.lxs.legou.page.client.CategoryClient") CategoryClient categoryClient,
                           @Qualifier("com.lxs.legou.page.client.SpuDetailClient") SpuDetailClient spuDetailClient,
                           @Qualifier("com.lxs.legou.page.client.SkuClient") SkuClient skuClient,
                           TemplateEngine templateEngine) {
        this.spuClient = spuClient;
        this.categoryClient = categoryClient;
        this.spuDetailClient = spuDetailClient;
        this.skuClient = skuClient;
        this.templateEngine = templateEngine;
    }


    /**
     * 生成静态页
     *
     * @param id 商品SpuId
     */
    @Override
    public void createPageHtml(Long id) {
        //先删除同名文件 (这样新增和更新操作的处理流程是一致的了,更新的逻辑就是删除后创建新文件)
        removePageHtml(id);

        //再创建html文件
        //指定静态页面存储位置
        File dir = new File(pagepath);
        //若不存在，创建目录
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
        }
        //在目录下创建 html文件，文件名为商品id
        File pageHtmlFile = new File(dir, id + ".html");

        //手动渲染html文件, 公式 : html = 模板 + contextMap
        //context部分
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(id);
        context.setVariables(dataModel);
        //thymeleaf模板渲染部分
        try {
            PrintWriter writer = new PrintWriter(pageHtmlFile, "UTF-8");
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除静态页
     *
     * @param id 商品SpuId
     */
    @Override
    public void removePageHtml(Long id) {
        File dir = new File(pagepath);
        File pageHtmlFile = new File(dir, id + ".html");
        if(pageHtmlFile.isFile() && pageHtmlFile.exists()){
            boolean delete = pageHtmlFile.delete();
        }
    }

    /**
     * 获取用于构建静态模板的数据并封装
     *
     * @param spuId 商品SpuId
     * @return thymeleaf模板渲染所需要的数据
     */
    private Map<String, Object> buildDataModel(Long spuId) {
        //构建数据模型 (模板渲染需要什么数据,就获取什么数据,最后封装返回)
        Map<String, Object> dataMap = new HashMap<>();
        //获取spu 和SKU列表
        Spu spu = spuClient.edit(spuId);
        Category c1 = categoryClient.edit(spu.getCid1());
        //获取分类信息
        dataMap.put("category1", categoryClient.edit(spu.getCid1()));
        dataMap.put("category2", categoryClient.edit(spu.getCid2()));
        dataMap.put("category3", categoryClient.edit(spu.getCid3()));

        List<Sku> skus = skuClient.selectSkusBySpuId(spu.getId());
        List<String> images = new ArrayList<>();
        for (Sku sku : skus) {
            images.add(sku.getImages());
        }
        dataMap.put("imageList", images);

        SpuDetail spuDetail = spuDetailClient.edit(spu.getId());
        Map<String, Object> genericMap = JsonUtils.parseMap(spuDetail.getSpecialSpec(), String.class, Object.class);

        dataMap.put("specificationList", genericMap);

        dataMap.put("spu", spu);
        dataMap.put("spuDetail", spuDetail);

        //根据spuId查询Sku集合
        dataMap.put("skuList", skus);
        return dataMap;
    }

}
