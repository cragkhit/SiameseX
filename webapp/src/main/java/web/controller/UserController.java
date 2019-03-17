package web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.EmailService;
import web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private EmailService emailService;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(EmailService emailService, UserService userService, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        User currentUser = userService.findUserByEmail(principal.getName());

        modelAndView.addObject("user", currentUser);
        modelAndView.setViewName("profile");

        return modelAndView;
    }

    @GetMapping("/change-password")
    public ModelAndView changePassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("change-password");

        return modelAndView;
    }

    @GetMapping("/forgot-password")
    public ModelAndView forgotPassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forgot-password");

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        logger.info("user: {}", user.getEmail());

        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }

        if (bindingResult.hasErrors()) {
            logger.info("error: {}", bindingResult.getFieldError());

            modelAndView.addObject("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            modelAndView.setViewName("register");
        }
        else {
            logger.info("success");

            userService.createUser(user);
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("redirect:/login?registered");
        }
        return modelAndView;
    }

    @PostMapping("/profile")
    public ModelAndView updateProfile(Principal principal, @Valid User user) {
        logger.info("user: {}", principal.getName());

        ModelAndView modelAndView = new ModelAndView();

        User currentUser = userService.findUserByEmail(principal.getName());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());

        userService.updateUser(currentUser);

        modelAndView.addObject("message", "Your profile has been updated successfully.");
        modelAndView.setViewName("profile");
        return modelAndView;
    }

    @PostMapping("/change-password")
    public ModelAndView updatePassword(Principal principal, @Valid User user, BindingResult bindingResult) {
        logger.info("user: {}", principal.getName());

        ModelAndView modelAndView = new ModelAndView();

        User currentUser = userService.findUserByEmail(principal.getName());
        if (!passwordEncoder.matches(user.getPassword(), currentUser.getPassword())) {
            bindingResult
                    .rejectValue("password", "error.user",
                            "Your current password is incorrect");
        }

        if (bindingResult.hasErrors()) {
            logger.info("error: {}", bindingResult.getFieldError());

            modelAndView.setViewName("redirect:/change-password?error");
        } else {
            logger.info("success");

            currentUser.setPassword(user.getNewPassword());
            userService.updatePassword(currentUser);

            modelAndView.setViewName("redirect:/change-password?success");
        }

        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public ModelAndView processForgotPasswordForm(HttpServletRequest request, @Valid User user, BindingResult bindingResult) {
        logger.info("user: {}", user.getEmail());

        ModelAndView modelAndView = new ModelAndView();

        User currentUser = userService.findUserByEmail(user.getEmail());
        if (currentUser == null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "The email does not exist in the system");
        }

        if (bindingResult.hasErrors()) {
            logger.info("error: {}", bindingResult.getFieldError());

            modelAndView.setViewName("redirect:/forgot-password?error");
        } else {
            logger.info("success");

            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(currentUser, token);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();

            passwordResetEmail.setFrom("support@demo.com");
            passwordResetEmail.setTo(currentUser.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset-password?token=" + token);

            emailService.sendEmail(passwordResetEmail);

            modelAndView.setViewName("redirect:/forgot-password?success");
        }
        return modelAndView;
    }
}
