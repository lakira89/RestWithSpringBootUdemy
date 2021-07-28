package br.com.semeru.services;

import br.com.semeru.data.model.User;
import br.com.semeru.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    UserRepository repository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserName(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + "not found");
        }
    }
}
