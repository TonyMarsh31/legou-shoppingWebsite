package com.lxs.legou.canal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 因为Page微服务应该只会被canal调用，所以没有在page模块中定义api接口，需要调用的方法直接在本接口中定义
 */
@FeignClient(name="page-service")
@RequestMapping("/page")
public interface PageClient {
    /***
     * 根据SpuID生成静态页
     * @param id SpuId商品主键
     * @return 消息响应
     */
    @RequestMapping("/createHtml/{id}")
    ResponseEntity<String> createHtml(@PathVariable(name = "id") Long id);

    /***
     * 根据SpuID删除静态页
     * @param id SpuId商品主键
     * @return 消息响应
     */
    @DeleteMapping("/deleteHtml/{id}")
    ResponseEntity<String> deleteHtml(@PathVariable(name = "id") Long id);

}
