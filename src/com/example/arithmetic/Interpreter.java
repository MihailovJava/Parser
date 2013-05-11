package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 05.04.13
 * Time: 19:43
 */
public class Interpreter {


    private int ic; // счетчик интсрукций
    private String[] instructions;
    int instruction_code[] = new int[FonNeiman.RANK];;


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
        ic = 0;
        FonNeiman.getInstance().clear();
        Exeptions.getInstance().clear();
        instructions = command.split("\n");
        for ( ; ic < instructions.length ; ic++){
            String[] literals = instructions[ic].split(" ");
            literals = removeEmptyLiterals(literals);
            toDo(literals);
        }
        FonNeiman.getInstance().execute();
        BinaryNumber result = new BinaryNumber(FonNeiman.getInstance().getData(new int[]{0,1,0,0,1,1},0),
               FonNeiman.RANK,
               FonNeiman.WEIGHT);
        return String.valueOf(result.getNumberWithWeight());
    }

    private void setInstructionCommand(int code){
        for (int i = 0 ;  i < FonNeiman.INSTRUCTION_CODE ; i++ ){
            instruction_code[FonNeiman.INSTRUCTION_CODE - 1 - i] = code % 2;
            code /= 2;
        }
    }
    private int[] getMemory(int memory,int flag){
        int[] memory_array = new int[FonNeiman.MEMORY_FOR_DATA];

        if ( flag > 1 ){
           memory_array[0] = 2;
           memory_array[FonNeiman.MEMORY_FOR_DATA-1] = memory;
        }   else {
           for (int i = 0 ; memory > 0 && i < FonNeiman.MEMORY_FOR_DATA ; i++){
                memory_array[FonNeiman.MEMORY_FOR_DATA - 1 - i] = memory % 2;
                memory /= 2;
           }
           memory_array[0] = flag;
        }


        return memory_array;
    }
    private void setFirstParam(int[] memory){

        for (int i = 0 ; i < FonNeiman.MEMORY_FOR_DATA ; i++){
            instruction_code[i+FonNeiman.INSTRUCTION_CODE] = memory[i];
        }
    }
    private void setSecondParam(int[] memory){
        for (int i = 0 ; i < FonNeiman.MEMORY_FOR_DATA ; i++){
            instruction_code[i + FonNeiman.MEMORY_FOR_DATA + FonNeiman.INSTRUCTION_CODE] = memory[i];
        }
    }

    private void setThirdParam(int[] memory){
        for (int i = 0 ; i < FonNeiman.MEMORY_FOR_DATA ; i++){
            instruction_code[i + 2*FonNeiman.MEMORY_FOR_DATA + FonNeiman.INSTRUCTION_CODE] = memory[i];
        }
    }

    private void toDo(String... literals){
        if (literals[0].equals("MOVE")){      // MOVE val param
            setInstructionCommand(0);
            int param1[] = getNumber(literals[1]);
            int param2[] = getNumber(literals[2]);
            setFirstParam(getMemory(param1[0],param1[1]));
            setSecondParam(getMemory(param2[0],param2[1]));
            FonNeiman.getInstance().setNextCommand(instruction_code);

        }   else

        if (literals[0].equals("ADD")){ // ADD a b c   do c = a + b
            setInstructionCommand(1);
            int param1[] = getNumber(literals[1]);
            int param2[] = getNumber(literals[2]);
            int param3[] = getNumber(literals[3]);
            setFirstParam(getMemory(param1[0],param1[1]));
            setSecondParam(getMemory(param2[0],param2[1]));
            setThirdParam(getMemory(param3[0],param3[1]));
            FonNeiman.getInstance().setNextCommand(instruction_code);
        }   else

        if (literals[0].equals("MULT")){ // MULT a b c  do c = a * b
            setInstructionCommand(2);
            int param1[] = getNumber(literals[1]);
            int param2[] = getNumber(literals[2]);
            int param3[] = getNumber(literals[3]);
            setFirstParam(getMemory(param1[0],param1[1]));
            setSecondParam(getMemory(param2[0],param2[1]));
            setThirdParam(getMemory(param3[0],param3[1]));
            FonNeiman.getInstance().setNextCommand(instruction_code);
        }   else

        if (literals[0].equals("PROC")){           // JUMP label do ic = indexOf(label)
            setInstructionCommand(3);
            for (int i = 0 ; i < instructions.length ; i++){
                String tmpliterals[] = instructions[i].split(" ");
                tmpliterals = removeEmptyLiterals(tmpliterals);
                if (tmpliterals[0].equals(literals[1])){
                    int param1[] = getNumber(String.valueOf(ic));
                    int param2[] = getNumber(String.valueOf(i));
                    setFirstParam(getMemory(param1[0],param1[1]));
                    setSecondParam(getMemory(param2[0],param2[1]));
                    FonNeiman.getInstance().setNextCommand(instruction_code);
                    return;
                }
            }
        } else

        if (literals[0].equals("JUMP.NZ")){
                setInstructionCommand(4);
                for (int i = 0 ; i < instructions.length ; i++){
                    String tmpliterals[] = instructions[i].split(" ");
                    tmpliterals = removeEmptyLiterals(tmpliterals);
                    if (tmpliterals[0].equals(literals[1])){
                        int param2[] = getNumber(String.valueOf(i));
                        setFirstParam(getMemory(param2[0],param2[1]));
                        FonNeiman.getInstance().setNextCommand(instruction_code);
                        return;
                    }
                }
        } else

        if (literals[0].equals("RET")){
            setInstructionCommand(5);
            FonNeiman.getInstance().setNextCommand(instruction_code);
        } else

        if (literals[0].equals("STOP")){
            setInstructionCommand(6);
            FonNeiman.getInstance().setNextCommand(instruction_code);
        }
        else
        {
            setInstructionCommand(7);
            FonNeiman.getInstance().setNextCommand(instruction_code);
        }
    }

    private int[] getNumber(String param){
        int num[] = isMemoryLocation(param)[1] > 0 ?  isMemoryLocation(param) : isRegistrLocation(param);
        if (!( num[0] < 0)){
            return  num;
        } else {
            num[0] = Integer.valueOf(param);
            num[1] = 2;
        }
        return num;
    }

    private int[] isRegistrLocation(String param){
        for (int i = 0 ; i < FonNeiman.REGISTERS; i++){
            if (param.equals("A"+String.valueOf(i)))
                return new int[]{i,0};
        }
        return new  int[]{-1,0};
    }

    private int[] isMemoryLocation(String param){
        for (int i = 0 ; i < FonNeiman.DATA ; i++){
            if (param.equals("["+String.valueOf(i)+"]"))
                return new int[]{i,1};
        }
        return new int[]{-1,0};
    }
}
