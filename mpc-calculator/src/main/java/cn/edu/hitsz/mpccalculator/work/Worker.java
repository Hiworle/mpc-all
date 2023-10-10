package cn.edu.hitsz.mpccalculator.work;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpcapi.data.Sender;
import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import com.alibaba.fastjson.JSON;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.hitsz.mpccalculator.CalculatorContext.*;

/**
 * @author hope
 * @date 2023/9/9 - 10:36
 */
public class Worker implements Runnable {

    private final int counter = id;
    private BigInteger[] d = null;
    private BigInteger[] result = null;
    private final FieldOperator fieldOperator = new FieldOperator(k, alpha);

    @Override
    public void run() {
        while (true) {
            try {
                // 计算[d]_i = [a]_i * [b]_i, i是 counter
                if (!inputQueue.isEmpty()) {
                    BigInteger[] a = inputQueue.take();
                    BigInteger[] b = inputQueue.take();
                    d = fieldOperator.mul(a, b);
                    List<String> ips = new ArrayList<>();
                    if (id == 0) { // 若为P1，发送给P3-Pn
                        ips.addAll(calculators.subList(2, total));
                    } else if (counter == 1) { // 若为P2，发送给P1
                        ips.add(calculators.get(0));
                    } else if (counter == total - 1) { // 若为Pn，发送给P2
                        ips.add(calculators.get(1));
                    }
                    for (String ip : ips) {
                        Sender sender = new Sender(Sender.Type.CALCULATOR, calculators.get(id));
                        Data data = new Data(0, sender, Data.Type.SWAP, d);
                        HttpUtils.httpPostRequest("http://" + ip, JSON.toJSONString(data));
                        System.out.println("SEND: " + data);
                    }
                }
                // 当有别的节点的数据到达，且自己已经算出[d]_i
                if (!swapQueue.isEmpty() && d != null) {
                    BigInteger divisor;
                    BigInteger[] dividend;
                    BigInteger[] swapD = swapQueue.take();

                    if (id == 0) { // P1计算
                        dividend = fieldOperator.sub(swapD, fieldOperator.mul(fieldOperator.alpha(), d));
                        divisor = fieldOperator.sub(BigInteger.ONE, fieldOperator.alpha());
                    } else if (counter == 1) { // P2计算
                        dividend = fieldOperator.sub(swapD, fieldOperator.mul(fieldOperator.exp(fieldOperator.alpha(), BigInteger.valueOf(total - 2)), d));
                        divisor = fieldOperator.sub(BigInteger.ONE, fieldOperator.exp(fieldOperator.alpha(), BigInteger.valueOf(total - 2)));
                    } else { // Pi计算
                        dividend = fieldOperator.sub(d, fieldOperator.mul(fieldOperator.exp(fieldOperator.alpha(), BigInteger.valueOf(counter)), swapD));
                        divisor = fieldOperator.sub(BigInteger.ONE, fieldOperator.exp(fieldOperator.alpha(), BigInteger.valueOf(counter)));
                    }

                    result = fieldOperator.div(dividend, divisor);

//                    if (id > 0) { // P1 不更新
//                        if (counter == 1) { // P2 -> Pn
//                            counter = total - 1;
//                        } else { // Pi -> Pi-1
//                            counter--;
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public BigInteger[] getResult() {
        return result;
    }


    public BigInteger[] clearResult() {
        BigInteger[] ans = result;
        result = null;
        return ans;
    }

}
