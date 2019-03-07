package cn.compusshare.weshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WeshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshareApplication.class, args);
	}



}
