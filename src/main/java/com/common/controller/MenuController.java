package com.common.controller;

import com.common.dao.MenuDao;
import com.common.entity.Menu;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("menu")
@MyServiceGroupAnnotation(name = "Menü Yönetimi",path = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @GetMapping(path = "/all")
    @MyServiceAnnotation(name = "Menü Listeleme", path = "/all", type = MethodType.GET, permissionRoles = {"ADMIN","USER"})
    public ResponseEntity<List<Menu>> getAllMenus() {
        final List<Menu> menuList = menuDao.getAllByParentNull();
        return new ResponseEntity<>(menuList, HttpStatus.OK);
    }

}
