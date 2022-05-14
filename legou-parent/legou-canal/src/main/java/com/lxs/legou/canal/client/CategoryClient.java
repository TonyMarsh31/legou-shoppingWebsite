package com.lxs.legou.canal.client;

import com.lxs.legou.item.api.CategoryApi;
import com.lxs.legou.item.po.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "item-service", fallback = CategoryClient.CategoryClientFallback.class)
public interface CategoryClient extends CategoryApi {
    @Component
    @RequestMapping("/item/category-fallback") //注意父接口中也有RequestMapping的配置，此处需要使用不同的路径来避免重复
    class CategoryClientFallback implements CategoryClient {
        private static final Logger LOGGER = LoggerFactory.getLogger(CategoryClientFallback.class);
        @Override
        public List<Category> list(Category category) {
            LOGGER.info("异常发生，进入fallback方法");
            return null;
        }
    }

}
