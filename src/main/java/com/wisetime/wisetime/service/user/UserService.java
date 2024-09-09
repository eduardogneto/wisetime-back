package com.wisetime.wisetime.service.user;

import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    

    public User saveOrUpdateUser(User user) {
        // Verifica se o usuário com o mesmo email já existe
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        
        
        
        if (existingUser.isPresent() && user.getId() == null) {
            throw new RuntimeException("Usuário já existe.");
        }
        
        if(!existingUser.isPresent() && user.getId() != null) {
        	throw new RuntimeException("Usuário nao existe.");
        }
        
        if(user.getTag() == null) {
        	throw new RuntimeException("Necessario inserir uma tag.");
        }

        // Cria um novo usuário se o email não estiver em uso
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        // Verifica se o email existe
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Verifica se a senha está correta
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty(); // Retorna vazio se as credenciais não forem válidas
    }
    
    public List<User> getUsersByOrganization(Long organizationId) {
        return userRepository.findByRole_Organization_Id(organizationId);
    }
    
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
    
}
