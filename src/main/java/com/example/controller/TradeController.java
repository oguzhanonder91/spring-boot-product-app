package com.example.controller;

import com.example.dao.TradeDao;
import com.example.dto.TradeDto;
import com.example.entity.Product;
import com.example.entity.Trade;
import com.example.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("trade")
public class TradeController {

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private TradeService tradeService;

    @PostMapping(path = "/create")
    public ResponseEntity<Trade> create(@RequestBody final TradeDto tradeDto) {
        final Trade trade = tradeDao.convertFromDto(tradeDto);
        final Trade newTrade  = trade.getId() == null ? tradeService.save(trade) : tradeService.update(trade);
        return new ResponseEntity<>(newTrade, HttpStatus.OK);
    }

    @GetMapping(path = "/result")
    public ResponseEntity<Trade> getResult() {
        final List<Trade> tradeList = tradeService.findAll();
        return new ResponseEntity<>(tradeList.size() > 0 ? tradeList.get(0) : new Trade(), HttpStatus.OK);
    }
}
