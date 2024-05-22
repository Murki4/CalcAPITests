package PojoClasses;

import java.util.Random;

public class PostRequestBody {
    private String number_1;
    private String number_2;
    private String operator;

    private boolean doubleswitch;

    public PostRequestBody(String operator){
        Random rand = new Random();
        number_1 = Integer.toString(rand.nextInt(100));
        number_2 = Integer.toString(rand.nextInt(100));
        this.operator = operator;
    }

    public PostRequestBody(String number_1, String number_2, String operator){
        this.number_1 = number_1;
        this.number_2 = number_2;
        this.operator = operator;
    }

    public PostRequestBody(String operator, boolean doubleswitch){
        Random rand = new Random();
        number_1 = Double.toString(rand.nextDouble()*10);
        number_2 = Double.toString(rand.nextDouble()*10);
        this.operator = operator;
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
}
