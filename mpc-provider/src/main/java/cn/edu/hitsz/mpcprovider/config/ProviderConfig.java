package cn.edu.hitsz.mpcprovider.config;

import cn.edu.hitsz.mpcprovider.ProviderContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.math.BigInteger;
import java.util.List;

/**
 * 读取配置文件
 *
 * @author Yifu
 * @date 2023/9/12 - 20:29
 * 完全仿照calculator
 */
@Configuration
public class ProviderConfig implements  EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "mpc.";
        String total = environment.getProperty(prefix + "total");
        String id = environment.getProperty(prefix + "id");
        String[] calculators = environment.getProperty(prefix + "calculators").split(",");
        String[] providers = environment.getProperty(prefix + "providers").split(",");
        String alpha = environment.getProperty(prefix + "field.alpha");
        String k = environment.getProperty(prefix + "field.k");

        ProviderContext.total = Integer.valueOf(total);
        ProviderContext.calculators = List.of(calculators);
        ProviderContext.providers = List.of(providers);
        ProviderContext.alpha = new BigInteger(alpha);
        ProviderContext.k = new BigInteger(k);
        ProviderContext.id = Integer.valueOf(id);
    }
}


