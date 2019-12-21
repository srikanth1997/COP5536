/*
    methods and description
    void getMin()               - returns the min element in min heap
    void getExecutionTime()     - returns the execution time of min element
    void insert_heap(int exe_time, int tot_time, int build_num)





*/

public class MinHeap {


    private int execution_time[];
    private int building_num[];
    private int total_time[];
    private int size;
    private int maxSize;
    private static final int ROOT = 1;


    public MinHeap(int maxSize) {
        this.maxSize = maxSize;
        this.size = 0;
        execution_time = new int[this.maxSize + 1];
        execution_time[0] = Integer.MIN_VALUE;
        total_time = new int[this.maxSize + 1];
        total_time[0] = Integer.MIN_VALUE;
        building_num = new int[this.maxSize + 1];
        building_num[0] = Integer.MIN_VALUE;
    }

    /*
    Name        : buildHeap
    Parameters  : -
    return type : void
    Description : maintains the heap property by calling heapify for every internal node
    */
    public void buildHeap() {
        for (int pos = (size / 2); pos >= 1; pos--) {
            heapify(pos);
        }
    }

    /*
    Name        : isLeaf
    Parameters  : int pos
    return type : boolean
    Description : check and returns whether the node at position pos is leaf or not
    */
    public boolean isLeaf(int pos) {
        if (pos > size / 2 && pos <= size)
            return true;
        return false;
    }

    /*
    Name        : heapify
    Parameters  : int pos
    return type : void
    Description : maintains the heap property by doing necessary operations
    */
    public void heapify(int pos) {
        if (!isLeaf(pos)) {
            if (leftChild(pos) != -1 && rightChild(pos) != -1 && execution_time[leftChild(pos)] == execution_time[rightChild(pos)] && execution_time[pos] == execution_time[leftChild(pos)]) {
                if (building_num[pos] < building_num[leftChild(pos)] && building_num[pos] < building_num[rightChild(pos)])
                    return;
                if (building_num[leftChild(pos)] < building_num[rightChild(pos)]) {
                    swap(pos, leftChild(pos));
                    heapify(leftChild(pos));
                } else {
                    swap(pos, rightChild(pos));
                    heapify(rightChild(pos));
                }
            } else if (leftChild(pos) != -1 && execution_time[pos] >= execution_time[leftChild(pos)]) {
                if (rightChild(pos) != -1 && execution_time[leftChild(pos)] > execution_time[rightChild(pos)]) {
                    if (execution_time[pos] == execution_time[rightChild(pos)] && building_num[pos] < building_num[rightChild(pos)]) {
                        return;
                    }
                    swap(pos, rightChild(pos));
                    heapify(rightChild(pos));
                    return;
                } else if (rightChild(pos) != -1 && execution_time[leftChild(pos)] == execution_time[rightChild(pos)]) {
                    if (building_num[leftChild(pos)] < building_num[rightChild(pos)]) {
                        swap(pos, leftChild(pos));
                        heapify(leftChild(pos));
                        return;
                    } else {
                        swap(pos, rightChild(pos));
                        heapify(rightChild(pos));
                        return;
                    }
                }
                if (execution_time[pos] == execution_time[leftChild(pos)] && building_num[pos] < building_num[leftChild(pos)]) {
                    return;
                }
                swap(pos, leftChild(pos));
                heapify(leftChild(pos));
            } else if (rightChild(pos) != -1 && execution_time[pos] >= execution_time[rightChild(pos)]) {
                if (execution_time[pos] == execution_time[rightChild(pos)] && building_num[pos] < building_num[rightChild(pos)]) {
                    return;
                }
                swap(pos, rightChild(pos));
                heapify(rightChild(pos));
            } //else if ()
        }
    }

    /*
    Name        : getSize
    Parameters  : -
    return type : int
    Description : returns the size of min heap
    */
    public int getSize() {
        return size;
    }

    /*
    Name        : leftChild
    Parameters  : int pos
    return type : int
    Description : returns the right child of node at position pos
    */
    public int leftChild(int pos) {
        if (2 * pos <= size)
            return 2 * pos;
        else
            return -1; //checking for -1 condition should be done
    }

    /*
    Name        : rightChild
    Parameters  : int pos
    return type : int
    Description : returns the right child of node at position pos
    */
    public int rightChild(int pos) {
        if (2 * pos + 1 <= size)
            return 2 * pos + 1;
        else
            return -1; //checking for -1 condition should be done.
    }

    /*
    Name        : parent
    Parameters  : int pos
    return type : int
    Description : returns the parent of node at position pos
    */
    public int parent(int pos) {
        return pos / 2;
    }

    /*
    Name        : swap
    Parameters  : int node1, int node2
    return type : void
    Description : swaps the execution times, total times adn building number among the given positions
    */
    public void swap(int node1, int node2) {
        int temp;
        temp = execution_time[node1];
        execution_time[node1] = execution_time[node2];
        execution_time[node2] = temp;
        temp = total_time[node1];
        total_time[node1] = total_time[node2];
        total_time[node2] = temp;
        temp = building_num[node1];
        building_num[node1] = building_num[node2];
        building_num[node2] = temp;
    }

    /*
    Name        : removeMin
    Parameters  : -
    return type : int
    Description : removes the root of min heap i.e returns the building number of root.
    */
    public int removeMin() {
        int min_node_build_num = building_num[ROOT];
        execution_time[ROOT] = execution_time[size]; //changed from size-- to size and decreased below
        total_time[ROOT] = total_time[size];
        building_num[ROOT] = building_num[size];
        size--;

        heapify(ROOT);
        return min_node_build_num; //returning the root's building number
    }

    /*
    operations acc to rising city problem
    */
    /*
    Name        : insert_heap
    Parameters  : int exe_time, int tot_time, int build_num
    return type : void
    Description : inserts the execution time, total time and building in their valid position.
    */
    public void insert_heap(int exe_time, int tot_time, int build_num) {
        if (size < maxSize) {
            execution_time[++size] = exe_time;
            total_time[size] = tot_time;
            building_num[size] = build_num;
            int current = size;
            while (execution_time[current] < execution_time[parent(current)]) {
                swap(current, parent(current));
                current = parent(current);
            }
        }
    }

    // getter method for execution time
    public int getExecutionTime() {
        return execution_time[ROOT];
    }

    // getter method for total time
    public int getTotalTime() {
        return total_time[ROOT];
    }

    // getter method for building number
    public int getBuildingNum() {
        return building_num[ROOT];
    }

    public void updateExecutionTimeByX(int localCounter) {
        execution_time[ROOT] += localCounter;
    }


}
