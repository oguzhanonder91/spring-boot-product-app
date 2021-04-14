package com.example.service.impl;

import com.common.service.impl.BaseServiceImpl;
import com.example.dto.TradeDto;
import com.example.entity.Trade;
import com.example.service.TradeService;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl extends BaseServiceImpl<Trade, TradeDto> implements TradeService {
}
