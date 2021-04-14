package top.kwseeker.developkit.commonutil.excel.write;

import java.io.File;
import java.util.List;

/**
 * 要写到工作簿的数据结构对象
 *  定义了一个完整的工作簿的数据结构（Sheet、Row） TODO sure?
 * easyexcel通过继承的方式将工作簿的数据结构拆分了，这里先合并
 */
public class WriteWorkbook {

    /**
     * 写入到的excel文件
     */
    private File file;

    /**
     * Sheet头部的结构定义
     */
    private List<List<String>> head;

    /**
     * 同样是Sheet头部的结构定义，和head的作用一样，通过反射获取各个字段注解定义
     * 和head二选一
     */
    private Class clazz;


    /* ===================== getter and setter ======================*/

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<List<String>> getHead() {
        return head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
