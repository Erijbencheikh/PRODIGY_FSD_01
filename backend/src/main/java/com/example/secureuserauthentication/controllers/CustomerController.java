package com.example.secureuserauthentication.controllers;

import com.example.secureuserauthentication.models.ChangePasswordRequest;
import com.example.secureuserauthentication.models.Customer;
import com.example.secureuserauthentication.models.ERole;
import com.example.secureuserauthentication.models.Role;
import com.example.secureuserauthentication.payload.request.SignupRequest;
import com.example.secureuserauthentication.payload.response.MessageResponse;
import com.example.secureuserauthentication.repository.CustomerRepository;
import com.example.secureuserauthentication.repository.RoleRepository;
import com.example.secureuserauthentication.repository.UserRepository;
import com.example.secureuserauthentication.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StorageService storage;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    private final Path rootLocation = Paths.get("upload-dir");
    @GetMapping("/all")
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    @GetMapping("/getone/{id}")
    public Customer getOneCustomer(@PathVariable Long id){
        return customerRepository.findById(id).get();
    }
    @PostMapping("/save")
    public Customer saveCustomer(@RequestParam(value = "file")MultipartFile file, Customer customer){
        try {
            String filenamme = Integer.toString(new Random().nextInt(100000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + filenamme + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            customer.setImage(
                    original);

        } catch (Exception e) {
            throw new RuntimeException("FAIL File Problem BackEnd !");
        }
        return customerRepository.save(customer);
    }
    /* @postMapping("/save")
    public Customer saveCustomer(@RequestBody Customer customer){
    return customerRepository.save(customer);
    }
     */
    @PutMapping("/update/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer c = customerRepository.findById(id).get();
        if (c != null) {
            customer.setId(id);
            customer.setImage(c.getImage());
            customer.setPassword(c.getPassword());
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Fail");
        }
    }
        @DeleteMapping("/delete/{id}")
        public HashMap<String, String> deleteCustomer(@PathVariable Long id) {
            HashMap<String, String> message = new HashMap<>();
            try {
                customerRepository.deleteById(id);
                message.put("Etat", "Customer deleted");
            } catch (Exception e) {
                message.put("Etat", "Customer not deleted");
            }
            return message;
        }
    //Display picture
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerCustomer(@RequestParam(value = "file") MultipartFile file, SignupRequest signUpRequest) throws MessagingException {


        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Role custRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(custRole);
        }
        customer.setRoles(roles);

        try {
            String filenamme = Integer.toString(new Random().nextInt(100000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + filenamme + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            customer.setImage(original);

        } catch (Exception e) {
            throw new RuntimeException("FAIL File Problem BackEnd !");
        }
        customerRepository.save(customer);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest request){
        String username = authentication.getName();
        Customer customer =  customerRepository.findByUsername(username);
        if (customer == null){
            throw new IllegalArgumentException("Invalid user");
        }
        if (!encoder.matches(request.getOldPassword(), customer.getPassword())){
            return new ResponseEntity<>("Invalid old password", HttpStatus.EXPECTATION_FAILED);

        }
        customer.setPassword(encoder.encode(request.getNewPassword()));
        return ResponseEntity.ok(customerRepository.save(customer));
    }
    @PutMapping("/updatefile/{id}")
    public Customer updateimage (@PathVariable Long id , @RequestParam(value="file") MultipartFile file) {
        Customer customer = customerRepository.findById(id).get();
        if (customer != null) {
            try {
                String filenamme = Integer.toString(new Random().nextInt(100000000));
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
                String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
                String original = name + filenamme + ext;
                Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
                customer.setImage(original);

            } catch (Exception e) {
                throw new RuntimeException("FAIL File Problem BackEnd !");
            }
        } else {
            throw new RuntimeException("Fail");
        }
        return customerRepository.saveAndFlush(customer);
    }







}






