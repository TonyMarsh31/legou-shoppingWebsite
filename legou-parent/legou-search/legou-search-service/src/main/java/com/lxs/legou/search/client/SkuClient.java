package com.lxs.legou.search.client;

import com.lxs.legou.item.api.SkuApi;
import com.lxs.legou.item.po.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "item-service", fallback = SkuClient.SkuClientFallback.class)
public interface SkuClient extends SkuApi {

    @Component
    @RequestMapping("/sku-fallback")
            //这个可以避免容器中requestMapping重复
    class SkuClientFallback implements SkuClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(SkuClientFallback.class);

        @Override
        public List<Sku> selectSkusBySpuId(Long spuId) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }
    }

}