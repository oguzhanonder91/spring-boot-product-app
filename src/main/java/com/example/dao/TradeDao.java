package com.example.dao;

import com.example.dto.TradeDto;
import com.example.entity.Trade;
import com.example.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class TradeDao {

    @Autowired
    private TradeService tradeService;

    public Trade convertFromDto(TradeDto tradeDto){
        Trade trade = getTrade();
        if(!StringUtils.isEmpty(trade.getId())){
            if(tradeDto.getPurchase() != 0 ){
                trade.setPurchase(trade.getPurchase() + tradeDto.getPurchase() * tradeDto.getCount());
                trade.setCount(trade.getCount() + tradeDto.getCount());
            }
            if(tradeDto.getSale() != 0 ){
                trade.setSale(trade.getSale() + tradeDto.getSale() * tradeDto.getCount());
                trade.setCount(trade.getCount() - tradeDto.getCount());
            }
        }else{
            trade.setCount(tradeDto.getCount());
            if(tradeDto.getPurchase() != 0 ){
                trade.setPurchase(tradeDto.getPurchase() * tradeDto.getCount());
            }
            if(tradeDto.getSale() != 0 ){
                trade.setSale(tradeDto.getSale() * tradeDto.getCount());
            }
        }

        trade.setBalance(trade.getSale() - trade.getPurchase());
        return trade;
    }

    private Trade getTrade(){
        List<Trade> tradeList = tradeService.findAllForEntity();
        return tradeList.size() > 0 ? tradeList.get(0) : new Trade();
    }

    public List<Trade> findAll(){
        return tradeService.findAllForEntity();
    }

    public Trade update(Trade trade){
        return tradeService.updateForEntity(trade);
    }

    public Trade create(Trade trade){
        return tradeService.saveForEntity(trade);
    }
}
