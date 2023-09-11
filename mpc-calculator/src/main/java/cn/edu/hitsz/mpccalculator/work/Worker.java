package cn.edu.hitsz.mpccalculator.work;

import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;

import java.math.BigInteger;

import static cn.edu.hitsz.mpccalculator.CalculatorContext.*;

/**
 * @author hope
 * @date 2023/9/9 - 10:36
 */
public class Worker implements Runnable {

    private int counter;
    private FieldOperator fieldOperator;

    @Override
    public void run() {
        while (true) {
            // todo 取数据计算并按照规则发送
            BigInteger a = queue.poll();
            BigInteger b = queue.poll();
            BigInteger c = fieldOperator.mul(a, b);
            queue.addFirst(c);
        }
    }
}
