package cn.edu.hitsz.mpccalculator;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author hope
 * @date 2023/9/11 - 14:09
 */
public class CalculatorContext {

    public static final BlockingDeque<BigInteger> inputQueue = new LinkedBlockingDeque<>();
    public static final BlockingDeque<BigInteger> swapQueue = new LinkedBlockingDeque<>();
    public static volatile Integer id;
    public static volatile Integer total;
    public static volatile List<String> calculators;
    public static volatile List<String> providers;
    public static volatile BigInteger alpha;
    public static volatile BigInteger k;

}
