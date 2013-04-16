package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 25.03.13
 * Time: 18:55
 */
public class BinaryArithmetic {
   public static BinaryNumber sum(BinaryNumber... args){

        int rank = args[0].getCapacity();   // разрядность системы
        int additionalBit = 0;
        boolean  flag = false;
        int[] resultDenom  = new int[args[0].getCapacity()/2];
        int[] resultNumer  = new int[args[0].getCapacity()/2];
        for (int i = 0 ; i < args.length ; i ++) {    // перевод отрицательных в дополнительный код
            if (args[i].isNegative()){
                args[i].inverseNumber();
                flag = true;
            }
        }
        for (int i = args[0].getCapacity()/2 - 1 ; i > -1  ; i--){
            int tmp = args[0].getDenomBit(i)+args[1].getDenomBit(i)+additionalBit;
            if (tmp > 1 ){
                additionalBit = 1;
                if (tmp > 2)
                    tmp /= 2;
                else
                    tmp = 0;
            } else {
                additionalBit = 0;
            }
            resultDenom[i] =  tmp;
        }
        for (int i = 0 ; i < args[0].getCapacity()/2  ; i++){
            int tmp = args[0].getNumerBit(i)+args[1].getNumerBit(i)+additionalBit;
            if (tmp > 1 ){
                additionalBit = 1;
                if (tmp > 2)
                    tmp /= 2;
                else
                    tmp = 0;
            } else {
                additionalBit = 0;
            }
            resultNumer[i] =  tmp;
        }
       BinaryNumber result = new BinaryNumber(resultNumer,resultDenom,rank);
        if (flag )
            for (int i = 0 ; i < args.length ; i ++)
                if (args[i].isNegative())
                    args[i].inverseNumber();
        return result;
    };

    public  static  BinaryNumber mult(BinaryNumber... args){
        int rank =  args[0].getCapacity();
        int number1[] = new int[rank];
        int number2[] = new int[rank];
        int numer[] = new int[rank/2];
        int denom[] = new int[rank/2];
        int zero[] = {0};
        int j = rank - 2 ;
        for ( int i = rank/2 - 2 ; i > -1 ;i--,j--){
            number1[j]  = args[0].getNumerBit(i);
            number2[j]  = args[1].getNumerBit(i);
        }
        for (int i = 0 ; i < args[0].getCapacity()/2; i++,j--){
            number1[j]  = args[0].getDenomBit(i);
            number2[j]  = args[1].getDenomBit(i);
        }

        BinaryNumber binNumb1 = new BinaryNumber(number1,zero,4*rank);
        BinaryNumber binNumb2 = new BinaryNumber(number2,zero,4*rank);
        BinaryNumber result = new BinaryNumber(0,4*rank);
        for (int i = 0 ; i < binNumb1.getCapacity()/2 - 1; i++){
            if (binNumb1.getNumerBit(i) == 1){
                result = BinaryArithmetic.sum(result,binNumb2);
                binNumb2.moveLeftNumer();

            }   else {
                binNumb2.moveLeftNumer();
            }
        }

        for (int i = 0 ; i < rank/2 - 1; i++){
            numer[i] = result.getNumerBit(i+rank);
            denom[i] = result.getNumerBit(rank-1-i);
        }

        BinaryNumber resultNumber  = new BinaryNumber(numer,denom,rank);
        if (args[0].isNegative() ^ args[1].isNegative()){
            resultNumber.setNegative(true);
        }
        return     resultNumber;
    }
}



