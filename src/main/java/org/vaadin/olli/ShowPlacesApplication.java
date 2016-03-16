package org.vaadin.olli;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.olli.domain.Location;
import org.vaadin.olli.domain.Role;
import org.vaadin.olli.domain.Status;
import org.vaadin.olli.domain.User;
import org.vaadin.olli.repository.LocationsRepository;
import org.vaadin.olli.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@Configuration
@ComponentScan
public class ShowPlacesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowPlacesApplication.class, args);
    }

    @Bean
    public InitializingBean insertDefaultUsers() {
        return new InitializingBean() {
            @Autowired
            private UserRepository userRepository;
            @Autowired
            LocationsRepository locationsRepository;

            @Override
            public void afterPropertiesSet() {
                addUser("admin", "admin");
                addUser("user", "user");

                generateExpenses();
            }

            private void generateDemoData() {
                int howMany = 200;
                ArrayList<Location> locations = new ArrayList<>(howMany);
                Random random = new Random();
                for (int i = 0; i < howMany; i++) {
                    Location location = new Location();
                    locations.add(location);
                }

                locationsRepository.save(locations);
            }

            private void addUser(String username, String password) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.save(user);
            }
        };
    }
}
