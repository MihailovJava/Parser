package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 25.03.13
 * Time: 18:55
 */
public class BinaryArithmetic {
   public static BinaryNumber sum(BinaryNumber... args){
       int minrank;
       int maxrank;
       int maxnum;
       int minmun;

       if(args[0].getRank() < args[1].getRank() ){
           minrank = args[0].getRank();
           maxrank = args[1].getRank();
           maxnum = 1;
           minmun = 0;
       } else {
           minrank = args[1].getRank();
           maxrank = args[0].getRank();
           maxnum = 0;
           minmun = 1;
       }

        int additionalBit = 0;

        int[] resultNumber  = new int[maxrank];

        for (int i = 1,j = maxrank-1 ; i < minrank  ; i++,j--){
            int tmp = args[minmun].getBit(minrank - i)+args[maxnum].getBit(j)+additionalBit;
            if (tmp > 1 ){
                additionalBit = 1;
                tmp %= 2;
            } else {
                additionalBit = 0;
            }
            resultNumber[j] =  tmp;
        }
       for (int i  = maxrank - minrank - 1 ; i > -1 ; i--){
           int tmp = args[maxnum].getBit(i)+additionalBit;
           if (tmp > 1 ){
               additionalBit = 1;
               tmp %= 2;
           } else {
               additionalBit = 0;
           }
           resultNumber[i] =  tmp;
       }

       boolean out_of_range_flag = ((additionalBit == 1) || (args[0].getBit(0) == 0 && args[1].getBit(0) == 0) ) && (resultNumber[0] == 1);

       if ( out_of_range_flag)
           Exeptions.getInstance().e(Exeptions.OUT_OF_RANGE);

       BinaryNumber result = new BinaryNumber(resultNumber,maxrank,args[maxnum].getWeightCoeff());
       return result;
    };

    public  static  BinaryNumber mult(BinaryNumber... args){
        int rank = args[0].getRank();
        int weightCoeff = args[0].getWeightCoeff();
        boolean flag = false;
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].isNegative()){
                args[i].inverseNumber();
                flag = true;
            }
        }

        BinaryNumber numb2 = new BinaryNumber(args[1].getBitArray(),2*rank,rank+weightCoeff);

        BinaryNumber res = new BinaryNumber(0,2*rank,weightCoeff);

        for (int i = rank - 1 ; i > 0 ; i--){
            if (args[0].getBit(i) == 1) {
                res = BinaryArithmetic.sum(res,numb2);
                numb2.moveLeft();
            }   else {
                numb2.moveLeft();
            }
        }
        for (int i = 0 ; i < weightCoeff + 1; i++){
            res.moveLeft();
            if (res.getBit(1) ==  1 && i < weightCoeff)
                Exeptions.getInstance().e(Exeptions.OUT_OF_RANGE);
        }
        int result[] = new int[rank];
        for ( int i = 1 ; i < rank ; i++ ){
           result[i] = res.getBit(i);
        }

        BinaryNumber resultBinary = new BinaryNumber(result,rank,weightCoeff);
        resultBinary.setNegative( args[1].isNegative() ^ args[0].isNegative());
        if (flag){
            for (int i = 0 ; i < args.length ; i++){
                if (args[i].isNegative()){
                    args[i].inverseNumber();
                }
            }
        }
        return resultBinary;
    }
}



