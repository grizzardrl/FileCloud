/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  grizzardfamily
 * Created: Nov 17, 2018
 */


insert into role (id, name)
values (1, 'ROLE_ADMIN');
 
insert into role (id, name)
values (2, 'ROLE_USER');

/* ADD AMDIN USER WITH ROLE_ADMIN AND PASSWORD 'password' */
insert into users (id, username, password)
SELECT 0, 'administrator', '$2a$10$RKKj7u0GMUQ/QEEpKdxJg.B39YL8v2ZN8bs0h3UsuTWtKgFu4NBoK'
WHERE NOT EXISTS (SELECT * FROM users WHERE username='administrator');

insert into users_roles (user_id, role_id)
SELECT 0, 1
WHERE NOT EXISTS (SELECT * FROM users_roles WHERE user_id=0);