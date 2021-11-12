//state the package that this class belongs to
package ca.ucalgary.cpsc331;


public class RedBlackTree {

    public Node root;//pointer to the root node of the tree
    private Node TNULL;//pointer to a sentient null leaf node

    private String treeOutput;

    //assign constant numeric values for RED and BLACK
    private final int RED = 1;
    private final int BLACK = 0;

    //class that represents a structure of each node in the tree
    class Node{
        int key;//int value stored in the node
        int color;//store 1 if node is red and 0 if the node is black
        Node parent;//pointer to the parent node
        Node left;//pointer to the left child of current node
        Node right;//pointer to the right child of current node
    }

    //default constructor that initializes an empty tree
    public RedBlackTree(){
        this.TNULL = new Node();
        this.TNULL.color = BLACK;
        this.TNULL.left = null;
        this.TNULL.right = null;
        this.root = this.TNULL;
    }
    
    //This function returns true if the red black tree is empty, and false if the tree is not empty.
    public boolean empty(){
        if (this.root == TNULL)
            return true;
        else
            return false;
    }
    
    //This function inserts a new node with the passed key into the red black tree structure
    //and maintains the red-black properties.
    public void insert(int key){

        //pack the passed key into a new node.
        Node nodeZ = new Node();
        nodeZ.key = key;
        nodeZ.color = RED;//set new nodes to red initially
        nodeZ.parent = null;
        nodeZ.left = TNULL;
        nodeZ.right = TNULL;

        Node nodeY = null;
        Node nodeX = this.root;

        while (nodeX != TNULL){
            nodeY = nodeX;
            if (nodeZ.key < nodeX.key)
                nodeX = nodeX.left;
            else
                nodeX = nodeX.right;
        }

        nodeZ.parent = nodeY;
        if (nodeY == null)
            this.root = nodeZ;
        else if(nodeZ.key < nodeY.key)
            nodeY.left = nodeZ;
        else
            nodeY.right = nodeZ;
        
        //don't perform a fixup if the new node is a root node and color the new node black
        if(nodeZ.parent == null){
            nodeZ.color = BLACK;
            return;
        }

        //don't perform a fixup if the new node does not have a grand parent
        if (nodeZ.parent.parent == null)
            return;

        insertFixup(nodeZ);
    }

    //This function is a helper method that is used by the insert method to balance the tree after insertion
    private void insertFixup(Node nodeZ){
        Node nodeY;
        while (nodeZ.parent.color == RED){
            if (nodeZ.parent == nodeZ.parent.parent.left){
                nodeY = nodeZ.parent.parent.right;//uncle of nodeZ
                if (nodeY.color == RED){
                    nodeZ.parent.color = BLACK;//case 1
                    nodeY.color = BLACK;//case 1
                    nodeZ.parent.parent.color = RED;//case 1
                    nodeZ = nodeZ.parent.parent;//case 1
                }
                else{
                    if(nodeZ == nodeZ.parent.right){
                        nodeZ = nodeZ.parent;//case 2
                        leftRotate(nodeZ);//case 2
                    }
                    nodeZ.parent.color = BLACK;//case 3
                    nodeZ.parent.parent.color = RED;//case 3
                    rightRotate(nodeZ.parent.parent);//case 3          
                }
            }
            else{
                nodeY = nodeZ.parent.parent.left;//uncle of nodeZ
                if (nodeY.color == RED){
                    nodeZ.parent.color = BLACK;//mirrored case 1
                    nodeY.color = BLACK;//mirrored case 1
                    nodeZ.parent.parent.color = RED;//mirrored case 1
                    nodeZ = nodeZ.parent.parent;//mirrored case 1
                }
                else {
                    if(nodeZ == nodeZ.parent.left){
                        nodeZ = nodeZ.parent;//mirrored case 2
                        rightRotate(nodeZ);//mirrored case 2
                    }
                    nodeZ.parent.color = BLACK;//mirrored case 3
                    nodeZ.parent.parent.color = RED;//mirrored case 3
                    leftRotate(nodeZ.parent.parent);//mirrored case 3          
                }
            }
            
            if (nodeZ == this.root)
                break;
        }
        this.root.color = BLACK;
    }

