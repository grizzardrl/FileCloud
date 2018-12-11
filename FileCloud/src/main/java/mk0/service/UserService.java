/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.service;

import mk0.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author grizzardfamily
 */
public interface UserService extends UserDetailsService {
    User findByUsername(String sUsername);
    
    User save(User user);
}
