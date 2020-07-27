package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.modelmapper.EnhancedModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {

    @Bean
    fun modelMapper(): EnhancedModelMapper {
        return EnhancedModelMapper()
    }

}
