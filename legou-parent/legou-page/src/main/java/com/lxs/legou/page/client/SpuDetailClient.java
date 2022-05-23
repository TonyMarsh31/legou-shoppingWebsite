package com.lxs.legou.page.client;

import com.lxs.legou.item.api.SpuDetailApi;
import com.lxs.legou.item.po.SpuDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "item-service", contextId = "p4", fallback = SpuDetailClient.SpuDetailFallback.class)
public interface SpuDetailClient extends SpuDetailApi {

    @Component
    @RequestMapping("/spu-detail-fallback2") //这个可以避免容器中requestMapping重复
    class SpuDetailFallback implements SpuDetailClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(SpuDetailFallback.class);

        @Override
        public SpuDetail edit(Long id) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }
    }

}