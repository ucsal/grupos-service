package sistemaprojetos.api_controle_projetos.grupos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "sistemaprojetos.api_controle_projetos.grupos.client")
public class GruposApplication {

    public static void main(String[] args) {
        SpringApplication.run(GruposApplication.class, args);
    }
}
