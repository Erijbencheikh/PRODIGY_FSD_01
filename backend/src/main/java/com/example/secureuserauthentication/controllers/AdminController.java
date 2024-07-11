package com.example.secureuserauthentication.controllers;

import com.example.secureuserauthentication.models.Admin;
import com.example.secureuserauthentication.models.ChangePasswordRequest;
import com.example.secureuserauthentication.models.ERole;
import com.example.secureuserauthentication.models.Role;
import com.example.secureuserauthentication.payload.request.SignupRequest;
import com.example.secureuserauthentication.payload.response.MessageResponse;
import com.example.secureuserauthentication.repository.AdminRepository;
import com.example.secureuserauthentication.repository.RoleRepository;
import com.example.secureuserauthentication.repository.UserRepository;
import com.example.secureuserauthentication.security.jwt.JwtUtils;
import com.example.secureuserauthentication.security.services.RefreshTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class


AdminController {
@Autowired
    private AdminRepository adminRepository;
@Autowired
private JavaMailSender mailSender;
@Autowired
private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping("/all")
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }
    @GetMapping("/getone/{id}")
    public Admin getOneAdmin(@PathVariable Long id){
        return adminRepository.findById(id).get();
    }

    @PostMapping("/save")
    public Admin saveAdmin(@RequestBody Admin a){
        return adminRepository.save(a);
    }


    @PutMapping("/update/{id}")
    public Admin updateAdmin(@PathVariable Long id, @RequestBody Admin admin){
        Admin a = adminRepository.findById(id).get();
        if(a!=null){
            admin.setId(id);
            admin.setPassword(a.getPassword());
            return adminRepository.saveAndFlush(admin);

        }else{
            throw new RuntimeException("Fail");
        }

    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String , String> deleteAdmin(@PathVariable Long id){
        HashMap<String, String> message = new HashMap<>();
        try {
            adminRepository.deleteById(id);
            message.put("Etat","Admin deleted");
        }catch (Exception e){
            message.put("Etat","Admin not deleted");
        }
        return message;
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new provider's account
        Admin admin = new Admin(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {

            Role provRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(provRole);

        }
        admin.setConfirm(false);
        String from ="bcheikherij@gmail.com";
        String to = signUpRequest.getEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><a href=\"http://localhost:8080/admin/updateconfirm?email="+signUpRequest.getEmail()+"\">VERIFY</a><body></HTML>", true);
        mailSender.send(message);
        admin.setRoles(roles);
        adminRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @GetMapping("/updateconfirm")
    public String updateConfirm(String email){
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null){
            admin.setConfirm(true);
            adminRepository.saveAndFlush(admin);
            return "Congratulations email confirm welcome to your account";
        } else{
            throw new RuntimeException("FAIL !");
        }


    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest request){
        String username = authentication.getName();
        Admin admin =  adminRepository.findByUsername(username);
        if (admin == null){
            throw new IllegalArgumentException("Invalid user");
        }
        if (!encoder.matches(request.getOldPassword(), admin.getPassword())){
            return new ResponseEntity<>("Invalid old password", HttpStatus.EXPECTATION_FAILED);

        }
        admin.setPassword(encoder.encode(request.getNewPassword()));
        return ResponseEntity.ok(adminRepository.save(admin));


    }
}
