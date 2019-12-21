import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class risingCity {
    public static int counter = 0;
    static RedBlackTree rbt = new RedBlackTree();
    static MinHeap minHeap = new MinHeap(2001);

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        PrintStream printStream = new PrintStream(new File("output_file.txt"));
        System.setOut(printStream);

        Scanner sc = new Scanner(file);
        ArrayList<Integer> inputCounter = new ArrayList<>();
        ArrayList<String> inputCommand = new ArrayList<>();

        String line = "";
        int index, counterTime;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            index = line.indexOf(':');
            counterTime = Integer.parseInt(line.substring(0, index));
            if (inputCounter.contains(counterTime)) { // case where there are multiple operations at same time
                String cmd = inputCommand.get(inputCounter.indexOf(counterTime)) + ";" + line.substring(index + 1, line.length());
                inputCommand.add(inputCounter.indexOf(counterTime), cmd); // appending the second command to first one
            } else {
                inputCommand.add(line.substring(index + 2, line.length()));
            }
            inputCounter.add(counterTime);


        }
        index = line.indexOf(':');
        int endTime = Integer.parseInt(line.substring(0, index));
        buildingProcess(endTime, inputCounter, inputCommand);

    }

    /*
    Name        : buildingProcess
    Parameters  : int endTime, ArrayList<Integer> inputCounter, ArrayList<String> inputCommand
    return type : void
    Description : buildingProcess handles all the operation executions and also handles the global counter.
    */
    public static void buildingProcess(int endTime, ArrayList<Integer> inputCounter, ArrayList<String> inputCommand) {
        int startIndex, middleIndex, endIndex;
        int num1, num2, x, y = 0, minTime = 5;
        ArrayList<Integer> insertCommands = new ArrayList<>();
        if (counter == 0){
            Integer arr[] = new Integer[2];
            startIndex = inputCommand.get(0).indexOf("(") + 1;
            middleIndex = inputCommand.get(0).indexOf(",");
            endIndex = inputCommand.get(0).indexOf(")");
            arr[0] = Integer.parseInt(inputCommand.get(0).substring(startIndex, middleIndex));
            arr[1] = Integer.parseInt(inputCommand.get(0).substring(middleIndex + 1, endIndex));
            insertCommands.addAll(Arrays.asList(arr));
            int build_num = insertCommands.get(0);
            int total_time = insertCommands.get(1);
            rbt.insert(new Building(build_num, 0, total_time));
            minHeap.insert_heap(0, total_time, build_num);
            insertCommands.remove(1);
            insertCommands.remove(0);
            counter++;
        }
        while (counter <= endTime || minHeap.getSize() != 0) {
            if (minHeap.getSize() != 0) {
                minTime = minHeap.getTotalTime() - minHeap.getExecutionTime();
            } else {
                minTime = 5;
            }
            minTime = (minTime < 5) ? minTime : 5;
            x = minTime;

            while (minTime > 0 && counter != 0) { // local time while loop
                y = 0;
                if (inputCounter.contains(counter)) { // if there is any operation on that day
                    int index = inputCounter.indexOf(counter);

                    if (!inputCommand.contains(';')) {  // considering there are no multiple op at same time
                        if (inputCommand.get(index).contains("Print") && inputCommand.get(index).contains(",")) { // cond for ranged printing
                            startIndex = inputCommand.get(index).indexOf("(") + 1;
                            middleIndex = inputCommand.get(index).indexOf(",");
                            endIndex = inputCommand.get(index).indexOf(")");
                            num1 = Integer.parseInt(inputCommand.get(index).substring(startIndex, middleIndex));
                            num2 = Integer.parseInt(inputCommand.get(index).substring(middleIndex + 1, endIndex));
                            y = x - minTime + 1;
                            y = minHeap.getExecutionTime() + y;
                            rbt.updateExecutionTime(minHeap.getBuildingNum(), y);
//                            rbt.printBuilding(num1, num2);
                            rbt.printTree(num1, num2);
                        } else if (inputCommand.get(index).contains("Insert")) {
                            Integer arr[] = new Integer[2];
                            startIndex = inputCommand.get(index).indexOf("(") + 1;
                            middleIndex = inputCommand.get(index).indexOf(",");
                            endIndex = inputCommand.get(index).indexOf(")");
                            arr[0] = Integer.parseInt(inputCommand.get(index).substring(startIndex, middleIndex));
                            arr[1] = Integer.parseInt(inputCommand.get(index).substring(middleIndex + 1, endIndex));
                            insertCommands.addAll(Arrays.asList(arr));

                            rbt.insert(new Building(arr[0], 0, arr[1]));

                        } else if (inputCommand.get(index).contains("Print") && !inputCommand.get(index).contains(",")) { // condition for single node printing
                            startIndex = inputCommand.get(index).indexOf("(") + 1;
                            endIndex = inputCommand.get(index).indexOf(")");
                            num1 = Integer.parseInt(inputCommand.get(index).substring(startIndex, endIndex));
                            rbt.printTree(num1);
                        }
                    }
                } //end of if cond

                counter++;
                minTime--;
            } // end of inner while loop
            if ((minHeap.getExecutionTime() + x) == minHeap.getTotalTime()) { // building is finished
                int build_num = minHeap.getBuildingNum();
                int min = minHeap.removeMin();
                int tempCounter = counter-1;
                System.out.println("(" + min + "," + tempCounter + ")");
                rbt.deleteKeyPair(build_num);
            } else {
                minHeap.updateExecutionTimeByX(x); //increase the execution time to new value
                rbt.updateExecutionTime(minHeap.getBuildingNum(), minHeap.getExecutionTime());
            }

            minHeap.buildHeap();
            while (insertCommands.size() != 0) {
                int build_num = insertCommands.get(0);
                int total_time = insertCommands.get(1);
//                rbt.insert(new Building(build_num, 0, total_time));
                minHeap.insert_heap(0, total_time, build_num);
                insertCommands.remove(1);
                insertCommands.remove(0); // removing insert values of 1 insert commmand
            }
        } // end of outer while loop

    } // end of building process

} // end of risingCity class
