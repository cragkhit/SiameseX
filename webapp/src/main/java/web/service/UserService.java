package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.PasswordResetToken;
import web.model.User;
import web.repository.PasswordResetTokenRepository;
import web.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRole("USER");
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updatePassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }
}
