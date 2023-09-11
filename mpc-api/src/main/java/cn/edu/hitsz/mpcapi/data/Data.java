package cn.edu.hitsz.mpcapi.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author hope
 * @date 2023/9/9 - 10:19
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data {

    public enum Type {
        INPUT,
        SWAP,
        RESULT
    }
    private Integer seqNum;
    private Sender sender;
    private Type type;
    private BigInteger content;
}
