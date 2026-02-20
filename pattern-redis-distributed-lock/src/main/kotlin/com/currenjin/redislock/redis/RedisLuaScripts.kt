package com.currenjin.redislock.redis

object RedisLuaScripts {
    const val COMPARE_AND_DELETE = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1])
        else
            return 0
        end
    """

    const val COMPARE_AND_EXPIRE = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('pexpire', KEYS[1], ARGV[2])
        else
            return 0
        end
    """
}
