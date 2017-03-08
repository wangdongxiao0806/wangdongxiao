package com.distributed.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
	
	@Resource
	private JedisPool jedisPool;

	/*************************key-value(String)API********************/
	
	/**
	 * 根据key获取value内容
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		
		Jedis jedis = null;
		try{
			jedis = getJedis();
			String value =jedis.get(key);
			return value;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally {
			this.release(jedis);
		}
	}
	/**
	 * 设置key-value键值对
	 * @param key
	 * @param value
	 */
	public void setValue(String key ,String value){
		Jedis jedis = null;
		try{
			jedis = getJedis();
			jedis.set(key, value);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			this.release(jedis);
		}
	}
	
	/**
	 * 根据key删除键值对
	 * @param key
	 */
	public void delValue(String key){
		if(StringUtils.isEmpty(key)){
			return ;
		}
		Jedis jedis = null;
		try{
			jedis  = this.getJedis();
			jedis.del(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			this.release(jedis);
		}
	}
	
	private void release(Jedis jedis){
		if(jedis != null){
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取jedis客户端对象
	 * @return jedis对象
	 */
	private Jedis getJedis(){
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
	
}
