package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 25.03.13
 * Time: 18:55
 */
public class BinaryNumber extends Number {
    private int capacity;
    private boolean negative = false;
    private double number;

    public int getNumerBit(int index){
        if (index > -1 & index < capacity/2){
            return numer[index];
        }
        return -1;
    }

    public int getDenomBit(int index){
        if (index > -1 & index < capacity/2){
            return denom[index];
        }
        return -1;
    }

    public int[] getDenom(){
        return denom;
    }

    public int[] getNumer(){
        return numer;
    }

    public  boolean  isNegative(){
        return negative;
    }

    public void setNegative(boolean negative){
        number = -Math.abs(number);
        this.negative = negative;
    }
    public int getCapacity() {
        return capacity;
    }

    public double getNumber(){
        double num = 0;
        for (int i = 0 ; i < capacity/2  ; i++){
            num += denom[i]  * Math.pow(2,-i-1);
        }
        for (int i = 0 ; i < capacity/2  ; i++){
            num += numer[i]  * Math.pow(2,i);
        }
        if (isNegative())
            num *= -1;
        return num;
    }

    private  int getNumberLenght(int number){
        int num = number;
        int lenght = 0;
        while (num > 0){
            num /= 2;
            lenght++;
        }
        return lenght;
    }

    private void moveRightNumer(){
        for (int i = 0; i < capacity/2-1 ; i++)
            numer[i] = numer[i+1];
    }

    public void moveLeftNumer(){
        number *= 2;
        for (int i = capacity/2-3 ; i > -1 ; i--)
            numer[i+1] = numer[i];
        numer[0] = denom[0];
        moveLeftDenom();
    }

    public void moveRightDenom(){
        number /= 2;
        for (int i = capacity/2-2 ; i > -1  ; i--)
            denom[i+1] = denom[i];
        denom[0] = numer[0];
        moveRightNumer();
    }

    private void moveLeftDenom(){
        for (int i = capacity/2-2 ; i > -1 ; i--)
            denom[i] = denom[i+1];
    }

    public int compareNumerWithNumer(int[] numer){
        int lenght = numer.length;
        for (int i = lenght - 2 ; i > -1 ; i--){
            if (this.numer[i] > numer[i])
                return 1;
            if (this.numer[i] < numer[i])
                return -1;
        }
        return 0;
    }

    public void inverseNumber(){

        for (int i = 0 ; i < capacity/2 ; i++){
           numer[i] = numer[i] > 0 ? 0 : 1;
           denom[i] = denom[i] > 0 ? 0 : 1;
        }
        denom[capacity/2-1] += 1;
        for (int i =  capacity/2 - 1 ; i > 0 ; i--){
            if (denom[i] > 1){
                if (denom[i] > 2)
                    denom[i] /= 2;
                else
                    denom[i] = 0;
                denom[i-1] ++;
            }
        }
        if (denom[0] > 1){
            if (denom[0] > 2)
                denom[0] /= 2;
            else
                denom[0] = 0;
            numer[0] ++;
        }
        for (int i = 0 ; i < capacity/2-1 ; i++){
            if (numer[i] > 1){
                if (numer[i] > 2)
                    numer[i] /= 2;
                else
                    numer[i] = 0;
                numer[i+1] ++;
            }
        }
    }



    public void setNumber(BinaryNumber number){
        this.number = number.number;
        this.negative = number.negative;
        this.capacity = number.capacity;
        this.numer = new int[capacity/2];
        this.denom = new int[capacity/2];

        for (int i = 0 ; i < number.numer.length - 1 ; i++){
            this.numer[i] = number.numer[i];
            this.denom[i] = number.denom[i];
        }
    }

    public BinaryNumber(int[] numer,int[] denom,int capacity){
        this.numer = new int[capacity/2];
        this.denom = new int[capacity/2];

        for (int i = 0 ; i < numer.length ; i++){
            this.numer[i] = numer[i] > 0 ? 1 : 0;
        }

        for (int i = 0 ; i < denom.length ; i++){
            this.denom[i] = denom[i] > 0 ? 1 : 0;
        }
        this.capacity = capacity;
        if (numer[numer.length-1] == 1){
            setNegative(true);
            inverseNumber();
            this.number = getNumber();
        } else {
            this.number = getNumber();
        }
    }

    public BinaryNumber(double number,int capacity){
        this.capacity = capacity;
        numer = new int[capacity/2];
        denom = new int[capacity/2];

        int num = (int) Math.abs( number );
        double den = Math.abs(number) - num;

        int counter = getNumberLenght(num) - 1;
        // initialize  numer
        if (counter > capacity/2 - 1 & number > 0) {
            Exeptions.e(Exeptions.OUT_OF_RANGE);
        }   else {
            counter = 0;
            while (num > 0){
                numer[counter] = num%2;
                num /= 2;
                counter++;
            }
            counter = 0;
            while (den != 0)
            {
                num = (int) (den*2);
                denom[counter] = num;
                den = den * 2 - num;
                ++counter;
                if (counter == capacity/2 - 1) // тут понимаем, процесс затянулся и число может быть вообще иррационально, поставим точки
                {
                    break;
                }
            }
        }
        if (number < 0) {
           negative = true;
        }
        this.number = getNumber();
    }


}
