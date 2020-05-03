package com.example.dao;

import com.example.dto.TradeDto;
import com.example.entity.Trade;
import com.example.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeDao {

    @Autowired
    private TradeService tradeService;

    public Trade convertFromDto(TradeDto tradeDto){
        Trade trade = getTrade();
        trade.setCount(tradeDto.getCount());
        if(tradeDto.getPurchase() != 0 ){
            trade.setPurchase(tradeDto.getPurchase() * tradeDto.getCount());
        }
        if(tradeDto.getSale() != 0 ){
            trade.setSale(tradeDto.getSale() * tradeDto.getCount());
        }
        trade.setBalance(trade.getSale() - trade.getPurchase());
        return trade;
    }

    public Trade getTrade(){
        List<Trade> tradeList = tradeService.findAll();
        return tradeList.size() > 0 ? tradeList.get(0) : new Trade();
    }
}
