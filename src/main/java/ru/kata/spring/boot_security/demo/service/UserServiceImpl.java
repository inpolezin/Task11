package ru.kata.spring.boot_security.demo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        List<Role> roles = Collections.singletonList(roleRepository.findRoleByName("ROLE_USER"));
        user.setRoles(roles);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void updateUser(User user) {
        User userDb = findUserById(user.getId());
        userDb.setUsername(user.getUsername());
        userDb.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDb.setRoles(user.getRoles());
        userRepository.save(userDb);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        Optional<User> userOptional = userRepository.findByUsername(s);

        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        } else {
            return userOptional.get();
        }
    }

    @PostConstruct
    @Override
    public void init() {

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
        List<Role> rolesForAdmin = new ArrayList<>();
        List<Role> rolesForUser = new ArrayList<>();
        rolesForAdmin.add(roleAdmin);
        rolesForAdmin.add(roleUser);
        rolesForUser.add(roleUser);
        User admin = new User("admin", "admin", rolesForAdmin);
        User user = new User ("user", "user", rolesForUser);
        userRepository.save(admin);
        userRepository.save(user);
    }
}
