package ru.stas.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stas.demo.config.SecurityConfig;

import ru.stas.demo.model.User;
import ru.stas.demo.repo.UserRepo;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findById(long id) {
        return userRepo.getOne(id);
    }
    public User findByUsername(String username){
        return userRepo.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null){
            throw  new UsernameNotFoundException(String.format("User '%s' not found",username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getAuthorities());
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
    @Transactional
    public void saveUser(User user) {
        String password = user.getPassword();
        if (!((password.startsWith("$2a$") || password.startsWith("$2b$")) && password.length() == 60)) {
            user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        }
        userRepo.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepo.delete(user);
    }


}