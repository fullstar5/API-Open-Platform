package com.star.starapiclientsdk;

import com.star.starapiclientsdk.client.StarApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties("starapi.client")
@Data
@ComponentScan
public class StarApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public StarApiClient starApiClient() {
        return new StarApiClient(accessKey, secretKey);
    }

}
