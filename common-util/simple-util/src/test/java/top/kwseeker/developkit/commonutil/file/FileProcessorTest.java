package top.kwseeker.developkit.commonutil.file;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileProcessorTest {

    /**
     * 测试文件内容转换
     */
    @Test
    public void testConvert() throws IOException {
        String sourceFilePath = "/home/lee/mywork/java/develop-kit/testfile/source.txt";
        String targetFilePath = "/home/lee/mywork/java/develop-kit/testfile/target.sql";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileProcessor.convert(sourceFilePath, targetFilePath, line -> {
            //{"user_id":703687560438753,"lover_id":703687560441690,"prop_id":122,"time":1623164350882}
            //转换为
            //update proposal_record set result=1, modify_time='2021-06-08 22:59:10'
            //where id = (select tid from (select max(id) as tid from proposal_record where proposer_id=703687441851333 and lover_id=703687441851334 and ring=137) as tmp);
            JSONObject jsonObject = JSONObject.parseObject(line);
            String dateTime = sdf.format(new Date((long) jsonObject.get("time")));
            return String.format("update proposal_record set result=1, modify_time='%s' " +
                            "where id = (select tid from (select max(id) as tid from proposal_record where proposer_id=%d and lover_id=%d and ring=%d) as tmp);\n",
                    dateTime, jsonObject.getLongValue("user_id"), jsonObject.getLongValue("lover_id"), jsonObject.getIntValue("ring"));
        });
    }
}