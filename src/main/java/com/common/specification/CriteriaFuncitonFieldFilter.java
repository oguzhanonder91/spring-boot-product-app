package com.common.specification;

/**
 * @author Oğuzhan ÖNDER
 * @date 23.03.2021 - 12:43
 */

public class CriteriaFuncitonFieldFilter {
    private String field;
    private String root;
    private String alias;
    private CriteriaFunctionType criteriaFunctionType;
    private Object[] whens;

    public CriteriaFuncitonFieldFilter() {
    }

    public CriteriaFuncitonFieldFilter(String field, String root, String alias, CriteriaFunctionType criteriaFunctionType) {
        this.field = field;
        this.root = root;
        this.alias = alias;
        this.criteriaFunctionType = criteriaFunctionType;
    }

    public CriteriaFuncitonFieldFilter(String field, String root, String alias, CriteriaFunctionType criteriaFunctionType, Object[] whens) {
        this.field = field;
        this.root = root;
        this.alias = alias;
        this.criteriaFunctionType = criteriaFunctionType;
        this.whens = whens;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CriteriaFunctionType getCriteriaFunctionType() {
        return criteriaFunctionType;
    }

    public void setCriteriaFunctionType(CriteriaFunctionType criteriaFunctionType) {
        this.criteriaFunctionType = criteriaFunctionType;
    }

    public Object[] getWhens() {
        return whens;
    }

    public void setWhens(Object[] whens) {
        this.whens = whens;
    }
}
