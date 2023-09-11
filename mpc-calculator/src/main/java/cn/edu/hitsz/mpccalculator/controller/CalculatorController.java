package cn.edu.hitsz.mpccalculator.controller;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpccalculator.CalculatorContext;
import cn.edu.hitsz.mpccalculator.work.Worker;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hope
 * @date 2023/9/9 - 10:33
 */
@RestController
public class CalculatorController {
    private Thread t;
    @RequestMapping("/")
    public Data cal(@RequestBody Data data) {
        // todo 存放到相应的队列中，如果是第一次启动，创建计算线程
        CalculatorContext.queue.add(data.getContent());
        if (t == null) {
            Worker worker = new Worker();
            t = new Thread(worker);
            t.start();
        }
        System.out.println(data);
        return data;
    }
}
