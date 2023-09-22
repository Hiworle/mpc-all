package cn.edu.hitsz.mpcprovider;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author yifu
 * @date 2023/9/12
 * 对比calculatorContext，这里不需要queue，只需要存储一个key向量就好
 */
public class ProviderContext {
    public static volatile Integer id;
    public static volatile Integer total;
    public static volatile List<String> calculators;
    public static volatile List<String> providers;
    public static volatile BigInteger alpha;
    public static volatile BigInteger k;
}
