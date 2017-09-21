package juja.microservices;

import org.springframework.test.context.TestPropertySource;

/**
 * @author Ivan Shapovalov
 */
@TestPropertySource(properties = "app.scheduling.enable=false")
public interface WithoutScheduling {
}
