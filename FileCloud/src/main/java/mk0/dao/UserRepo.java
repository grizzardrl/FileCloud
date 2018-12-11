/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.dao;

import java.util.List;
import mk0.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author grizzardfamily
 */
public interface UserRepo extends CrudRepository<User, Long> {
    
    User findByUsername(String uName);   
    @Override
    List<User> findAll();
    
    @Modifying
    @Transactional
    @Override
    void deleteById(Long id);
    
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    @Transactional
    void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
    
}
