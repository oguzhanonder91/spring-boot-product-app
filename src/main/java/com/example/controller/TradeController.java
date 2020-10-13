package com.example.controller;

import com.example.dao.TradeDao;
import com.example.dto.TradeDto;
import com.example.entity.Trade;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("trade")
@MyServiceGroupAnnotation(name = "Cari Özet",path = "trade")
public class TradeController {

    @Autowired
    private TradeDao tradeDao;

    @PostMapping(path = "/createOrUpdate")
    @MyServiceAnnotation(name = "Cari Güncelleme", path = "/createOrUpdate", type = MethodType.POST, permissionRoles = {"ADMIN"})
    public ResponseEntity<Trade> create(@RequestBody final TradeDto tradeDto) {
        final Trade trade = tradeDao.convertFromDto(tradeDto);
        final Trade newTrade  = trade.getId() == null ? tradeDao.create(trade) : tradeDao.update(trade);
        return new ResponseEntity<>(newTrade, HttpStatus.OK);
    }

    @GetMapping(path = "/result")
    @MyServiceAnnotation(name = "Cari Sonuç Gör", path = "/result", type = MethodType.GET, permissionRoles = {"ADMIN","USER"})
    public ResponseEntity<Trade> getResult() {
        final List<Trade> tradeList = tradeDao.findAll();
        return new ResponseEntity<>(tradeList.size() > 0 ? tradeList.get(0) : new Trade(), HttpStatus.OK);
    }
}
