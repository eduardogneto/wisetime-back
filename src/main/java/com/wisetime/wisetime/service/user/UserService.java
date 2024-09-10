package com.wisetime.wisetime.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisetime.wisetime.DTO.team.TeamDTO;
import com.wisetime.wisetime.DTO.user.UserRegisterDTO;
import com.wisetime.wisetime.DTO.user.UserResponseDTO;
import com.wisetime.wisetime.models.team.Team;
import com.wisetime.wisetime.models.user.User;
import com.wisetime.wisetime.repository.team.TeamRepository;
import com.wisetime.wisetime.repository.user.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    public Optional<User> findEntityById(Long userId) {
        return userRepository.findById(userId); 
    }

//    public User saveOrUpdateUser(User user) {
//        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
//
//        if (existingUser.isPresent() && user.getId() == null) {
//            throw new RuntimeException("Usuário já existe.");
//        }
//
//        if (!existingUser.isPresent() && user.getId() != null) {
//            throw new RuntimeException("Usuário não existe.");
//        }
//
//        if (user.getTag() == null) {
//            throw new RuntimeException("Necessário inserir uma tag.");
//        }
//
//        return userRepository.save(user);
//    }
//
//    public Optional<UserResponseDTO> login(String email, String password) {
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            if (user.getPassword().equals(password)) {
//                return Optional.of(mapToDTO(user)); 
//            }
//        }
//        return Optional.empty();
//    }

    public List<UserResponseDTO> getUsersByOrganization(Long organizationId) {
        List<User> users = userRepository.findByTeam_Organization_Id(organizationId);
        return users.stream().map(this::mapToDTO).collect(Collectors.toList()); 
    }

//    public UserResponseDTO registerOrUpdate(UserRegisterDTO registerDTO) {
//        User user = new User();
//        user.setId(registerDTO.getId());
//        user.setName(registerDTO.getName());
//        user.setEmail(registerDTO.getEmail());
//        user.setPassword(registerDTO.getPassword());
//        user.setTag(registerDTO.getTag());
//
//        Team team = teamRepository.findById(registerDTO.getTeamId())
//                .orElseThrow(() -> new RuntimeException("Nenhum cargo encontrado para o id: " + registerDTO.getTeamId()));
//
//        user.setTeam(team);
//
//        User savedUser = saveOrUpdateUser(user);
//
//        return mapToDTO(savedUser); 
//    }

    private UserResponseDTO mapToDTO(User user) {
        Team team = user.getTeam(); 
        TeamDTO teamDTO = new TeamDTO(team.getId(), team.getName(), team.getOrganization().getId());

        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getTeam().getOrganization().getId(),
            teamDTO,  
            user.getTag()
        );
    }
}
