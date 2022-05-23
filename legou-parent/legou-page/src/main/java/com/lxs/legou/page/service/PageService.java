package com.lxs.legou.page.service;

public interface PageService {

    /**
     * 生成静态页
     * @param id 商品SpuId
     */
    void createPageHtml(Long id);

    /**
     * 删除静态页
     * @param id 商品SpuId
     */
    void removePageHtml(Long id);
}