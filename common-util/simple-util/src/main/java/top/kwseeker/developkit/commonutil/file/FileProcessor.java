package top.kwseeker.developkit.commonutil.file;

import org.apache.commons.lang3.StringUtils;
import top.kwseeker.developkit.commonutil.batch.BatchProcessor;

import java.io.*;

/**
 * 读取文件
 * 按行解析/转换
 * 写入文件
 */
public class FileProcessor {

    /**
     * 文件内容转换，按行转换
     * @param sourceFilePath    源文件
     * @param targetFilePath    目标文件
     * @param lineProcessor     行转规则
     * @throws IOException      文件不存在、目录或文件创建失败、流操作失败
     */
    public static void convert(String sourceFilePath, String targetFilePath, LineProcessor lineProcessor) throws IOException {
        checkFileExist(targetFilePath);
        //!!! try-with-resources 只能给实现了 java.lang.AutoCloseable 接口的类使用
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFilePath));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFilePath))) {

            BatchProcessor<String> batchWriteProcessor = new BatchProcessor<>(500, lineList -> {
                for (String lineToWrite : lineList) {
                    bufferedWriter.write(lineToWrite);
                }
                bufferedWriter.flush();
            });

            String line;
            for (line = bufferedReader.readLine(); StringUtils.isNotEmpty(line); line = bufferedReader.readLine()) {
                String convertedLine = lineProcessor.convert(line);
                batchWriteProcessor.add(convertedLine);
            }
            batchWriteProcessor.doFinal();
        }
    }

    /**
     * 文件内容拆分
     */
    public static void split(String sourceFilePath, String targetFilePath, Splitter splitter) {
    }

    public static void checkFileExist(String targetFilePath) throws IOException {
        int lastDirIdx = targetFilePath.lastIndexOf("/");
        File dir = new File(targetFilePath.substring(0, lastDirIdx+1));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("创建目录失败：dir=" + dir);
            }
        }
        File targetFile = new File(targetFilePath);
        if (!targetFile.exists() && !targetFile.createNewFile()) {
            throw new IOException("创建文件失败：file=" + targetFilePath);
        }
    }

    public interface LineProcessor {
        String convert(String line);
    }

    public interface Splitter {
    }
}
