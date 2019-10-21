package com.fundoo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil<T> 
{
	 private ValueOperations<String,T> valueOperations;
	    
	 @Autowired
	 RedisUtil(RedisTemplate<String,T> redisTemplate)
	 {
	     this.valueOperations = redisTemplate.opsForValue();
	 }
	     
	 public void putValue(String key,T value) 
	 {
	     valueOperations.set(key, value);
	 }
	 
	 public T getValue(String key) 
	 {
	     return valueOperations.get(key);
	 }
	 
}
