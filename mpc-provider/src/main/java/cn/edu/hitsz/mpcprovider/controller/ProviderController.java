package cn.edu.hitsz.mpcprovider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Arrays;

@Controller
public class ProviderController {

    @PostMapping("/matrix")
    @ResponseBody
    public String submitMatrix(@RequestBody BigInteger[][] matrix) {
        System.out.println(Arrays.deepToString(matrix));
        return "提交成功！";
    }
}
