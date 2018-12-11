/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author grizzardfamily
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)    
    private Long id;
    private String username;
    private String password;
    @Transient
    private String passwordConfirm;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
    
    public User() {        
    }
    
    public User(String sUsername, String sPassword, List<Role> sRoles) {
        this.username = sUsername;
        this.password = sPassword;
        this.roles = sRoles;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    
    public List<Role> getRoles() {
        return roles;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String uName) {
        this.username = uName;
    }
    
    public void setPassword(String pWord) {
        this.password = pWord;
    }
    
    public void setPasswordConfirm(String pwConfirm) {
        this.passwordConfirm = pwConfirm;
    }
    
    public void setRoles(List<Role> sRoles) {
        this.roles = sRoles;
    }
    
    @Override
    public String toString() {
        return "User [ID = " + id + ", userName = " + username + ", password = " + password + "]";
    }
    
    
}
