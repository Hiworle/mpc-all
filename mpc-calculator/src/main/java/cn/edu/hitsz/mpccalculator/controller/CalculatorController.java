package cn.edu.hitsz.mpccalculator.controller;

import cn.edu.hitsz.mpcapi.data.Data;
import cn.edu.hitsz.mpccalculator.CalculatorContext;
import cn.edu.hitsz.mpccalculator.work.Worker;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author hope
 * @date 2023/9/9 - 10:33
 */
@RestController
public class CalculatorController {
    private Thread t;
    private Worker worker;

    @RequestMapping("/")
    public Data cal(@RequestBody Data data) {
        // 存放到相应的队列中，如果是第一次启动，创建计算线程
        if (t == null) {
            worker = new Worker();
            t = new Thread(worker);
            t.start();
        }

        try {
            switch (data.getType()) {
                case INPUT -> {
                    CalculatorContext.inputQueue.put(data.getContent());
                }
                case SWAP -> {
                    CalculatorContext.swapQueue.put(data.getContent());
                }
                case RESULT -> {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("RECEIVED: " + data);
        return data;
    }

    @RequestMapping("/result")
    public String result() {
        if (worker.getResult() == null) {
            return "NULL";
        }
        return Arrays.toString(worker.clearResult());
    }
}
