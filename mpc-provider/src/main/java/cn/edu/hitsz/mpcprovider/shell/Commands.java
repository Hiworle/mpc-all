package cn.edu.hitsz.mpcprovider.shell;

import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcprovider.ProviderContext;
import cn.edu.hitsz.mpcprovider.upload.Uploads;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;

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

    @ShellMethod(value = "Add array.", key = {"add"})
    public void add(@ShellOption(arity = 100) BigInteger[] numbers) { //通过shell输入最大为100的向量
        System.out.println(String.format("Read input success! num=%d", numbers.length));
        if (Uploads.uploads(numbers))
            System.out.println(String.format("Upload succeed!"));
        else {
            System.out.println(String.format("Upload failed!"));
        }
    }

    @ShellMethod(value = "Combine the numbers.", key = "comb")
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
}
