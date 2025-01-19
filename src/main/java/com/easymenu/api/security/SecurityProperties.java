package com.easymenu.api.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Getter
public class SecurityProperties {
    @Value("${security.secret-key}")
    private String secretKey;
    @Value("${security.jwt-ttl}")
    private Integer jwtTtl;
}
