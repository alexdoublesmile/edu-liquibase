package edu.plohoy.micro.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

import static java.util.Arrays.asList;

@Configuration
public class RestConfig {
    private static final int CONNECTION_TIMEOUT_MS = 3000;
    private static final int READ_TIMEOUT_MS = 15000;
    private static final int MAX_CONN_TOTAL = 50;
    private static final int MAX_CONN_PER_ROUTE = 30;

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        HttpClient httpClient = new HttpClientBuilder().create()
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory
                = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT_MS);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.setMessageConverters(asList(
                new StringHttpMessageConverter(),
                new FormHttpMessageConverter(),
                new ByteArrayHttpMessageConverter(),
                new MappingJackson2HttpMessageConveter(objectMapper)
        ));

        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.setSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        javaTimeModule.setSerializer(new LocalDateSerializer(DateTimeFormatter.ISO_DATE));

        return new ObjectMapper().registerModule(javaTimeModule)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

}

