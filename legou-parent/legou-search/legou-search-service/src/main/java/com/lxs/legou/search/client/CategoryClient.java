package com.lxs.legou.search.client;

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
    @RequestMapping("/item/category-fallback") //这个可以避免容器中requestMapping重复
    class CategoryClientFallback implements CategoryClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(CategoryClientFallback.class);

        @Override
        public List<String> queryNameByIds(List<Long> ids) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public List<Category> list(Category category) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public Category edit(Long id) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }
    }

}