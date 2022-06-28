package com.legou.order.client;

import com.lxs.legou.item.api.SkuApi;
import com.lxs.legou.item.po.Sku;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "item-service", /*fallback = SkuClient.SkuClientFallback.class*/ fallbackFactory = SkuClient.SkuClientFallbackFactory.class)
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

        @Override
        public Sku edit(Long id) {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public void decrCount(Integer num, Long skuId) {
            LOGGER.error("异常发生，进入fallback方法");
        }
    }

    @Component
    @RequestMapping("/sku-fallback-factory")
    class SkuClientFallbackFactory implements FallbackFactory<SkuClient> {

        Logger logger = LoggerFactory.getLogger(SkuClientFallbackFactory.class);

        @Override
        public SkuClient create(Throwable throwable) {
            throwable.printStackTrace();
            logger.error(throwable.getMessage());

            //异常直接返回null (有更好的处理方法,比如返回一个包含错误信息的对象)
            return new SkuClient() {
                @Override
                public List<Sku> selectSkusBySpuId(Long spuId) {
                    return null;
                }

                @Override
                public Sku edit(Long id) {
                    return null;
                }

                @Override
                public void decrCount(Integer num, Long skuId) {
                    logger.error("异常发生，进入服务降级方法");
                }
            };
        }
    }

}