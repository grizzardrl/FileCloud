package mk0.controller;

import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import mk0.dao.RoleRepo;
import mk0.model.DBFile;
import mk0.model.UploadFileResponse;
import mk0.model.User;
import mk0.service.DBFileStorageService;
import mk0.service.UserServiceImpl;
import mk0.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class MvcConfig implements WebMvcConfigurer {
    
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private RoleRepo roleRepo;    
    
    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private DBFileStorageService dbFileStorageService;
        
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/helloUser").setViewName("helloUser");
        registry.addViewController("/helloAdmin").setViewName("helloAdmin");        
    }
    
    @GetMapping("/home")
    public String homePage(HttpServletRequest request) {
        String destination = "home";
        if (request.isUserInRole("ROLE_ADMIN")) {
            destination = "redirect:/helloAdmin";
        }
        else if (request.isUserInRole("ROLE_USER")) {
            destination = "redirect:/helloUser";
        }
        return destination;
    }
    
    @PostMapping("/home")
    public String userLoggedIn() {
        return "home";
    }
    
    @GetMapping("/newUser")
    public String newUser(Model model) {           
        model.addAttribute("userToAdd", new User());
        return "newUser";
    }
    
    @PostMapping("/newUser")    
    public String addUser(@ModelAttribute("userToAdd") @Valid User userToAdd, BindingResult bindingResult) {        
        userValidator.validate(userToAdd, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "newUser";
        }        
        userService.save(userToAdd);
        
        return "redirect:/helloAdmin";
    }
    
    @GetMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request, Model model){        
        Principal principal = request.getUserPrincipal();
        String pName = principal.getName();        
        User currentUser = userService.findByUsername(pName);        
        List<DBFile> filesOfUser = dbFileStorageService.findByUserId(currentUser);
        model.addAttribute("filesOfUser", filesOfUser);
        return "uploadFile";
    }
    
    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {        
        Principal principal = request.getUserPrincipal();
        String pName = principal.getName();        
        User currentUser = userService.findByUsername(pName);
        
        DBFile dbFile = dbFileStorageService.storeFile(file, currentUser);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dbFile.getId().toString())
                .toUriString();
        UploadFileResponse responseMessage = new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
        
        UserDetails ud = userService.loadUserByUsername(pName);
        String auth = ud.getAuthorities().toString();
        String addon = "USER: " + pName + "\tAUTHORITIES: " + auth;
        return ResponseEntity.ok(responseMessage);        
    }
    
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId) {
        // Load file from database
        DBFile dbFile = dbFileStorageService.getFile(fileId);
        
        Resource resource = dbFileStorageService.loadFileAsResource(dbFile.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")                
                .body(resource);
    }
    
    @DeleteMapping("/uploadFile")
    public ResponseEntity<?> deleteFile(HttpServletRequest request, @RequestParam("fileToDelete") String fileToDelete) {
        
        DBFile fileToRemove = dbFileStorageService.findByFileName(fileToDelete);
        long fileToRemoveId = fileToRemove.getId();
        dbFileStorageService.removeById(fileToRemoveId);
        return ResponseEntity.ok("NAME: " + fileToDelete + "\tID: " + fileToRemoveId + " DELETED");
    }
    
    @GetMapping("/manageAccount")
    public String changePassword(HttpServletRequest request, Model model) {         
        model.addAttribute("userToUpdate", new User());
        
        return "manageAccount";
    }
    
    @PostMapping("/manageAccount")    
    public String changePassword(@ModelAttribute("userToUpdate") @Valid User userToUpdate, HttpServletRequest request, BindingResult bindingResult) {        
        Principal principal = request.getUserPrincipal();
        String pName = principal.getName();
        String testUserName = pName.substring(0, pName.length() - 2);
        userToUpdate.setUsername(testUserName + "_1");       
        
        userValidator.validate(userToUpdate, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "manageAccount";
        }        
        
        User userInDatabase = userService.findByUsername(pName);
        Long lId = userInDatabase.getId();
        userService.updatePassword(lId, userToUpdate.getPassword());        
        
        String destination = "redirect:/helloUser";
        if (request.isUserInRole("ROLE_ADMIN")) {
            destination = "redirect:/helloAdmin";
        }
        return destination;
    }  
    
    @GetMapping("/manageUsers")
    public String manageUsers(HttpServletRequest request, Model model){                      
        List<User> listOfUsers = userService.findAll();
        model.addAttribute("listOfUsers", listOfUsers);
        return "manageUsers";
    }
    
    @DeleteMapping("/manageUsers")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestParam("userToDelete") String userToDelete) {        
        User userToRemove = userService.findByUsername(userToDelete);
        Long userToRemoveId = userToRemove.getId();        
        userService.deleteById(userToRemoveId);        
        return ResponseEntity.ok("NAME: " + userToRemove.getUsername() + "\tID: " + userToRemoveId + " DELETED");
    }
}
