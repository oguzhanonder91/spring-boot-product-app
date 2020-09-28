package com.common.configuration;

import com.common.dao.MenuDao;
import com.common.dao.RoleDao;
import com.common.dao.UserDao;
import com.common.entity.Menu;
import com.common.entity.Role;
import com.common.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class InitiationData implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuDao menuDao;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {


        /*ObjectMapper objectMapper = new ObjectMapper();
        try {
            File resource = new ClassPathResource(
                    "data/data.json").getFile();
            List<Menu> menus = Arrays.asList(objectMapper.readValue(resource, Menu[].class));
            for(Menu menu : menus){
                menuDao.save(menu);
                for(Menu child : menu.getChildren()){
                    child.setParent(menu.getId());
                    menuDao.save(child);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        if (roleDao.findByCode("USER") == null) {
            Role roleUser = new Role("User", "USER");
            roleDao.save(roleUser);
        }
        if (roleDao.findByCode("ADMIN") == null) {
            Role roleAdmin = new Role("Admin", "ADMIN");
            roleDao.save(roleAdmin);
        }

        if (!userDao.emailExist("admin@admin.com")) {
            Role role = roleDao.findByCode("ADMIN");
            User user = new User();
            user.setName("Admin");
            user.setSurname("Admin");
            user.setEmail("admin@admin.com");
            user.setRoles(Arrays.asList(role));
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("Admin2020?"));
            userDao.save(user);
        }


    }
}
