package com.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

public class RedisTest {
	/**redis服务ip*/
	public static final String hostName = "39.96.116.222";
	/**redis服务端口*/
	public static final int port = 6379;
	/**队列名称*/
	public static final String template_Queue = "TEMPLATE_QUEUE";
	/**测试数据*/
	public static final String data = getData();
	
	public static void main(String [] args) throws InterruptedException{
		
		/**1.配置连接池参数*/
		JedisPoolConfig  poolConfig =getPoolConfig();
		/**2.获取连接工厂*/
		JedisConnectionFactory connectionFactory = getConnectionFactory(poolConfig);
		/**3.获取RedisTemplate实例*/
		RedisTemplate redisTemplate = getRedisTemplate(connectionFactory);
		for(int i =0; i<20; i++){
		System.out.println("测试开始......");
		System.out.println("队列写入数据:");
		System.out.println(data);
		/**获取RedisTemplate操作方式*/
		ListOperations operation = redisTemplate.opsForList();
		 
		operation.leftPush(template_Queue, data);
		}
		
		
		System.out.println("主线程休眠10秒......");
		Thread.sleep(10000);
		ListOperations operation1 = redisTemplate.opsForList();
		while(redisTemplate.hasKey(template_Queue)){
		Object strJson = operation1.rightPop(template_Queue,10, TimeUnit.SECONDS);
		System.out.println("队列读出数据:");
		System.out.println(strJson);
		System.out.println("测试结束......");
		}
	}
 
	/**获取JSON数据字符串*/
	public static String getData(){
		
		 String data=  "{\n" +
	            "    \"AREA_CODE\": \"0592\",\n" +
	            "    \"AREA_NUMBER\": \"350200\",\n" +
	            "    \"CITY_NAME\": \"厦门\",\n" +
	            "    \"DESCRIBE\": \"适合居住\",\n" +
	            "    \"POSTAL_CODE\": \"361000\"\n" +
	            "}";
		 return data;
	}
	
	
	/**1.配置连接池参数*/
	public static JedisPoolConfig getPoolConfig(){
		
		JedisPoolConfig jedisPoolConfig = new redis.clients.jedis.JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxIdle(100);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(50000);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(20000);
		jedisPoolConfig.setNumTestsPerEvictionRun(-1);
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10000);
		jedisPoolConfig.setMaxWaitMillis(1000);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestWhileIdle(true);
		jedisPoolConfig.setTestOnReturn(false);
		jedisPoolConfig.setJmxEnabled(true);
		jedisPoolConfig.setJmxNamePrefix("pool");
		jedisPoolConfig.setBlockWhenExhausted(false);
		return jedisPoolConfig;
	}
	/**2.获取连接工厂*/
	public static JedisConnectionFactory getConnectionFactory(JedisPoolConfig poolConfig){
		
		JedisConnectionFactory jedisConnetFactory = new JedisConnectionFactory();
		jedisConnetFactory.setPoolConfig(poolConfig);
		jedisConnetFactory.setHostName(hostName);
		jedisConnetFactory.setPort(port);
		
		/**必须执行这个函数,初始化JedisConnectionFactory*/
		jedisConnetFactory.afterPropertiesSet();
		 
		return jedisConnetFactory;
	}
	/**3.获取RedisTemplate实例*/
	public static RedisTemplate getRedisTemplate(JedisConnectionFactory connectionFactory){
		
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		StringRedisSerializer serializer = new StringRedisSerializer();
		redisTemplate.setDefaultSerializer(serializer);
		redisTemplate.setKeySerializer(serializer);
		redisTemplate.setValueSerializer(serializer);
		
		/**必须执行这个函数,初始化RedisTemplate*/
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
