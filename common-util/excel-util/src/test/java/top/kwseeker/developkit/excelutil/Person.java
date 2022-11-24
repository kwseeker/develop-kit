package top.kwseeker.developkit.excelutil;

import top.kwseeker.developkit.excelutil.validate.ValidateType;

@Tab(name = "人物")
public class Person {

    @Column(title = "姓名", isPrimary = true)
    private String name;
    @Column(title = "年龄")
    private Integer age;
    @Column(title = "详情", validateTypes = ValidateType.JSON)
    private String detail;

    public Person() {
    }

    public Person(String name, Integer age, String detail) {
        this.name = name;
        this.age = age;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", detail='" + detail + '\'' +
                '}';
    }
}
