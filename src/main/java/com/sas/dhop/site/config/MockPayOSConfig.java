package com.sas.dhop.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@Configuration
public class MockPayOSConfig {
    
    @Bean
    @Primary
    public PayOS mockPayOS() {
        // Sử dụng giả lập hoặc đối tượng giả
        PayOS mockPayOS = new PayOS("mock-client-id", "mock-api-key", "mock-checksum-key");
        
        // Chúng ta không override các phương thức của PayOS mà để nó trả về giá trị mặc định
        // hoặc sẽ xử lý lỗi trong các service sử dụng nó
        
        return mockPayOS;
    }
} 