package top.kwseeker.developkit.excelutil;

import java.util.List;

public class ExcelEntityRelation {

    private List<FieldColumnRelation> relations;
    private Integer primaryColumnIndex;

    public ExcelEntityRelation() {
    }

    public ExcelEntityRelation(List<FieldColumnRelation> relations, Integer primaryColumnIndex) {
        this.relations = relations;
        this.primaryColumnIndex = primaryColumnIndex;
    }

    public List<FieldColumnRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<FieldColumnRelation> relations) {
        this.relations = relations;
    }

    public Integer getPrimaryColumnIndex() {
        return primaryColumnIndex;
    }

    public void setPrimaryColumnIndex(Integer primaryColumnIndex) {
        this.primaryColumnIndex = primaryColumnIndex;
    }
}
