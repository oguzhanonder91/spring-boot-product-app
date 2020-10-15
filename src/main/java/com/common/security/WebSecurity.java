package com.common.security;

import com.common.dao.PermissionDao;
import com.common.dao.ServiceDao;
import com.common.dao.UserDao;
import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.entity.Service;
import com.common.entity.User;
import com.util.enums.MethodType;
import com.util.enums.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebSecurity {

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private RequestMappingHandlerMapping mappingHandler;

    public boolean check(Authentication authentication, HttpServletRequest request) {

        try {
            HandlerMethod handlerMethod = (HandlerMethod) mappingHandler.getHandler(request).getHandler();
            String key = handlerMethod.getMethod().toGenericString();
            MethodType methodType = MethodType.valueOf(request.getMethod());
            String username = authentication.getPrincipal() instanceof UserDetails ? ((UserDetails) authentication.getPrincipal()).getUsername() : authentication.getPrincipal().toString();
            Service service = serviceDao.findByKeyAndMethod(key, methodType);
            if (service != null) {
                User user = userDao.findByEmail(username);
                List<Role> roles = new ArrayList<>(user.getRoles());
                List<Permission> permissions = permissionDao.findByItemIdAndTypeAndRolesIn(service.getId(), PermissionType.SERVICE, roles);
                if (permissions != null && !permissions.isEmpty()) {
                    return true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
