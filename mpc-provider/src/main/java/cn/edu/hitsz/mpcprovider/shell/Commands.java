package cn.edu.hitsz.mpcprovider.shell;

import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import cn.edu.hitsz.mpcprovider.upload.Uploads;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;
import java.util.Arrays;

import static cn.edu.hitsz.mpcprovider.ProviderContext.*;

/**
 * @author yifu
 * @date 2023/9/12 - 14:15
 */
@ShellComponent
public class Commands {

    @ShellMethod(value = "Echo input.", key = {"echo", "ec"})
    public void echo(String input) {
        System.out.println(input);
    }

    @ShellMethod(value = "Provide a vector.", key = {"provide"})
    public void provide(@ShellOption(arity = 100) BigInteger[] numbers) {
        // 通过 shell 输入最大为 100 的向量
        // todo 暂时要求输入偶数向量
        System.out.printf("Read input success! num=%d%n", numbers.length);
        if (Uploads.uploads(numbers))
            System.out.println("Upload succeed!");
        else {
            System.out.println("Upload failed!");
        }
    }

    @Deprecated
    @ShellMethod(value = "[Deprecated] Combine the numbers.", key = "comb")
    public void comp(BigInteger si, BigInteger i, BigInteger sj, BigInteger j) {
        if (i.equals(j)) {
            throw new RuntimeException("Error");
        }
        if (i.compareTo(j) < 0) {
            BigInteger t = i;
            i = j;
            j = t;
            t = si;
            si = sj;
            sj = t;
        }
        FieldOperator op = new FieldOperator(k, alpha);
        BigInteger a1b1 = op.div(
                op.sub(si, op.mul(op.exp(alpha, op.sub(i, j)), sj)),
                op.sub(BigInteger.ONE, op.exp(alpha, op.sub(i, j)))
        );
        BigInteger a2b2 = op.div(
                op.sub(si, sj),
                op.sub(op.exp(alpha, op.sub(op.add(BigInteger.ONE, j), BigInteger.ONE)),
                        op.exp(alpha, op.sub(op.add(BigInteger.ONE, i), BigInteger.ONE)))
        );
        System.out.println("\na1b1 = " + a1b1);
        System.out.println("a2b2 = " + a2b2);
    }

    @ShellMethod(value = "Combine the numbers.", key = "combine")
    public void combine(int i, int j) {
        if (i == j) {
            System.out.println("i, j 不应相同！");
            return;
        }
        if (i < j) {
            int t = i;
            i = j;
            j = t;
        }
        String ipI = getIp(i);
        String ipJ = getIp(j);

        String resultI = HttpUtils.httpPostRequest("http://" + ipI + "/result");
        String resultJ = HttpUtils.httpPostRequest("http://" + ipJ + "/result");
        BigInteger[] si = parseResult(resultI);
        BigInteger[] sj = parseResult(resultJ);

        System.out.println("\n===================== S =====================");
        System.out.println("s[" + j + "] = " + Arrays.toString(sj));
        System.out.println("s[" + i + "] = " + Arrays.toString(si));

        FieldOperator op = new FieldOperator(k, alpha);

        BigInteger[] a1b1 = op.div(
                op.sub(si, op.mul(op.exp(alpha, op.sub(BigInteger.valueOf(i), BigInteger.valueOf(j))), sj)),
                op.sub(BigInteger.ONE, op.exp(alpha, op.sub(BigInteger.valueOf(i), BigInteger.valueOf(j))))
        );
        BigInteger[] a2b2 = op.div(
                op.sub(si, sj),
                op.sub(op.exp(alpha, op.sub(op.add(BigInteger.ONE, BigInteger.valueOf(j)), BigInteger.ONE)),
                        op.exp(alpha, op.sub(op.add(BigInteger.ONE, BigInteger.valueOf(i)), BigInteger.ONE)))
        );
        System.out.println("=================== RESULT ===================");
        BigInteger[] ans = new BigInteger[si.length + sj.length];
        for (int k = 0; k < a1b1.length; k++) {
            ans[2 * k] = a1b1[k];
            ans[2 * k + 1] = a2b2[k];
        }
        System.out.println(Arrays.toString(ans));
    }

    /**
     * 根据（最初的）下标获取 Pi 的 ip
     *
     * @param i （最初的）下标，1~n
     * @return Pi 的 ip 地址
     */
    private String getIp(int i) {
        switch (i) {
            case 1 -> {
                return calculators.get(0);
            }
            case 2 -> {
                return calculators.get(total - 1);
            }
            default -> {
                return calculators.get(i - 2);
            }
        }
    }

    /**
     * 将 result 转为 BigInteger 数组
     *
     * @param result 从计算节点获取的 result
     * @return result 对应的数组
     */
    private BigInteger[] parseResult(String result) {
        String[] split = result.substring(1, result.length() - 1).split(",");
        BigInteger[] ans = new BigInteger[split.length];
        for (int i = 0; i < split.length; i++) {
            ans[i] = new BigInteger(split[i].trim());
        }
        return ans;
    }
}
