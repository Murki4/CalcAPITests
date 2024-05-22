package PojoClasses;

public class ResultData {
    private Integer pk;
    private String number_1;
    private String number_2;
    private String operator;
    private String result;

    public ResultData(Integer pk, String number_1, String number_2, String operator, String  result) {
        this.pk = pk;
        this.number_1 = number_1;
        this.number_2 = number_2;
        this.operator = operator;
        this.result = result;
    }
    public ResultData(){

    }

    public Integer getPk() {
        return pk;
    }

    public String getNumber_1() {
        return number_1;
    }

    public String getNumber_2() {
        return number_2;
    }

    public String getOperator() {
        return operator;
    }

    public String getResult() {
        return result;
    }
}