    //this function performs the left rotate operation on nodeX
    private void leftRotate(Node nodeX){

        Node nodeY = nodeX.right; //initialize nodeY
        nodeX.right = nodeY.left; //turn nodeY's left subtree into nodeX's right subtree

        if (nodeY.left != TNULL)
            nodeY.left.parent = nodeX;
        nodeY.parent = nodeX.parent;//link nodeX's parent to nodeY

        if (nodeX.parent == null)
            this.root = nodeY;
        else if(nodeX == nodeX.parent.left)
            nodeX.parent.left = nodeY;
        else 
            nodeX.parent.right = nodeY;

        nodeY.left = nodeX;
        nodeX.parent = nodeY;
    }

    //this function performs the right rotate operation on nodeX, it is symmetric of the leftRotate method
    private void rightRotate(Node nodeX){

        Node nodeY = nodeX.left;//initialize nodeY
        nodeX.left = nodeY.right;//turn nodeY's right subtree into nodeX's left subtree

        if (nodeY.right != TNULL)
            nodeY.right.parent = nodeX;
        nodeY.parent = nodeX.parent;//link nodeX's parent to nodeY

        if (nodeX.parent == null)
            this.root = nodeY;

        else if (nodeX == nodeX.parent.right)
            nodeX.parent.right = nodeY;

        else
            nodeX.parent.left = nodeY;

        nodeY.right = nodeX;
        nodeX.parent = nodeY;
    }

