package cn.edu.hitsz.mpccalculator;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpcapi.data.Sender;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Map;

@SpringBootTest
class MpcCalculatorApplicationTests {

    @Test
    void testHttp() throws UnsupportedEncodingException {
        Data data = new Data(100, new Sender(Sender.Type.PROVIDER, "192.168.0.1"), Data.Type.INPUT, BigInteger.ONE);
        // 发送并接收请求
        String result = HttpUtils.httpPostRequest("http://10.249.62.233:8080", JSON.toJSONString(data));
        System.out.println(result);
        // 将收到的字符串转为对象
        Data data1 = JSON.parseObject(result, Data.class);
        System.out.println(data1);
    }

}
