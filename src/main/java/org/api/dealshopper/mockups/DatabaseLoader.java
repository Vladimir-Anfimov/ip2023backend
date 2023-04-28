package org.api.dealshopper.mockups;

import org.api.dealshopper.domain.Role;
import org.api.dealshopper.repositories.RoleRepository;
import org.api.dealshopper.repositories.UserRepository;
import org.api.dealshopper.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * populates the db with mockup data
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public DatabaseLoader(UserRepository userRepository, RoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role role = new Role("ROLE_USER");

        roleRepository.save(role);

        User user1 = new User(
                "test1",
                "Ion",
                "Ionescu",
                "test1@yahoo.com",
                "password",
                "07",
                "123",
                role);

        User user2 = new User(
                "test2",
                "Ion",
                "Popescu",
                "test2@gmail.com",
                "password",
                "07",
                "123",
                role);

        /*User user3 = new User(
                "sam",
                "Sam",
                "Laz",
                "sami@email.com",
                "$2a$10$C7EQR9YL2HQ35D57I.woc.ALHFXdk7EyqYgZ.QOhXguamwqNLmKGO",
                "07",
                "main street",
                role);*/

        // Save the users to the database
        userRepository.saveAll(Arrays.asList(user1, user2));


    }
}
