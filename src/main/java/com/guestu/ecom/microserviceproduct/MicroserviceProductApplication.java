package com.guestu.ecom.microserviceproduct;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceProductApplication.class, args);
    }
    @Bean
    CommandLineRunner start(){
        return args -> {
          /*  if(categoryRepository.findAll().size()==0) {
                Category firstCategory= new Category();
                firstCategory.setName("RootCategory");
                firstCategory.setDescription("Premier category entité technique");

                categoryRepository.save(firstCategory);
               // System.out.println(categoryRepository.findById(1L));
            }*/
        };
    }


}
