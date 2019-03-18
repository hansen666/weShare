package cn.compusshare.weshare;

import cn.compusshare.weshare.repository.responsebody.GoodsInfo;
import cn.compusshare.weshare.service.GoodsService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeshareApplicationTests {

	@Autowired
	private GoodsService goodsService;
	@Test
	public void contextLoads() {
	}

}
