package com.core.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SwaggerProperty {
    @Value("${swagger.server.url}")
    private String serverUrl;
}
