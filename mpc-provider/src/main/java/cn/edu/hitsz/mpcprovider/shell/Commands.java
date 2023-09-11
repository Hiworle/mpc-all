package cn.edu.hitsz.mpcprovider.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @author hope
 * @date 2023/9/11 - 14:15
 */
@ShellComponent
public class Commands {

    @ShellMethod(value = "Echo input.", key = {"echo", "ec"})
    public void echo(String input) {
        System.out.println(input);
    }
}
