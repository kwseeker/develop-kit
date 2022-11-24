package top.kwseeker.developkit.excelutil.validate;

import com.alibaba.fastjson.JSONObject;

public class JSONValidator implements Validator {

    @Override
    public boolean matchType(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean valid(Object columnContent) {
        try {
            if (!matchType(columnContent.getClass())) {
                return false;
            }
            JSONObject.parseObject((String) columnContent);  //会不会被JIT给优化了
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }
}
