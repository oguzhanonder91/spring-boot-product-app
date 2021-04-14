package com.common.service.impl;

import com.common.dto.MenuDto;
import com.common.entity.Menu;
import com.common.repository.MenuRepository;
import com.common.service.MenuService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, MenuDto> implements MenuService {
}
