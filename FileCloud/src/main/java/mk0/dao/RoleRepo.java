/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.dao;

import mk0.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author grizzardfamily
 */
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String sName);
}
