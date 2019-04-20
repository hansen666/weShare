package cn.compusshare.weshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableAsync
@ServletComponentScan
@EnableTransactionManagement
@EnableWebSocket
public class WeshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshareApplication.class, args);
	}



}
