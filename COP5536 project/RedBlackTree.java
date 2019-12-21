public class RedBlackTree {

    private final boolean RED = false;
    private final boolean BLACK = true;


    class RedBlackNode {

        Building element = new Building(-1, -1, -1);
        boolean color = BLACK;
        RedBlackNode left = tnil, right = tnil, parent = tnil;

        RedBlackNode(Building element) {
            this.element = element;
        }
    }

    //default tnil node for the RB Tree
    private final RedBlackNode tnil = new RedBlackNode(new Building(-1, -1, -1));

    //initialize root as tnil initially and continue
    private RedBlackNode root = tnil;

    /*
    Name        : doesNodeExist
    Parameters  : RedBlackNode find, RedBlackNode node
    return type : RedBlackNode
    Description : check whether node exists in the tree rooted at "node".
    */
    private RedBlackNode doesNodeExist(RedBlackNode find, RedBlackNode node) {
        if (root == tnil) {
            return tnil;
        }

        if (find.element.building_num < node.element.building_num) {
            if (node.left != tnil) {
                return doesNodeExist(find, node.left);
            }
        } else if (find.element.building_num > node.element.building_num) {
            if (node.right != tnil) {
                return doesNodeExist(find, node.right);
            }
        } else if (find.element.building_num == node.element.building_num) {
            return node;
        }
        return tnil;
    }

    /*
    Name        : insert
    Parameters  : Building item
    return type : void
    Description : inserts a building object into the red black tree
    */
    //Insert node into the RBT
    public void insert(Building item) {
        RedBlackNode node = new RedBlackNode(item);

        RedBlackNode temp = root;

        //Insert at root if tree is empty
        if (root == tnil) {
            root = node;
            node.color = BLACK;
            node.parent = tnil;
        } else {
            node.color = RED;
            temp = nearestPostion(node);
            if (temp != tnil) {
                if (temp.element.building_num < node.element.building_num) {
                    temp.right = node;
                    node.parent = temp;
                } else {
                    temp.left = node;
                    node.parent = temp;
                }
            }
            // Call RB_Insert_Fixup to fix the Red Black Tree Properties
            RB_Insert_Fixup(node);
        }
    }

    /*
    Name        : nearestPostion
    Parameters  : RedBlackNode target
    return type : RedBlackNode
    Description : does the post insert fixes to maintain properties of red black tree
    */
    private RedBlackNode nearestPostion(RedBlackNode target) {
        return nearestPostion(root, target);
    }

    /*
    Name        : nearestPostion
    Parameters  : RedBlackNode node, RedBlackNode target
    return type : RedBlackNode
    Description : returns the nearest node to target with subtree rooted at node
    */
    private RedBlackNode nearestPostion(RedBlackNode node, RedBlackNode target) {
        if (node.element.building_num < target.element.building_num) {
            if (node.right == tnil)
                return node;
            else {
                return nearestPostion(node.right, target);
            }
        } else {
            if (node.left == tnil)
                return node;
            else {
                return nearestPostion(node.left, target);
            }
        }
    }

    /*
    Name        : RB_Insert_Fixup
    Parameters  : RedBlackNode node
    return type : void
    Description : maintains the properties of red black tree by performing rotations and color flips
    */
    private void RB_Insert_Fixup(RedBlackNode node) {
        /* We insert new node with Color = Red
         *  RBT property violated if the parent is RED */

        while (node.parent.color == RED) {
            RedBlackNode y = tnil;

            /* Two cases. When :
             *  1. Parent is left child of it parent - Rotate Right
             *  2. Parent is right child of its parent - Rotate Left
             *  */

            if (node.parent == node.parent.parent.left) {
                y = node.parent.parent.right;

                if (y != tnil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    leftRotate(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rightRotate(node.parent.parent);
            } else {
                y = node.parent.parent.left;
                if (y != tnil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rightRotate(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                leftRotate(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    /*
    Name        : leftRotate
    Parameters  : RedBlackNode node
    return type : void
    Description : performs the left rotation based on node
    */
    void leftRotate(RedBlackNode node) {
        if (node.parent != tnil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != tnil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            // The root node is rotated to the left
            RedBlackNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = tnil;
            root = right;
        }
    }

    /*
    Name        : rightRotate
    Parameters  : RedBlackNode node
    return type : void
    Description : performs the right rotation based on node
    */
    void rightRotate(RedBlackNode node) {
        if (node.parent != tnil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != tnil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            //the root node is rotated to the right
            RedBlackNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = tnil;
            root = left;
        }
    }

    /*
    Name        : RB_transplant
    Parameters  : RedBlackNode u, RedBlackNode v
    return type : void
    Description : replace one subtree as a child of its parent with another subtree.
    */
    void RB_transplant(RedBlackNode u, RedBlackNode v) {
        if (u.parent == tnil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else
            u.parent.right = v;
        v.parent = u.parent;
    }

    /*
    Name        : deleteNode
    Parameters  : RedBlackNode z
    return type : boolean
    Description : deletes the node from red black tree
    */
    // The primary method to delete a node. Called only from the reduce method
    boolean deleteNode(RedBlackNode z) {
        if ((z = doesNodeExist(z, root)) == tnil)
            return false;
        RedBlackNode x = tnil;
        RedBlackNode y = z;
        boolean y_original_color = y.color;
        if (z.left == tnil) {
            x = z.right;
            RB_transplant(z, z.right);
        } else if (z.right == tnil) {
            x = z.left;
            RB_transplant(z, z.left);
        } else {
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                RB_transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            RB_transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (y_original_color == BLACK)
            RB_Delete_Fixup(x);
        return true;
    }

    /*
    Name        : RB_Delete_Fixup
    Parameters  : RedBlackNode x
    return type : void
    Description : helps in maintaining the red black tree properties
    */
    // deleteNode helper method to fix colors and RB Tree properties post delete
    void RB_Delete_Fixup(RedBlackNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                RedBlackNode w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                    continue;
                } else if (w.right.color == BLACK) {
                    w.left.color = BLACK;
                    w.color = RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if (w.right.color == RED) {
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                RedBlackNode w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                    continue;
                } else if (w.left.color == BLACK) {
                    w.right.color = BLACK;
                    w.color = RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                if (w.left.color == RED) {
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    /*
    Name        : treeMinimum
    Parameters  : RedBlackNode subTreeRoot
    return type : RedBlackNode
    Description : Finds the minimum element of the tree rooted at subTreeRoot
    */
    RedBlackNode treeMinimum(RedBlackNode subTreeRoot) {
        while (subTreeRoot.left != tnil) {
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    /*
    Name        : findNodebyId
    Parameters  : int building_num
    return type : RedBlackNode
    Description : finds the node with building_num
    */
    public RedBlackNode findNodebyId(int building_num) {
        return findNodebyId(root, building_num);
    }


    /*
    Name        : findNodebyId
    Parameters  : RedBlackNode node, int theID
    return type : RedBlackNode
    Description : finds the node with building_num in red black tree rooted at node.
    */
    private RedBlackNode findNodebyId(RedBlackNode node, int theID) {
        while (node != tnil) {
            int rootValue = node.element.building_num;
            if (theID < rootValue)
                node = node.left;
            else if (theID > rootValue)
                node = node.right;
            else {
                return node;
            }
        }
        return tnil;
    }


    /*
    Name        : printTree
    Parameters  : int lowest, int highest
    return type : void
    Description : prints the nodes in red black tree in inorder traversal
    */
    public void printTree(int lowest, int highest) {
        if (root == tnil)
            System.out.println("(0,0,0)");
        else {
            printTree(root, lowest, highest);
            System.out.println();
        }
    }

    /*
    Name        : printTree
    Parameters  : int building_num
    return type : void
    Description : prints the properties of building with building number passed.
    */
    public void printTree(int building_num) {
        RedBlackNode node = findNodebyId(building_num);
        if (node != tnil)
            System.out.println("(" + node.element.building_num + "," + node.element.execution_time + "," + node.element.total_time + ")");
        else
            System.out.println("(0,0,0)");
    }

    /*
    Name        : printTree
    Parameters  : RedBlackNode node, int lowest, int highest
    return type : void
    Description : prints the properties of building whose building number is in the range passed
    */
    public void printTree(RedBlackNode node, int lowest, int highest) {
        if (node == tnil)
            return;
        if (node.element.building_num > lowest) {
            printTree(node.left, lowest, highest);
        }
        if (node.element.building_num >= lowest && node.element.building_num <= highest) {
            if (node.left != tnil && node.left.element.building_num >= lowest) {
                System.out.print(",");
            }
            System.out.print("(" + node.element.building_num + "," + node.element.execution_time + "," + node.element.total_time + ")");
            if (node.right != tnil && node.right.element.building_num <= highest) {
                System.out.print(",");
            }
        }
        if (node.element.building_num < highest) {
            printTree(node.right, lowest, highest);
        }
    }
    /*
    Name        : deleteKeyPair
    Parameters  : int building_num
    return type : void
    Description : finds the node having building_num and calls delete method
    */
    public void deleteKeyPair(int building_num) {
        RedBlackNode node = findNodebyId(building_num);
        deleteNode(node);
    }

    /*
    Name        : updateExecutionTime
    Parameters  : int building_num, int x
    return type : void
    Description : finds the node with building_num and updates it execution time.
    */
    public void updateExecutionTime(int building_num, int x) {
        RedBlackNode node = findNodebyId(building_num);
        node.element.execution_time = x;
    }


}

