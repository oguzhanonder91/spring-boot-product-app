package com.common.configuration;

import com.common.dao.*;
import com.common.entity.*;
import com.common.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.SecurityUtil;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import com.util.enums.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private ServiceGroupDao serviceGroupDao;

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Value("${scanPackage}")
    private String SCAN_PACKAGE;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
        roleSave("USER", "User");
        roleSave("ADMIN", "Admin");
        userSave("admin@admin.com");
        menuSave();
        serviceAndServiceGroupSave();
    }

    private void roleSave(String code, String name) {
        if (roleDao.findByCode(code) == null) {
            Role role = new Role(name, code);
            roleDao.save(role);
        }
    }

    private void userSave(String email) {
        if (!userDao.emailExist(email)) {
            Role role = roleDao.findByCode("ADMIN");
            User user = new User();
            user.setName("Admin");
            user.setSurname("Admin");
            user.setEmail(email);
            user.setRoles(Arrays.asList(role));
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("Admin2020?"));
            userDao.save(user);
        }
    }

    private void menuSave() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File resource = new ClassPathResource(
                    "data/menu.json").getFile();
            List<Menu> menus = Arrays.asList(objectMapper.readValue(resource, Menu[].class));
            for (Menu menu : menus) {
                Menu returnVal = controlMenu(menu);
                permissionSaveMenu(returnVal);
                if (menu.getChildren() != null) {
                    for (Menu child : menu.getChildren()) {
                        child.setParent(menu.getId());
                        Menu val = controlMenu(child);
                        permissionSaveMenu(val);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Menu controlMenu(Menu menu) {
        Menu returnMenu = menuDao.findByCode(menu.getCode());
        if (returnMenu == null) {
            returnMenu = menuDao.save(menu);
        }
        return returnMenu;
    }

    private void permissionSaveMenu(Menu menu) {
        if (menu.getRoleCodes() != null && !menu.getRoleCodes().isEmpty()) {
            String[] itemsRoles = new String[menu.getRoleCodes().size()];
            itemsRoles = menu.getRoleCodes().toArray(itemsRoles);
            for (String code : itemsRoles) {
                List<Role> roleList = new ArrayList<>();
                Permission permission = permissionDao.findByItemIdAndTypeAndRoleCode(menu.getId(), PermissionType.MENU, code);
                if (permission == null) {
                    Role role = roleDao.findByCode(code);
                    if (role != null) {
                        roleList.add(role);
                    } else {
                        throw new BaseException("Role Bulunamadı");
                    }
                    Permission permissionSave = new Permission(PermissionType.MENU, menu.getId());
                    permissionSave.setRoles(roleList);
                    permissionDao.save(permissionSave);
                }
            }
        }
    }

    private List<Role> roleControl(String[] roles) {
        List<Role> roleList = new ArrayList<>();
        if (roles != null && roles.length > 0) {
            for (String code : roles) {
                Role role = roleDao.findByCode(code);
                if (role != null) {
                    roleList.add(role);
                }
            }
        }
        return roleList;
    }

    private void permissionSaveService(String itemId, PermissionType permissionType, String[] roles) {
        if (permissionDao.findByTypeAndItemId(permissionType, itemId) == null) {
            Permission permission = new Permission(permissionType, itemId);
            List<Role> roleList = roleControl(roles);
            permission.setRoles(roleList);
            permissionDao.save(permission);
        }

    }

    private void serviceAndServiceGroupSave() {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(MyServiceGroupAnnotation.class));

        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(SCAN_PACKAGE);
        List<String> willDeleteServiceGroups = new ArrayList<>();
        List<String> willDeleteServices = new ArrayList<>();

        try {
            for (BeanDefinition bd : beanDefinitionSet) {
                Class clazz = Class.forName(bd.getBeanClassName());
                MyServiceGroupAnnotation annotation = (MyServiceGroupAnnotation) clazz.getAnnotation(MyServiceGroupAnnotation.class);
                String className = securityUtil.encode(clazz.getName());
                willDeleteServiceGroups.add(className);
                ServiceGroup serviceGroup = controlServiceGroup(className, annotation.path(), annotation.name());
                Method[] methods = ReflectionUtils.getDeclaredMethods(clazz);
                for (Method method : methods) {
                    MyServiceAnnotation myServiceAnnotation = method.getAnnotation(MyServiceAnnotation.class);
                    if (myServiceAnnotation != null) {
                        String methodName = securityUtil.encode(method.toGenericString());
                        willDeleteServices.add(methodName);
                        if (serviceGroup != null) {
                            Service service = controlService(methodName, myServiceAnnotation.path(), myServiceAnnotation.type(), myServiceAnnotation.name(), serviceGroup);
                            if (service != null) {
                                permissionSaveService(service.getId(), PermissionType.SERVICE, myServiceAnnotation.permissionRoles());
                            }
                        }
                    }

                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        deleteServiceAndPermissions(willDeleteServices);
        deleteServiceGroup(willDeleteServiceGroups);
    }

    private void deleteServiceGroup(List<String> keys) {
        List<ServiceGroup> serviceGroups = serviceGroupDao.findByKeyNotIn(keys);
        serviceGroupDao.realDeleteAll(serviceGroups);
    }

    private void deleteServiceAndPermissions(List<String> keys) {
        List<Service> services = serviceDao.findByKeyNotIn(keys);
        List<String> ids = services.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
        List<Permission> willDeletePermissions = permissionDao.findByItemIdIn(ids);
        permissionDao.realDeleteAll(willDeletePermissions);
        serviceDao.realDeleteAll(services);
    }

    private ServiceGroup controlServiceGroup(String key, String path, String name) {
        if (serviceGroupDao.findByKeyAndPath(key, path) == null) {
            ServiceGroup serviceGroup = new ServiceGroup(path, name, key);
            return serviceGroupDao.save(serviceGroup);
        }
        return null;
    }

    private Service controlService(String key, String path, MethodType methodType, String name, ServiceGroup serviceGroup) {
        if (serviceDao.findByKeyAndMethod(key, methodType) == null) {
            Service service = new Service(serviceGroup.getPath() + path, key, methodType, name, serviceGroup);
            return serviceDao.save(service);
        }
        return null;
    }

}


