package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 05.04.13
 * Time: 19:43
 */
public class Interpreter {
    public static final int RANG = 64;

    public BinaryNumber getRegister(int index) {
        if (index > -1 && index < registers.length)
            return registers[index];
        return registers[0];
    }

    public BinaryNumber getRam(int address){
        if (address > -1 && address < ram.length)
            return ram[address];
        return ram[0];
    }

    private BinaryNumber[] registers = new BinaryNumber[20]; // модель регистрового файла
    private BinaryNumber[] ram = new BinaryNumber[20];  // модель оперативной памяти

    private int ic; // счетчик интсрукций
    private String[] instructions;

    private int factorial(int n)
    {
        if (n == 0) return 1;
        return n * factorial(n-1);
    }

   public Interpreter(){
       for( int i = 0 ; i < 8 ; i++){
           ram[i] = new BinaryNumber(1./factorial(i+2),RANG);    // загрузка в оперативную память констант
           registers[i] = ram[i];                               // загрузка рабочих регистров
       }
       for ( int i = 8 ; i < ram.length ; i++){
           ram[i] = new BinaryNumber(0,RANG);
       }
       for ( int i = 8 ; i < registers.length ; i++){
           registers[i] = new BinaryNumber(0,RANG);
       }
   }
   private String[] removeEmptyLiterals(String[] literals){
    String result[] = new String[literals.length];
    for ( int count = 0 ,i = 0 ; i < literals.length ; i ++){
        if (literals[i].equals("")){
        } else {
            result[count] = literals[i];
            count++;
        }
    }
    return result;
   }

   public String parse(String command){
        clearRegisterFile();
        ic = 0;
        instructions = command.split("\n");
        for ( ; ic < instructions.length ; ic++){
            String[] literals = instructions[ic].split(" ");
            literals = removeEmptyLiterals(literals);
            toDo(literals);
        }
        return String.valueOf(registers[registers.length-1].getNumber());
    }

    private void clearRegisterFile() {
        for ( int i = 8 ; i < registers.length ; i++){
            registers[i] = new BinaryNumber(0,RANG);
        }
    }

    private void toDo(String... literals){
        if (literals[0].equals("MOVE")){      // MOVE val param
            BinaryNumber A = getNumberOf(literals[2]);
            A.setNumber(getNumberOf(literals[1]));
        }
        if (literals[0].equals("ADD")){ // ADD a b c   do c = a + b
            BinaryNumber A = getNumberOf(literals[1]);
            BinaryNumber B = getNumberOf(literals[2]);
            BinaryNumber C = getNumberOf(literals[3]);
            C.setNumber(BinaryArithmetic.sum(A,B));
            registers[registers.length-1].setNumber(C);
        }
        if (literals[0].equals("MULT")){ // MULT a b c  do c = a * b
            BinaryNumber A = getNumberOf(literals[1]);
            BinaryNumber B = getNumberOf(literals[2]);
            BinaryNumber C = getNumberOf(literals[3]);
            C.setNumber(BinaryArithmetic.mult(A,B));
            registers[registers.length-1].setNumber(C);
        }
        if (literals[0].equals("PROC")){           // JUMP label do ic = indexOf(label)
            for (int i = 0 ; i < instructions.length ; i++){
                String tmpliterals[] = instructions[i].split(" ");
                tmpliterals = removeEmptyLiterals(tmpliterals);
                if (tmpliterals[0].equals(literals[1])){
                    ic = i;
                    return;
                }
            }
        }
        if (literals[0].equals("JUMP.NZ")){
            if (registers[registers.length-1].getNumber()!= 0){
                for (int i = 0 ; i < instructions.length ; i++){
                    String tmpliterals[] = instructions[i].split(" ");
                    tmpliterals = removeEmptyLiterals(tmpliterals);
                    if (tmpliterals[0].equals(literals[1])){
                        ic = i;
                        return;
                    }
                }
            }
        }
        if (literals[0].equals("RET")){
            for (int i = 0 ; i < instructions.length ; i++){
                String tmpliterals[] = instructions[i].split(" ");
                tmpliterals = removeEmptyLiterals(tmpliterals);
                if (tmpliterals[0].equals("PROC")){
                    if (tmpliterals[1].equals(literals[1]))
                        ic = i;
                        instructions[i] = "DONE "+ instructions[i];
                        return;
                }
            }
        }
        if (literals[0].equals("STOP")){
            ic = instructions.length;
        }
    }

    private BinaryNumber getNumberOf(String param){
        if (isRegistrLocation(param) != -1){
            return registers[isRegistrLocation(param)];
        }else
        if (isMemoryLocation(param)!= -1){
            return ram[isMemoryLocation(param)];
        } else   return new BinaryNumber(Double.valueOf(param),RANG) ;
    }

    private int isRegistrLocation(String param){
        for (int i = 0 ; i < registers.length; i++){
            if (param.equals("A"+String.valueOf(i)))
                return i;
        }
        return -1;
    }

    private int isMemoryLocation(String param){
        for (int i = 0 ; i < ram.length; i++){
            if (param.equals("["+String.valueOf(i)+"]"))
                return i;
        }
        return -1;
    }
}
