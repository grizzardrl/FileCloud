/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.validator;

import mk0.model.User;
import mk0.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author grizzardfamily
 */
@Component
public class UserValidator implements Validator {
    @Autowired
    private UserServiceImpl userService;
    
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }
    
    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            //errors.rejectValue("username", "Size.newUserForm.username");
            errors.rejectValue("username", "Size.newUserForm.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.newUserForm.username");
        }        
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.newUserForm.password");
        }
        if(!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.newUserForm.passwordConfirm");
        }        
    }
}
