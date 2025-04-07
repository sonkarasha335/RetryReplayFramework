package com.retryreplayframwork.config;

@Bean
public WebMvcConfigurer corsConfigurer(){
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
        }
    };
}
