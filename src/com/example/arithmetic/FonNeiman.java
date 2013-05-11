package com.example.arithmetic;

/**
 * User: Nixy
 * Date: 02.05.13
 * Time: 21:00
 */
public class FonNeiman {

    static FonNeiman me;



    public static final int REGISTERS = 20;
    public static final int COMMANDS = 80;
    public static final int DATA = 20;
    public static final int RANK = 24;
    public static final int WEIGHT = 4;
    public static final int INSTRUCTION_CODE = 4;
    public static final int MEMORY_FOR_DATA = (RANK-INSTRUCTION_CODE) / 3;

    public static final int REGISTER_TYPE = 0;
    public static final int DATA_TYPE = 1;
    private static final int NUMER_TYPE = 2;

    public static final int COMMANDS_FIRST_ADR = 0;
    public static final int REGISTERS_FIRST_ADR = COMMANDS_FIRST_ADR + COMMANDS;
    public static final int DATA_FIRST_ADR = REGISTERS_FIRST_ADR + REGISTERS;

    public static final int[] RESULT_REGISTER = new int[]{0,1,0,0,1,1};

    public static final int MOVE_CODE = 0;
    public static final int SUM_CODE = 1;
    private static final int MULT_CODE = 2;
    private static final int PROC_CODE = 3;
    public static final int JUMP_NZ_CODE = 4;
    public static final int RET_CODE = 5;
    public static final int STOP_CODE = 6;

    int memory[];
  //  int memorycommands[][] = new int[COMMANDS][RANK];
 //   int memorydata[][] = new int[DATA][RANK];
  //  int memoryreg[][] = new int[REGISTERS][RANK];
    int code;
    int[] instruction = new int[RANK];

    public int instruction_counter;

    private int fact(int index){
        int res = 1;
        if (index < 1)
            return res;
        else
         for (int i = index ; i > 1 ; i--)
            res *= i;
        return res;
    }

    private FonNeiman(){
        clear();
    }

    public  void clear(){
            instruction_counter = 0;
            memory  = new int[COMMANDS*RANK+REGISTERS*RANK+DATA*RANK];
            for (int i = 0 ; i < 7 ; i++){
                BinaryNumber tmp = new BinaryNumber(1./fact(i+2),RANK,WEIGHT);
                int num[] = tmp.getBitArray();
                for (int j = 0; j < RANK ; j++)  {
                    memory[REGISTERS_FIRST_ADR*RANK+RANK*i+j] = num[j];
                    //   memoryreg[i][j] = num[j];
                }
            }
    }

    public static FonNeiman getInstance(){
        if (me == null)
            me = new  FonNeiman();
        return me;
    }

    public void execute(){

        for(instruction_counter = 0 ; instruction_counter < COMMANDS ; instruction_counter++){
            for (int i = 0 ; i < RANK ; i++){
                instruction[i] = memory[instruction_counter*RANK+i+COMMANDS_FIRST_ADR];
            }
            toDo(instruction);
        }

    }

