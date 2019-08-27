package com.nxlh.manager.serviceimpl;

import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.service.BaseRedisCURDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class BaseRedisCURDServiceImpl implements BaseRedisCURDService {


    @Autowired
    @Lazy
    protected RedisCommand redisService;
}
