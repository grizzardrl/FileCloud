/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mk0.dao.DBFileRepo;
import mk0.dao.RoleRepo;
import mk0.dao.UserRepo;
import mk0.model.DBFile;
import mk0.model.Role;
import mk0.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author grizzardfamily
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private DBFileRepo fileRepo;
    
    @Autowired
    private DBFileStorageService fileService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));        
        Role uRole = roleRepo.findByName("ROLE_USER");        
        user.setRoles(Arrays.asList(uRole));
        return userRepo.save(user);
    }
    
    public User findByUsername(String uName) {
        return userRepo.findByUsername(uName);
    }
    
    public List<User> findAll() {
        return userRepo.findAll();
    }
    
    public void deleteById(Long id) {        
        Optional<User> optUserToDelete = userRepo.findById(id);
        
        //This is a kludge caused by userRepo.findByID(Long id) returning an optional...
        //TODO: de-kludge by unpacking the optional in a more robust way
        User currentUser;
        if (optUserToDelete.isPresent()) {            
            currentUser = optUserToDelete.get();
        }
        else {
            return;
        }
        
        //delete all files of user
        List<DBFile> filesOfUser = fileService.findByUserId(currentUser);        
        for (DBFile f : filesOfUser) {
            fileService.removeById(f.getId());
        }   
        
        //delete user from users_roles
        currentUser.setRoles(new ArrayList<>());
        
        //delete user
        userRepo.deleteById(id);        
    }

    @Override
    public UserDetails loadUserByUsername(String uName) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(uName);        
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {            
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
    
    public void updatePassword(Long userId, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        userRepo.updatePassword(userId, encodedPassword);
    }
}
