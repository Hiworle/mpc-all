package cn.edu.hitsz.mpcprovider.upload;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpcapi.data.Sender;
import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.math.MatrixOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import cn.edu.hitsz.mpcprovider.ProviderContext;
import com.alibaba.fastjson.JSON;

import java.math.BigInteger;

/**
 * @author yifu
 * @date 2023/9/12 - 14:15
 */
public class Uploads {

    private static BigInteger[][] G; //G 目前通过 yml中提供的α进行计算
    private static final FieldOperator Fop = new FieldOperator(ProviderContext.k, ProviderContext.alpha);
    private static final MatrixOperator Mop = new MatrixOperator();

    public static boolean uploads(BigInteger[] numbers) {

        BigInteger[][] ans = CalcAns(numbers);
        try {
            for (int i = 0; i < ProviderContext.total; i++) {
                BigInteger num = ans[i][0];
                String ip = ProviderContext.providers.get(ProviderContext.id); //这里用的是id，0号id就是第一个（A），1号就是第二个（B)
                Data data = new Data(100, new Sender(Sender.Type.PROVIDER, ip), Data.Type.INPUT, num);

                String result = HttpUtils.httpPostRequest("http://" + ProviderContext.calculators.get(i), JSON.toJSONString(data));
                System.out.println(result);

            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //初始分离，计算需要传给每个calculator的数据
    private static BigInteger[][] CalcAns(BigInteger[] numbers) {
        BigInteger[][] a = new BigInteger[2][1];
        a[0][0] = numbers[0];
        a[1][0] = numbers[1];

        CalcG();

        return Mop.mul(G, a, Fop);
    }

    private static void CalcG() {
        int n = ProviderContext.total;
        G = new BigInteger[n][2];
        BigInteger t = BigInteger.valueOf(-1);
        for (int i = 0; i < n; i++) {
            G[i][0] = BigInteger.valueOf(1);
            G[i][1] = t;
            t = Fop.mul(t, ProviderContext.alpha);
        }
    }
}
