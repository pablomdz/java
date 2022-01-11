package Argnet.demo;

import Argnet.demo.servicios.userServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private userServicio userservicio;
    public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userservicio)
                .passwordEncoder (new BCryptPasswordEncoder());
    }
}
