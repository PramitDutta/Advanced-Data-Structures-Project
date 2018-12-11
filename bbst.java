import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class bbst {
	// class for the RB Tree nodes with attributes Colour, ID and Count
	static class TreeNode {
		String color;
		int ID;
		int count;
		TreeNode leftChild,rightChild,parent; //Nodes for parent, left and right child of a Node
	}	
	static int n; // global variable that stores the number of TreeNodes
	static TreeNode root; //global variable to store the root of the RB Tree
	static TreeNode sentinel; // global variable that basically points to Nil/NULL Tree Nodes.
	bbst(){ // constructor for bbst class
		sentinel = new TreeNode();
		sentinel.color="BLACK"; // sets colour of Nil/Null nodes to Black
		sentinel.ID=0; // sets ID of Nil/Null nodes to Zero
		sentinel.count=0; // sets count of Nil/Null nodes to Zero
		root=sentinel; // Sets the root to Nil/Null at the beginning
	} 
	// function that builds the tree from two sorted arrays(arr_ID contains the IDs and the arr_Count contains the Counts)
	static TreeNode sortedArrayToBST(int[] arr_ID, int[] arr_Count,int start, int end, TreeNode parent,int current_level, int max_level){
		if (start > end){
		    if(current_level == max_level) // considers the last level of the tree
		    		parent.color = "RED"; // colours the node in the last level as RED
		    	return sentinel;
		    }
		int mid = (start + end)/2; // calculates the middle element of the arrays
	    TreeNode n = new TreeNode();
	    n.ID = arr_ID[mid]; // Sets the ID of the Tree Node as the value of the Middle element from the array (arr_ID)
	    n.count=arr_Count[mid]; // Sets the Count of the Tree Node as the value of the Middle element from the array (arr_Count)
	    n.color = "BLACK"; // Sets colour of the Node as BLACK.
	    n.rightChild = sentinel; // Sets right child to Null
	    n.leftChild = sentinel; // Sets left child to Null

	    n.parent = parent;
	    // recursively builds the left subtree and right tree from the sorted arrays
	    n.leftChild =  sortedArrayToBST(arr_ID,arr_Count,start, mid-1,n,current_level+1,max_level);
	    n.rightChild = sortedArrayToBST(arr_ID,arr_Count,mid+1, end,n,current_level+1,max_level);

	    return n; // return the root of the BST
	}
	// function to insert new nodes in the RB Tree
	static TreeNode insert(TreeNode newNode){
		TreeNode a = root;
		TreeNode b = sentinel;
		while(a!=sentinel){
			b=a;
			// if ID of the node to be inserted is less than ID of the root go to the left subtree
			if(newNode.ID < a.ID){ 
				a=a.leftChild;
			}
			// if ID of the node to be inserted is greater than ID of the root go to the right subtree
			else if(newNode.ID > a.ID){
				a=a.rightChild;
			}
			else{ //sets the count of the root as the existing count plus the count of the new node to be inserted
				a.count+=newNode.count;
				//System.out.println("ID: "+a.ID+ " Count: "+a.count);
				return a;
		    }
		}
		newNode.parent=b; 
		if(b==sentinel){ // if the parent of the node inserted is Null then make the new node as the root
			root = newNode; 
		}
		else if(newNode.ID < b.ID){ // if ID of new Node less than the ID of its parent make the new Node its left child
			b.leftChild=newNode;
		}
		else{
			b.rightChild= newNode; // if ID of new Node less than the ID of its parent make the new Node its right child
		}
		newNode.leftChild=sentinel;
		newNode.rightChild=sentinel;
		newNode.color="RED"; 
		//System.out.println("ID: "+newNode.ID+ " Count: "+newNode.count);
		insert_Fix(newNode); // calling insert fix on the New Node to fix the Tree after the insertion of the new node
		return newNode;
	}
	// function to fix and balance the tree after an insert
	static void insert_Fix(TreeNode newNode){
		TreeNode a;
		while(newNode.parent.color.equals("RED")){
			if(newNode.parent==newNode.parent.parent.leftChild){
				a=newNode.parent.parent.rightChild;
				// Because both  new node and its parent are RED violation occurs.
				// We need to re-colour nodes and move pointers up the tree.
				if(a.color.equals("RED")){
					newNode.parent.color="BLACK";
					a.color="BLACK";
					newNode.parent.parent.color="RED";
					newNode=newNode.parent.parent;
				}
				else{//new node and its parent are both red but new nodes uncle is black 
					// since new node is the right child of its parent we perform a left rotation
					if(newNode==newNode.parent.rightChild){
						newNode=newNode.parent;
						LeftRotate(newNode);
					}// now new node is the left child of its parent so we need to re-colour and right rotate the tree.
					newNode.parent.color="BLACK";
					newNode.parent.parent.color="RED";
					RightRotate(newNode.parent.parent);
				}
			}
			else{// Same as before we left child replaced as right child and vice versa
				a=newNode.parent.parent.leftChild;
				if(a.color.equals("RED")){
					newNode.parent.color="BLACK";
					a.color="BLACK";
					newNode.parent.parent.color="RED";
					newNode=newNode.parent.parent;
				}
				else{
					if(newNode==newNode.parent.leftChild){
						newNode=newNode.parent;
						RightRotate(newNode);			
					}
				newNode.parent.color="BLACK";
				newNode.parent.parent.color="RED";
				LeftRotate(newNode.parent.parent);
				}
			}
		}
		root.color="BLACK"; // sets the colour of the root as Black after the Fix operation.
	}
	//Function to left rotate subtrees/tree
	static void LeftRotate(TreeNode node){
		TreeNode x= node.rightChild; //sets x
		node.rightChild=x.leftChild; // turn x's left subtree into node's right subtree
		if(x.leftChild!=sentinel){
			x.leftChild.parent=node;
		}
		x.parent=node.parent; // linking the node's  parent to x
		if(node.parent == sentinel){
			root = x;
		}
		else if(node == node.parent.leftChild){
			node.parent.leftChild=x;
		}
		else{
			node.parent.rightChild=x;
		}
		x.leftChild=node; // puts node on x's left
		node.parent=x;
	}
	// Function to right rotate subtrees/tree
	static void RightRotate(TreeNode node){
		TreeNode x= node.leftChild; //sets x
		node.leftChild=x.rightChild; // turn x's right subtree into node's left subtree
		if(x.rightChild!=sentinel){
			x.rightChild.parent=node;
		}
		x.parent=node.parent; // linking the node's  parent to x
		if(node.parent == sentinel){
			root = x;
		}
		else if(node == node.parent.rightChild){
			node.parent.rightChild=x;
		}
		else{
			node.parent.leftChild=x;
		}
		x.rightChild=node; // puts node on x's right
		node.parent=x;
	}
	// function to find minimum in a tree
	static TreeNode Minimum_Tree(TreeNode node){
		while(node.leftChild!=sentinel){
			node=node.leftChild; // following left child from root until we find a sentinel/NIL
		}
		return node;
	}
	// function to find maximum in a tree
	static TreeNode Maximum_Tree(TreeNode node){
		while(node.rightChild!=sentinel){
			node=node.rightChild; // following right child from root until we find a sentinel/NIL
		}
		return node;
	}
	// Function to find/search a node in a tree with a given ID
	static TreeNode find(int ID){
		TreeNode x = root;
		while(x!=sentinel && x.ID!=ID){
			if(ID < x.ID){ // if ID is in the left subtree
				x=x.leftChild;
			}
			else{// if ID is in the right subtree
				x=x.rightChild;
			}
		}
		return x;
	}
	// function to transplant in a tree between two nodes. replaces one subtree as a child of its parent with another subtree
	static void transPlant(TreeNode a, TreeNode b){
		// replace subtree rooted at node a with with subtree rooted at node b
		if(a.parent==sentinel){
			root = b;
		}
		else if(a==a.parent.leftChild){
			a.parent.leftChild=b;
		}
		else{
			a.parent.rightChild=b;
		}
		if(b!=null && a!=null){
			b.parent=a.parent; // b's parent becomes a's parent and vice versa
		}
		//b.parent=a.parent;
	}
	// function to delete a given tree node
	static void delete(TreeNode node){
		TreeNode a=node;
		TreeNode b=null;
		String a_orig_color = a.color;// stores the original colour of the node
		TreeNode p=node.parent; // stores the parent of the node to be deleted
		if(p!=null){ // checks for the boundary cases leftmost child and rightmost child in the tree
			if(node==p.leftChild && node.leftChild==sentinel && node.rightChild==sentinel){
				p.leftChild=null;
			}
			if(node==p.rightChild && node.leftChild==sentinel && node.rightChild==sentinel){
				p.rightChild=null;
			}
		}
		if(node.leftChild==sentinel){// case when left child is Nil
			b=node.rightChild;
			if(node.rightChild!=sentinel){
				transPlant(node,node.rightChild);
			}
			
		}
		else if(node.rightChild==sentinel){ // case when right child is Nil
			b=node.leftChild;
			if(node.leftChild!=sentinel){
				transPlant(node,node.leftChild);
			}
		}
		else{
			a=Minimum_Tree(node.rightChild);
			a_orig_color=a.color;
			b=a.rightChild;
			/*code here*/
			if(a.parent==node && b!=null){
				b.parent=a;
			}
			/*code here*/
			else if(a.rightChild!=null){
				transPlant(a,a.rightChild);
				a.rightChild=node.rightChild;
				a.rightChild.parent=a;
			}
			transPlant(node, a);
			a.leftChild=node.leftChild;
			a.leftChild.parent=a;
			a.color=node.color;
		}
		if(a_orig_color.equals("BLACK")){
			delete_Fix(b); // call delete fix to balance and restore the tree properties after delete operation
		}
		
	}
	// function to perform fixes in the tree to balance it and restore the properties
	static void delete_Fix(TreeNode node){
		TreeNode a = null;
		while(node!=root && node.color.equals("BLACK")){
			if(node==node.parent.leftChild){
				a=node.parent.rightChild;
				if(a.color.equals("RED")){
					//  when node's sibling a is Red
					a.color="BLACK";
					node.parent.color="RED"; // perform exchange of colours
					LeftRotate(node.parent); // perform Left rotate
					a=node.parent.rightChild;
				}
				// fixing case when node's sibling a is Black and both of a's children are black
				if(a.leftChild.color.equals("BLACK")&& a.rightChild.color.equals("BLACK")){
					// the extra black moves up the Tree  by coloring a as Red and sets node to point to its parent
					a.color="RED"; 
					node=node.parent;
				}
				else{
					// fixing case when node's sibling a is black , a's left child is Red and a's right child is black
					if(a.rightChild.color.equals("BLACK")){
						// colours of the nodes are exchanged and right rotation is performed
						a.leftChild.color = "BLACK";
						a.color = "RED";
						RightRotate(a);
						a=node.parent.rightChild;
					}
					// fixing case when node's sibling a is black and a's right child is red
					// removes the extra black by exchanging colours and performing left rotation
					a.color = node.parent.color;
					node.parent.color = "BLACK";
					a.rightChild.color = "BLACK";
					LeftRotate(node.parent);
					node=root;				
				}
			}
			else{ // same as the cases above with only left replaced by right and vice versa
				a=node.parent.leftChild;
				if(a.color.equals("RED")){
					a.color="BLACK";
					node.parent.color="RED";
					RightRotate(node.parent);
					a=node.parent.leftChild;
				}
				if(a.leftChild.color.equals("BLACK")&& a.rightChild.color.equals("BLACK")){
					a.color="RED";
					node=node.parent;
				}
				else{
					if(a.leftChild.color.equals("BLACK")){
						a.rightChild.color="BLACK";
						a.color="RED";
						LeftRotate(a);
						a=node.parent.leftChild;
					}
					a.color = node.parent.color;
					node.parent.color = "BLACK";
					a.leftChild.color = "BLACK";
					RightRotate(node.parent);
					node=root;
				}
			}
		}
		node.color="BLACK";
	}
	// function to traverse the tree in order
	static void inorderTraversal(TreeNode node){
		if(node == sentinel){ return; }
		inorderTraversal(node.leftChild);
		System.out.println(node.ID+" "+node.count+" "+node.color);
		inorderTraversal(node.rightChild);
	}
	// function to find the predecessor of a given node in a tree
	static TreeNode predecessor(TreeNode node){
		TreeNode a = null;
		// if left subtree of node is empty then predecessor is the rightmost node in the left subtree
		if(node.leftChild != sentinel){
			return Maximum_Tree(node.leftChild);
		}
		//if left subtree of node is empty and node has a predecessor  then the predecessor is the highest ancestor of x whose right child is also an ancestor of the node
		a = node.parent;
		while(a != sentinel && node == a.leftChild){
			node = a;
			a = a.parent;
		}
		return a;
	}
	// function to find the Successor of a given node in a tree
	static TreeNode successor(TreeNode node){
		TreeNode a = null;
		// if right subtree of node is empty then successor is the leftmost node in the right subtree
		if(node.rightChild != sentinel){
			return Minimum_Tree(node.rightChild);
		}
		//if right subtree of node is empty and node has a successor  then the successor is the lowest ancestor of x whose left child is also an ancestor of the node
		a = node.parent;
		while(a != sentinel && node == a.rightChild){
			node = a;
			a = a.parent;
		}
		return a;
	}
	// count of the event theID by m. If
	//theID is not present, insert it. Print the count
	//of theID after the addition.
	static TreeNode increase(int ID, int count){
		TreeNode n = find(ID);
		if(n!=sentinel){
			n.count+=count;
			//System.out.println("ID:" +n.ID +" Count:" +n.count);
			
		}
		if(n==null){
 
			TreeNode x=new TreeNode();
			x.ID=ID;
			x.count=count;
			TreeNode z=insert(x);
			return z;
		}
		return n;
		
		//else{
			//System.out.println("Node not found \n");
		//}
	}
	/*Decrease the count of theID by m. If theID’s
	count becomes less than or equal to 0, remove
	theID from the counter. Print the count of
	theID after the deletion, or 0 if theID is
	removed or not present. */
	static TreeNode reduce(int ID, int count){
		TreeNode n = find(ID);
		if(n!=sentinel){
			n.count-=count;
			//System.out.println("ID:" +n.ID +" Count:" +n.count);
			if(n.count<=0){
				delete(n);
				return sentinel;
			}
		}
		return n;
		//else{
			//System.out.println("Node not found \n");
		//}
	}
	/* Print the count of theID. If not present, print 0.*/
	static TreeNode count(int ID){
		TreeNode n = find(ID);
		//System.out.println(n.count);
		return n;
	}
	/* Print the ID and the count of the event with
	the lowest ID that is greater that theID. Print
	“0 0”, if there is no next ID.*/
	static TreeNode next(int ID){
		TreeNode a=root;
		TreeNode b=null;
		while(a!=sentinel){
			b=a;
			if(a.ID==ID){
				//System.out.println("ID:" +successor(a).ID+ " Count:"+successor(a).count);
				return successor(a);
			}
			else{
				if(ID < a.ID){
					a=a.leftChild;
				}
				else{
					a=a.rightChild;
				}
			}
		}
		if(b.ID > ID)
		{
			//System.out.println("ID:" +b.ID+ " Count:"+b.count);
			return b;
		}
		//System.out.println("ID:" +successor(b).ID+ " Count:"+successor(b).count);
		return successor(b);
	}
	/* Print the ID and the count of the event with
	the greatest key that is less that theID. Print
	“0 0”, if there is no previous ID.*/
	static TreeNode previous(int ID){
		TreeNode a=root;
		TreeNode b=null;
		while(a!=sentinel){
			b=a;
			if(a.ID==ID){
				//System.out.println("ID:" +predecessor(a).ID+ " Count:"+predecessor(a).count);
				return predecessor(a);
			}
			else{
				if(ID < a.ID){
					a=a.leftChild;
				}
				else{
					a=a.rightChild;
				}
			}
		}
		if(b.ID < ID)
		{
			//System.out.println("ID:" +b.ID+ " Count:"+b.count);
			return b;
		}
		// System.out.println("ID:" +predecessor(b).ID+ " Count:"+predecessor(b).count);
		return predecessor(b);
	}
	// helper function for the in range count calculation
	static int inRange_helper(TreeNode n, int ID1, int ID2){
		if(n==sentinel) return 0;		
		if(n.ID==ID1 && n.ID==ID2){
			return n.count;
		}
		if (n.ID <= ID2 && n.ID >= ID1){// traverse recursively to get the nodes that fall in range
	         return n.count + inRange_helper(n.leftChild,ID1,ID2) +
	                    inRange_helper(n.rightChild,ID1, ID2);}
		else if (n.ID < ID1)// if ID is less than the lower ID is less go to the right
	         return inRange_helper(n.rightChild,ID1,ID2);
		else // 
	    	 return inRange_helper(n.leftChild,ID1,ID2);
		
	}
	// function to count the summation of the event counts
	static void inRange(int ID1, int ID2){
		int sum=0;
		sum = inRange_helper(root, ID1, ID2);
		System.out.println(sum);
	}
	public static void main(String[] args){
		Scanner in;
		try {
            in = new Scanner(new File(args[0]));
			//in = new Scanner(new File("C:\\Users\\Hp\\workspace\\Practice\\src\\test_100.txt"));
		} catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }
		n = in.nextInt(); // variable to store the number of events
		int[] arr_ID =new int[n]; // stores the ID values in the arr_ID array
		int[] arr_Count =new int[n]; // stores the count values in the arr_Count array

		
		for(int i=0;i<n;i++){
			arr_ID[i]= in.nextInt();
			arr_Count[i]= in.nextInt();
		}
        in.close(); // close the scanner
		int h =(int) Math.ceil(Math.log(n+1)/Math.log(2));// calculates the height of the bbst
		root = sortedArrayToBST(arr_ID,arr_Count,0,n-1,root,0,h);
		/* Read Commands from Input */
		Scanner commands = new Scanner(System.in); //reads from system input
		String command = commands.next(); // read the command to run from the system input
		int argument1;  
		int argument2;
		while(command!="quit"){
			switch(command){
			case"increase":
				 argument1 = commands.nextInt();
				 argument2 = commands.nextInt();
				 TreeNode a =increase(argument1, argument2);
				 if(a!=null){
					 System.out.println(a.count);
				 }
				 break;
			case"reduce":
				 argument1 = commands.nextInt();
				 argument2 = commands.nextInt();
				TreeNode b = reduce(argument1, argument2);
				if(b!=null){
					 System.out.println(b.count);
				 }
				else{
					System.out.println("0");
				}
				break;
			case"count":
				 argument1 = commands.nextInt();
				 TreeNode c = count(argument1);
				 if(c!=null){
					 System.out.println(c.count);
				 }
				 else{
					 System.out.println("0");
				 }
				 break;
			case"inrange":
				 argument1 = commands.nextInt();
				 argument2 = commands.nextInt();
				 inRange(argument1,argument2);
				 break;
			case"next":
				 argument1 = commands.nextInt();
				 TreeNode nextNode = next(argument1);
				 if(nextNode!=null){
					 System.out.println(nextNode.ID +" "+nextNode.count);
				 }
				 else{
					 System.out.println("0 0");
				 }
				 //System.out.println(nextNode.id + " " + nextNode.count);
				 break;
			case"previous":
				 argument1 = commands.nextInt();
				 TreeNode previousNode = previous(argument1);
				 if(previousNode!=null){
					 System.out.println(previousNode.ID +" "+previousNode.count);
				 }
				 else{
					 System.out.println("0 0");
				 }
				 break;
			case"quit":
					return;
			default:
				System.out.println("invalid command");
			}
		if(command == "quit"){
			break;
		}
		 command = commands.next();
		}
		commands.close();		
	    }
	}