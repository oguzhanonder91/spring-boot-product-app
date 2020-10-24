package com.common.job;

import com.common.dao.TokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Component
@Transactional
public class TokenJob {

    @Autowired
    private TokenDao tokenDao;

    @Scheduled(cron = "#{@caboryaConfig.service.expiredTokenDeleteCronExpression}")
    public void purgeExpired() {
        Date now = Date.from(Instant.now());
        tokenDao.deleteTokensByIssuedDateBeforeNow(now.getTime());
    }
}
