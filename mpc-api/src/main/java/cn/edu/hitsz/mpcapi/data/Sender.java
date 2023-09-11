package cn.edu.hitsz.mpcapi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hope
 * @date 2023/9/9 - 10:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sender {
    public enum Type {
        PROVIDER, CALCULATOR
    }

    private Type type;
    private String ip;
}
