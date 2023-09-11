package cn.edu.hitsz.mpccalculator.config;

import cn.edu.hitsz.mpccalculator.CalculatorContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化，读取配置文件到上下文
 *
 * @author hope
 * @date 2023/9/11 - 15:41
 */
@Configuration
public class CalculatorConfig implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "mpc.";
        String id = environment.getProperty(prefix + "id");
        String total = environment.getProperty(prefix + "total");
        String[] calculators = environment.getProperty(prefix + "calculators").split(",");
        String[] providers = environment.getProperty(prefix + "providers").split(",");
        CalculatorContext.id = Integer.valueOf(id);
        CalculatorContext.total = Integer.valueOf(total);
        CalculatorContext.calculators = List.of(calculators);
        CalculatorContext.providers = List.of(providers);
    }
}
