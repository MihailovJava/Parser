package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 29.03.13
 * Time: 11:33
 */
public class Exeptions {
    static Exeptions uniqExeptions;

     boolean flag = false;
     String text ;
     Exeptions(){};
    public static final int OUT_OF_RANGE = 0;

    public String getText() {
        return text;
    }

    public boolean isFlag() {
        return flag;
    }

    public  void clear(){
        flag = false;
    }

    public static Exeptions getInstance(){
        if (uniqExeptions == null)
            uniqExeptions = new Exeptions();
        return   uniqExeptions;
    }


    public  void e(int code){
        switch (code){
            case OUT_OF_RANGE:
                flag = true;
                text = "OUT OF RANGE";
            break;
        }
    }
}