    public double getRAM(int adr){
        int[] location = new int[MEMORY_FOR_DATA];
        for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i--){
            location[i] = adr%2;
            adr /= 2;
        }
        BinaryNumber number = new BinaryNumber(getData(location,1),RANK,WEIGHT);
        return number.getNumberWithWeight();
    }
    public double getREG(int adr){
        int[] location = new int[MEMORY_FOR_DATA];
        for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i--){
            location[i] = adr%2;
            adr /= 2;
        }
        BinaryNumber number = new BinaryNumber(getData(location,0),RANK,WEIGHT);
        return number.getNumberWithWeight();
    }

    public int[] getData(int[] location,int type){
        int data[] = new int[RANK];
        if (type == REGISTER_TYPE){
            int loc = 0;
            for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i --)
                loc += location[i] * Math.pow(2,MEMORY_FOR_DATA-1-i);
            for (int i = 0 ; i < RANK ; i++)
                 data[i] = memory[loc*RANK+REGISTERS_FIRST_ADR*RANK+i] ;
        }
        if(type == DATA_TYPE){
            int loc = 0;
            for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i --)
                loc += location[i] * Math.pow(2,MEMORY_FOR_DATA-1-i);
            for (int i = 0 ; i < RANK ; i++)
                data[i] =  memory[loc*RANK+DATA_FIRST_ADR*RANK+i] ;
        }
        if (type == NUMER_TYPE){
            BinaryNumber num = new BinaryNumber(location[MEMORY_FOR_DATA-1],RANK,WEIGHT);
            data = num.getBitArray();
        }

        return  data;
    }

    public void setData(int[] data,int[] location ,int type){
        if (type == REGISTER_TYPE){
            int loc = 0;
            for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i --)
                loc += location[i] * Math.pow(2,MEMORY_FOR_DATA-1-i);
            for (int i = 0 ; i < RANK ; i++){
                memory[loc*RANK+REGISTERS_FIRST_ADR*RANK+i] = data[i];
                //memoryreg[loc][i] = data[i];
            }
        } else
        if (type == DATA_TYPE){
            int loc = 0;
            for (int i = MEMORY_FOR_DATA-1 ; i > 0 ; i --)
                loc += location[i] * Math.pow(2,MEMORY_FOR_DATA-1-i);
            for (int i = 0 ; i < RANK ; i++){
                memory[loc*RANK+DATA_FIRST_ADR*RANK+i] = data[i];
                //memorydata[loc][i] = data[i];
            }
        }

    }

    public void toDo(int[] instruction){
        code = 0;
        for (int i = 0 ; i < INSTRUCTION_CODE ; i++)
          code += instruction[i]*Math.pow(2,INSTRUCTION_CODE-i-1);

        if (code == MOVE_CODE){  // MOVE A -> B
            int dist_adr[] = new int[MEMORY_FOR_DATA];
            int data_adr[] = new int[MEMORY_FOR_DATA];
            int dist_type = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA];
            int data_type = instruction[INSTRUCTION_CODE];
            for (int i = 0 ; i < MEMORY_FOR_DATA ; i++){
                data_adr[i] = instruction[INSTRUCTION_CODE+i];
                dist_adr[i] = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA+i];

            }
            setData(getData(data_adr,data_type),dist_adr,dist_type);
        }
        if (code == SUM_CODE){  // MOVE A + B -> C
            int dist_adr[] = new int[MEMORY_FOR_DATA];
            int data1_adr[] = new int[MEMORY_FOR_DATA];
            int data2_adr[] = new int[MEMORY_FOR_DATA];
            int dist_type = instruction[INSTRUCTION_CODE+2*MEMORY_FOR_DATA];
            int data1_type = instruction[INSTRUCTION_CODE];
            int data2_type = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA];
            for (int i = 0 ; i < MEMORY_FOR_DATA ; i++){
                data1_adr[i] = instruction[INSTRUCTION_CODE+i];
                data2_adr[i] = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA+i];
                dist_adr[i] = instruction[INSTRUCTION_CODE+2*MEMORY_FOR_DATA+i];

            }
            BinaryNumber bin1 = new BinaryNumber(getData(data1_adr,data1_type),RANK,WEIGHT);
            BinaryNumber bin2 = new BinaryNumber(getData(data2_adr,data2_type),RANK,WEIGHT);
            int result[] = BinaryArithmetic.sum(bin1,bin2).getBitArray();
            setData(result,dist_adr,dist_type);
            setData(result,RESULT_REGISTER,0);
        }
        if (code == MULT_CODE){  // MOVE A + B -> C
            int dist_adr[] = new int[MEMORY_FOR_DATA];
            int data1_adr[] = new int[MEMORY_FOR_DATA];
            int data2_adr[] = new int[MEMORY_FOR_DATA];
            int dist_type = instruction[INSTRUCTION_CODE+2*MEMORY_FOR_DATA];
            int data1_type = instruction[INSTRUCTION_CODE];
            int data2_type = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA];
            for (int i = 0 ; i < MEMORY_FOR_DATA ; i++){
                data1_adr[i] = instruction[INSTRUCTION_CODE+i];
                data2_adr[i] = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA+i];
                dist_adr[i] = instruction[INSTRUCTION_CODE+2*MEMORY_FOR_DATA+i];

            }
            BinaryNumber bin1 = new BinaryNumber(getData(data1_adr,data1_type),RANK,WEIGHT);
            BinaryNumber bin2 = new BinaryNumber(getData(data2_adr,data2_type),RANK,WEIGHT);
            int result[] = BinaryArithmetic.mult(bin1, bin2).getBitArray();
            setData(result,dist_adr,dist_type);
            setData(result,RESULT_REGISTER,0);
        }
        if (code == PROC_CODE){

            int reg_for_call[] = new int[]{0,0,1,0,0,1};
            int call_type = 0;
            int calling_procedure = instruction[INSTRUCTION_CODE+2*MEMORY_FOR_DATA - 1];

            BinaryNumber call = new BinaryNumber(instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA - 1],RANK,RANK - 1);
            setData(call.getBitArray(),reg_for_call,call_type);
            instruction_counter = calling_procedure;
        }
        if (code == JUMP_NZ_CODE){

            int flag_call[] = new int[]{0,1,0,0,1,1};
            int call_type = 0;
            int calling_procedure = instruction[INSTRUCTION_CODE+MEMORY_FOR_DATA - 1];

            BinaryNumber flag_number = new BinaryNumber(getData(flag_call,call_type),RANK,RANK - 1);
            if (flag_number.getNumberWithWeight() == 0)
                 return;
            else
                instruction_counter = calling_procedure;
        }
        if (code == RET_CODE){
            int reg_for_call[] = new int[]{0,0,1,0,0,1};
            int call_type = 0;
            int call_procedure[] = getData(reg_for_call,call_type);
            int procedure_count = 0;
            for (int i = 1 ; i < RANK ; i++){
                procedure_count += call_procedure[RANK-i-1]*Math.pow(2,i);
            }
            instruction_counter = ++procedure_count;
        }
        if (code == STOP_CODE){
            instruction_counter = COMMANDS;
        }
    }

    public void setNextCommand(int command[]){
        for (int i = 0 ; i < RANK ; i++){
            memory[instruction_counter*RANK+i] = command[i];
            //memorycommands[instruction_counter][i] = command[i];
        }
        instruction_counter++;
    }
}
