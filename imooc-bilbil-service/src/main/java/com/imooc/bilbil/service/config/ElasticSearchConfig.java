package com.imooc.bilbil.service.config;

import org.apache.rocketmq.client.ClientConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configurable
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    private String url;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        //生成Rest配置 生成rest对es进行操作
        final ClientConfiguration clientConfiguration=ClientConfiguration.builder().connectedTo(url).build();
        return RestClients.create(clientConfiguration).rest();
    }
}
