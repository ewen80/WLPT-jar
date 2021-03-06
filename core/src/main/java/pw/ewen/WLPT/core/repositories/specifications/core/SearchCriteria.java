package pw.ewen.WLPT.core.repositories.specifications.core;

import pw.ewen.WLPT.utils.LocalDateStringConverter;

/**
 * Created by wenliang on 17-3-28.
 * 查询标准格式
 */
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        //如果value的值是"true"或者"false"，则将value转为Boolean型（必须是小写）
        if(value.getClass().equals(String.class) && (value.equals("true") || value.equals("false"))){
            value = Boolean.valueOf(value.toString());
        } else if (value.getClass().equals(String.class) && (value.toString().endsWith("/localdate"))) {
            //如果是 "/localdate" 结尾则认为字符串为 localdate类型，进行转换
            int postfixIndex = value.toString().lastIndexOf("/localdate");
            String dateString = value.toString().substring(0, postfixIndex);
            value = LocalDateStringConverter.toLocalDate(dateString);
        }
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public SearchOperation getOperation() {
        return operation;
    }
    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
}
