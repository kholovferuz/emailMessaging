package uz.pdp.app_jwt_real_email.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.app_jwt_real_email.DTO.UserDTO;
import uz.pdp.app_jwt_real_email.Entity.User;
import uz.pdp.app_jwt_real_email.Entity.enums.RoleName;
import uz.pdp.app_jwt_real_email.Repository.RoleRepository;
import uz.pdp.app_jwt_real_email.Repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse registerUser(UserDTO userDTO) {
        // check email
        boolean byEmail = userRepository.existsByEmail(userDTO.getEmail());
        if (byEmail) {
            return new ApiResponse("User with this email already exists!", false);
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));

        user.setEmailCode(UUID.randomUUID().toString());

        userRepository.save(user);

        // email ga yuborish
        sendEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Successfully registered. Please use the link sent to your email to verify your account!", true);
    }

    public Boolean sendEmail(String email, String emailCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(email);
            message.setSubject("Subject mail");
            message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "+&email=" + email + "'>Tasdiqlang</a>");
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user=optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account verified",true);
        }
        return new ApiResponse("Account already verified",false);
    }
}
