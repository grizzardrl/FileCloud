/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author grizzardfamily
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;  
    
    public Role() {        
    }
    
    public Role(String sName) {
        this.name = sName;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }   
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String sName) {
        this.name = sName;
    }   
}
