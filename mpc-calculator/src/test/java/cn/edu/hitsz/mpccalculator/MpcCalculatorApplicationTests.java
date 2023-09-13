package cn.edu.hitsz.mpccalculator;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpcapi.data.Sender;
import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.math.MatrixOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

@SpringBootTest
class MpcCalculatorApplicationTests {

    @Test
    void testHttp() {
        Data data = new Data(100, new Sender(Sender.Type.PROVIDER, "192.168.0.1"), Data.Type.INPUT, BigInteger.ONE);
        // 发送并接收请求
        String result = HttpUtils.httpPostRequest("http://127.0.0.1:8080", JSON.toJSONString(data));
        System.out.println(result);
        // 将收到的字符串转为对象
        Data data1 = JSON.parseObject(result, Data.class);
        System.out.println(data1);
    }

    @Test
    void testOperator() {
        BigInteger k = BigInteger.valueOf(11);
        BigInteger alpha = BigInteger.valueOf(2);
        FieldOperator fieldOperator = new FieldOperator(k, alpha);

        BigInteger add = fieldOperator.add(BigInteger.valueOf(7), BigInteger.valueOf(8));
        System.out.println("add = " + add);

        BigInteger mul = fieldOperator.mul(BigInteger.valueOf(5), BigInteger.valueOf(6));
        System.out.println("mul = " + mul);

        BigInteger sub = fieldOperator.sub(BigInteger.valueOf(2), BigInteger.valueOf(10));
        System.out.println("sub = " + sub);

        BigInteger div = fieldOperator.div(BigInteger.valueOf(2), BigInteger.valueOf(3));
        System.out.println("div = " + div);

        MatrixOperator matrixOperator = new MatrixOperator();
        BigInteger[][] a = new BigInteger[3][5];
        BigInteger[][] b = new BigInteger[5][3];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = BigInteger.valueOf(i + j);
            }
        }
        for (BigInteger[] bigIntegers : b) {
            Arrays.fill(bigIntegers, BigInteger.ONE);
        }
        BigInteger[][] matrixMul = matrixOperator.mul(a, b, fieldOperator);
        System.out.println("a = " + Arrays.deepToString(a));
        System.out.println("b = " + Arrays.deepToString(b));
        System.out.println("matrixMul = " + Arrays.deepToString(matrixMul));
    }
}
