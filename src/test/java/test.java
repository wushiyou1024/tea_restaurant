import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.druid.sql.visitor.functions.Now;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-12-13 21:06
 */
public class test {
    @Test
    public void test(){
        Date date=new Date();
        long time = date.getHours();
        char a=(char) (65+time/2);
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        String s = a + "" + minutes + "" + seconds;
        System.out.println(s);
    }
}