    //This method performs the transplant operation in a black-red tree
    private void rbTransplant(Node u, Node v){

        if (u.parent == null)
            this.root = v;
        else if(u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;

        v.parent = u.parent;
    }

    //This method restores properties of the passed nodeX after a delete operation is performed
    private void rbDeleteFixup(Node nodeX){

        Node nodeW;
      
        while(nodeX != this.root && nodeX.color == BLACK){
            if (nodeX == nodeX.parent.left){
                nodeW = nodeX.parent.right;//sibling of nodeX
                if (nodeW.color == RED){
                    nodeW.color = BLACK;
                    nodeX.parent.color = RED;
                    leftRotate(nodeX.parent);
                    nodeW = nodeX.parent.right;
                }
                if (nodeW.left.color == BLACK && nodeW.right.color == BLACK){
                    nodeW.color = RED;
                    nodeX = nodeX.parent;
                }
                else{
                    if(nodeW.right.color == BLACK){
                        nodeW.left.color = BLACK;
                        nodeW.color = RED;
                        rightRotate(nodeW);
                        nodeW = nodeX.parent.right;
                    }
                    nodeW.color = nodeX.parent.color;
                    nodeX.parent.color = BLACK;
                    nodeW.right.color = BLACK;
                    leftRotate(nodeX.parent);
                    nodeX = this.root;                    
                }
            }
            else{
                nodeW = nodeX.parent.left;//sibling of nodeX
                if (nodeW.color == RED){
                    nodeW.color = BLACK;
                    nodeX.parent.color = RED;
                    rightRotate(nodeX.parent);
                    nodeW = nodeX.parent.left;
                }
                if (nodeW.right.color == BLACK && nodeW.left.color == BLACK){
                    nodeW.color = RED;
                    nodeX = nodeX.parent;
                }
                else{
                    if(nodeW.left.color == BLACK){
                        nodeW.right.color = BLACK;
                        nodeW.color = RED;
                        leftRotate(nodeW);
                        nodeW = nodeX.parent.left;
                    }
                    nodeW.color = nodeX.parent.color;
                    nodeX.parent.color = BLACK;
                    nodeW.left.color = BLACK;
                    rightRotate(nodeX.parent);
                    nodeX = this.root;                    
                }
            }
        }
        nodeX.color = BLACK;
    }

    //This function deletes a node with the passed key from the red black tree structure
    //and maintains the red-black properties.
    public void delete(int key){

        //raise an appropriate run-time exception when the user attempts to delete from am empty tree
        if (this.root == null)
            throw new RuntimeException("The tree is empty, there are no nodes to delete");

        //find the node that contains the passed key
        Node searchNode = this.root;
        Node nodeZ = TNULL;
        while (searchNode != TNULL){
            if (searchNode.key == key)
                nodeZ = searchNode;
            if (searchNode.key <= key)
                searchNode = searchNode.right;
            else
                searchNode = searchNode.left;
        }
        if (nodeZ == TNULL)
            throw new RuntimeException("The node requested for deletion is not present.");

        Node nodeX = new Node();
        Node nodeY = nodeZ;
        int originalColorOfNodeY = nodeY.color;

        if(nodeZ.left == TNULL){
            nodeX = nodeZ.right;
            rbTransplant(nodeZ, nodeZ.right);
        }
        else if(nodeZ.right == TNULL){
            nodeX = nodeZ.left;
            rbTransplant(nodeZ, nodeZ.left);
        }
        else{
            nodeY = treeMinimum(nodeZ.right);
            originalColorOfNodeY = nodeY.color;
            nodeX = nodeY.right;
            if (nodeY.parent == nodeZ)
                nodeX.parent = nodeY;
            else{
                rbTransplant(nodeY, nodeY.right);
                nodeY.right = nodeZ.right;
                nodeY.right.parent = nodeY;
            }
            rbTransplant(nodeZ, nodeY);
            nodeY.left = nodeZ.left;
            nodeY.left.parent = nodeY;
            nodeY.color = nodeZ.color;
        }
        if(originalColorOfNodeY == BLACK)
            rbDeleteFixup(nodeX);
    }

    //This method finds the maximum value in a tree with root pointing to startingNode
    private Node treeMinimum(Node startingNode){
        
        Node searchNode = startingNode;//initialize the search at the specified node
        
        while(searchNode.left != TNULL)//keep traversing to the leftest node until an outer leaf is encountered
            searchNode = searchNode.left;
        
        return searchNode;//return the last encountered node before null node was found
    }

    //This function returns true if a node with the passed key exists in the red-black tree
    //and returns false otherwise.
    public boolean member(int key){

        boolean isMemberPresent = false;
    
        //set the starting search node to the root ot the tree
        Node searchNode = this.root;

        while (searchNode != TNULL){
            if (searchNode.key == key){
                isMemberPresent = true;
                break;//stop searching down the tree once the first instance is encountered
            }
            if (searchNode.key <= key)
                searchNode = searchNode.right;
            else
                searchNode = searchNode.left;
        }

        return isMemberPresent;
    }
    
    //perform a pre-order tree walk
    public void preorderTreeWalk(Node startingNode, String address)
    {
        Node searchNode = startingNode;

        if (searchNode.parent != null && searchNode == searchNode.parent.left)
            address = address + "L";
        if (searchNode.parent != null && searchNode == searchNode.parent.right)
            address = address + "R";

        if (searchNode != TNULL){
            String color = null;
            if (searchNode.color == 0)
                color = "black";
            if (searchNode.color == 1)
                color = "red";
            //System.out.print("address: "+ address +" color: "+color+ " key: "+searchNode.key+"\n");
            treeOutput = treeOutput+address +":"+color+ ":"+searchNode.key+"\n";
            preorderTreeWalk(searchNode.left, address);
            preorderTreeWalk(searchNode.right, address);
        }
    }

    //This method returns a string with information about each node in this red-black tree as specified in the problem statement.
    public String toString(){
        
        treeOutput = "";//intialize an empty string
        //do a preorder tree walk and print information about every encountered internal node
        if(this.root !=TNULL)
            preorderTreeWalk(this.root, "*");
        else
            treeOutput = "";

        return treeOutput;
    }

}






