package com.common.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 11:28
 */
@Configuration
public class ModelMapperConfig {
    @Bean
    @Primary
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
