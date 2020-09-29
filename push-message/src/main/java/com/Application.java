package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yx.handler.NettyServer;

//@EnableAutoConfiguration
//@MapperScan("com.yx.mapper*")
//@ComponentScan
//@EnableTransactionManagement//开启事务管理
//@Import(value = { MybatisPlusConfig.class })

@SpringBootApplication
@MapperScan("com.yx.mapper")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
		try {
            System.out.println("http://127.0.0.1:6688/push-message/index");
            new NettyServer(12345).start();
            //new NettyServer(7069).start();
		}catch(Exception e) {
			System.out.println("NettyServerError:"+e.getMessage());
		} 
	}
}