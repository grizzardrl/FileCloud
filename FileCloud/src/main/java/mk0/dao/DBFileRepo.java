/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.dao;

import java.util.List;
import mk0.model.DBFile;
import mk0.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author grizzardfamily
 */
@Repository
public interface DBFileRepo extends JpaRepository<DBFile, Long> {   
    
    List<DBFile> findByOwner(User uOwner);
    
    @Modifying
    @Transactional
    void removeById(long lId);    
    
    DBFile findByFileName(String sFileName);
}
