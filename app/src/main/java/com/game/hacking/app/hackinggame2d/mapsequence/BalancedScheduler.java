package com.game.hacking.app.hackinggame2d.mapsequence;

// For the pretty string.
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Red-Black Tree, the data structure that works well when it comes to situations
 * involving real time calculations and storage, presents itself as an abstract
 * data type that has the complexity and performance that can match to that of
 * a normal binary tree data structure. Given that the tree itself holds the
 * binary tree property, it also holds five extra invariants which are used to
 * maintain a time complexity of O(log n) run time. As a need to use this data
 * structure for various reasons that may include processor scheduling,
 * computational geometry, and various other time precious applications, these
 * structures are among the absolute useful and most ingenious designs in
 * Computer Science.
 * @param <K> The Key used for comparison and a way that corresponds to the
 * data that will be paired with it. We use this variable as a mechanism for
 * figuring out where the data must be stored in order to maintain the 5 invariants
 * as well as the binary search tree property.
 * @param <V> The value that will correspond with the key paired with it. Value
 * is the data that will be stored in the data structure.
 * @author MAGarcia
 */
public class BalancedScheduler<K extends Comparable<K>, V> implements Tree<K,V>,
        Serializable
{
  private static final long serialVersionUID = 1L;
  /**************************************************************************
   * Variable declarations.
   **************************************************************************/

  /**
   * Color RED or BLACK determining what the node color is.
   */
  public static enum COLOR { RED, BLACK };


  /**
   * The node class used for holding the key and value, as well as the
   * color, of the data which corresponds to the Red-Black Tree. This class
   * must be self referential in order to keep track of children, as well as
   * its parent.
   */
  private class Node implements Serializable
  {
    private static final long serialVersionUID = 1L;
    /**
     * The key used for comparisons within the Red-Black Tree.
     */
    protected K key;
    /**
     * The data that is to be held in the Red-Black Tree.
     */
    protected V value;
    /**
     * The color of the node, which has to be either red or black.
     */
    protected COLOR color;
    /**
     * The node's right child, which points to another node, or null value.
     */
    protected Node rightChild;
    /**
     * The node's left child, which points to another node, or null value.
     */
    protected Node leftChild;
    /**
     * The node's parent, with points to another node, most notably its
     * parent, or null value if it is root.
     */
    protected Node parent;
  }

  /**
   * The root node, which stems to the top of the tree.
   */
  private Node root;
  /**
   * Number of nodes currently present in the Red-Black Tree.
   */
  private int nodes;

  /**
   * The constructor for the Red-Black Tree. This method will be used to
   * set nodes to 0, since we current start with no nodes.
   */
  public BalancedScheduler()
  {
    root = null;
    nodes = 0;
  }

  /**
   * Adds the key and value into the tree through means of maintaining the
   * Red-Black Tree Property. In order for this method to maintain the property,
   * the inserted node is verified to satisfy the five invariants through
   * means of recursion, by which these methods used to fix the Red-Black
   * Tree will check if any invariants broken must be fixed.
   * @param key The key used to store the value in the Red-Black Tree.
   * @param value The data that is to be stored into the Red-Black Tree.
   */
  @Override
  public void add(K key, V value)
  {
    // check if key is null before proceeding.
    if(key == null)
      return;
    // Create the node to add into the Red-Black Tree.
    Node node = new Node();
    node.key = key;
    node.value = value;
    node.color = COLOR.RED;

    // Base case: Check if root is null, which means no nodes are currently
    // stored.
    if(root == null)
    {
      root = node;
    }
    else
    {
      // Find the node spot in which the added node will go in.
      Node currentNode = findAdderNode(root, key);
      // Set nodes parent to the currentn node.
      node.parent = currentNode;
      // check if node's parent' key is greater than or equal to node key.
      if(currentNode.key.compareTo(key) >= 0)
        currentNode.leftChild = node;
      else
        currentNode.rightChild = node;

    }
    // increment nodes.
    nodes++;
    // start calling to check Red-Black Tree invariants.
    fixTree(node);
  }

  /**
   * Fix tree is the first method used to determine if there is any need
   * to fix the tree, starting with determining if the node added has a null
   * parent. If so, then the node is actually at the root, and therefore must
   * by default be painted black. If there is in fact parent for node, then we
   * move on to the second case.
   * @param node The node recently added.
   */
  private void fixTree(Node node)
  {
    if(node.parent == null)
      node.color = COLOR.BLACK;
    else
      addCaseZero(node);
  }

  /**
   * First case, in which as a fact there is indeed a parent for the node,
   * checks if the node's parent is black. If not, then the tree is guaranteed
   * to be broken since invariant 3 states that red nodes must have black
   * children. To remedy this, the method calls on determining if the uncle is
   * also red, by which must be determined in the next case. If the parent
   * color is black though, then no invariant was broken and the tree is fixed.
   * @param node The recently added node.
   */
  private void addCaseZero(Node node)
  {
    if(node.parent.color != COLOR.BLACK)
      addCaseOne(node);
  }

  /**
   * Check if the uncle of the added node is present, and if that uncle node
   * is red. If so, perform repainting of the parent node to black, as well as
   * repainting the uncle node black, and finally repainting the grandparent
   * node to red. After this is done, then we move up the tree with the
   * grandparent node, to verify that this procedure had not broken any more
   * invariants. If the uncle is black, or if uncle does not exist, we move on
   * to the next case.
   * @param node The recently added node.
   */
  private void addCaseOne(Node node)
  {
    // Grab our uncle.
    Node uncleNode = getUncle(node);
    // Do not reference grandparent of node yet until we know uncle is
    // not null
    Node grandParentNode;

    // Check if uncle is not null and if uncle is red.
    if( (uncleNode != null) && (uncleNode.color == COLOR.RED ))
    {
      // paint node's parent black.
      node.parent.color = COLOR.BLACK;
      // paint uncle black.
      uncleNode.color = COLOR.BLACK;
      // get grandparent and color red to maintain invariant 4.
      grandParentNode = getGrandParent(node);
      grandParentNode.color = COLOR.RED;
      fixTree(grandParentNode);
    }
    else
      addCaseTwo(node);
  }

  /**
   * Rotation of the subtree within the Red-Black Tree, by which we agree
   * that uncle is indeed black, or if there is any uncle at all, this pretty
   * much states that a null uncle is a black uncle node. To remedy this case,
   * we go about determining where the added node, which is an internal node,
   * and we rotate about the parent node based on whether the node is on the
   * left or right of its parent. Once the rotation is done, node is
   * reassigned to its right or left child, since node is now the parent, to
   * the old parent node, and this node is taken to the next case.
   * @param node The recently added node.
   */
  private void addCaseTwo(Node node)
  {
    // Get Grandparent node.
    Node grandParentNode = getGrandParent(node);

    // check if node is the right child of parent, since we must know where
    // node is in order to properly rotate the subtree.
    if((node == node.parent.rightChild) &&
            (node.parent == grandParentNode.leftChild))
    {
      rotateLeft(node.parent);
      // reference node's left child now, since we are now setting up for
      // case 3.
      node = node.leftChild;
    }
    else if((node == node.parent.leftChild) &&
            (node.parent == grandParentNode.rightChild))
    {
      rotateRight(node.parent);
      // reference node's right child now since we are now setting up for
      // case 3.
      node = node.rightChild;
    }

    addCaseThree(node);
  }

  /**
   * Final case in order to fix the Red-Black Tree properties. since rotation
   * has been accomplished at this point, we must now repaint the old parent
   * node's parent, which was the once child node, to black, and the
   * grandparent to red. Since at this point our node is external, and our
   * uncle remains to be black, we must perform a rotation on the grandparent
   * node, which by this point the tree is fixed.
   * @param node The node which was once a parent and is now a child of the
   * recently added node.
   */
  private void addCaseThree(Node node)
  {
    Node grandParentNode = getGrandParent(node);

    node.parent.color = COLOR.BLACK;
    grandParentNode.color = COLOR.RED;

    if(node == node.parent.leftChild)
      rotateRight(grandParentNode);
    else
      rotateLeft(grandParentNode);
  }

  /**
   * Rotates the specified node to the right, making its right child the new
   * parent of the node. The once right child of node then points to its
   * grandparent as its new parent, for node itself, its parent becomes the
   * grandparent.
   * @param node The node to be rotated.
   */
  private void rotateRight(Node node)
  {
    // Set a temporary node that references left child.
    Node left = node.leftChild;
    // replace the node with left, which means left is now parent.
    replaceParent(node, left);
    node.leftChild = left.rightChild;

    if(left.rightChild != null)
      left.rightChild.parent = node;

    left.rightChild = node;
    node.parent = left;
  }

  /**
   * Rotates the specified node to the left, making its left child the new
   * parent of the node. The once left child of node then points to its
   * grandparent as its new parent, for node itself, its parent becomes the
   * grandparent.
   * @param node The node to be rotated.
   */
  private void rotateLeft(Node node)
  {
    Node right = node.rightChild;
    replaceParent(node, right);
    node.rightChild = right.leftChild;

    if(right.leftChild != null)
      right.leftChild.parent = node;

    right.leftChild = node;
    node.parent = right;
  }

  /**
   * Replaces the position of the node with it's new parent node. The once
   * old parent node is replaced by the new parent node, which happens to be
   * either the left or right child of the old parent node.
   * @param oldParentNode The old parent node to be replaced.
   * @param newParentNode The new parent node to take the position of the
   * old parent node.
   */
  private void replaceParent(Node oldParentNode, Node newParentNode)
  {
    // check if the node we are replacing is the root.
    if(oldParentNode.parent == null)
      root = newParentNode;
    else
    {
      if(oldParentNode == oldParentNode.parent.leftChild)
        oldParentNode.parent.leftChild = newParentNode;
      else
        oldParentNode.parent.rightChild = newParentNode;
    }
    // check if the new parent node is null. If so, do not set its parent
    // to the old parent's parent node. This likely means we are removing
    // the old node and replacing it with null.
    if(newParentNode != null)
      newParentNode.parent = oldParentNode.parent;
  }

  /**
   * Locates the node which performs traversing through the tree in a normal
   * binary search tree way. Once the node reaches null, the previous node
   * is returned and the node to be added is to be assigned to either
   * the left or right child of the previous node.
   * @param initNode The starting node for the traversal of the tree.
   * @param key The key used to compare each node, by which determines if
   * the node being compared is greater or less than or equal to the key.
   * @return The previous node after traversal.
   */
  private Node findAdderNode(Node initNode, K key)
  {
    Node prevNode = null;

    while( initNode != null )
    {
      prevNode = initNode;

      if( initNode.key.compareTo(key) >= 0 )
        initNode = initNode.leftChild;
      else
        initNode = initNode.rightChild;
    }

    return prevNode;
  }

  /**
   * Removes the node in the Red-Black Tree. Such cases are similar to the
   * binary search tree, only with three cases, if the removal node contains
   * no nodes, one node, or is full. In this case, whether the node has no
   * one, or two children, we take this into consideration and make the
   * assumption that the node has two children nodes regardless, as we can
   * perform both binary tree cases 1, 2, and 3 together, sort of like killing
   * two birds with one stone. Since null nodes are considered black by
   * default, we can make the assumption that null nodes exist and are colored
   * black.
   * @param key The key used to search and remove the node.
   * @return The value of the removed node.
   */
  @Override
  public V remove(K key)
  {
    Node remNode = nodeLookUp(key);
    V result;

    if(remNode == null)
      result = null;
    else
    {
      result = remNode.value;
      // Case 3: If node has both children!!
      if(isFull(remNode))
      {
        // Grab and replace the removed node with predecessor
        // Invariants are not broken as color is the same
        // when replacing the removed node with predecessor.
        Node predecessor = getInOrderPredecessor(remNode);
        remNode.key = predecessor.key;
        remNode.value = predecessor.value;
        // Successor node now becomes our node to remove.
        remNode = predecessor;
      }

      // Case 1 and 2: If node has at most 1 child!!
      if(remNode.leftChild == null || remNode.rightChild == null)
      {
        // Assume null children are empty black leaf nodes.
        Node child = ((remNode.rightChild == null) ? remNode.leftChild :
                remNode.rightChild);
        // Check if node to be removed is black, then already invariant
        // 5 is going to be broken once we removed this node.
        if(remNode.color == COLOR.BLACK)
        {
          // Change color to that of the child and proceed to first
          // remove case checkpoint.
          remNode.color = getNodeColor(child);
          rBRemoveCase1(remNode);
        }
        // Since child is null, we set remNodes parent pointer to null.
        replaceParent(remNode, child);
        nodes--;
      }
    }

    if(root != null)
      root.color = COLOR.BLACK;

    return result;
  }

  /**
   * Grabs the color of the node. If the node is null though, then we must
   * assume the null is already black, and so must return it.
   * @param node The node used to check for the color
   * @return The color of the node, default black if node is null.
   */
  private COLOR getNodeColor(Node node)
  {
    return (node == null) ? COLOR.BLACK : node.color;
  }

  /**
   * Checks if the the node's sibling is red, which means that the Red-Black
   * Tree's invariant 5 is broken, therefore we must repaint the replaced
   * node's parent red, and its sibling black. When this is done, we must
   * rotate the node's parent based on whether the node is on the left or
   * right of the parent. Once we have accomplished this, we move on to the
   * next case to check for colors.
   * @param node The recently replaced node, which was the node that replaced
   * the removed node.
   */
  private void rBRemoveCase1(Node node)
  {
    if(node.parent != null)
    {
      // Grab our sibling from the node.
      Node siblingNode = getSibling(node);
      // Check if the color of sibling is red.
      if(getNodeColor(siblingNode) == COLOR.RED)
      {
        // paint node parent red and sibling black, since the node we are
        // using is black.
        node.parent.color = COLOR.RED;
        siblingNode.color = COLOR.BLACK;
        // rotate about the node parent.
        if(node == node.parent.leftChild)
          rotateLeft(node.parent);
        else
          rotateRight(node.parent);
      }
      rBRemoveCase2(node);
    }
  }

  /**
   * Checks if sibling node is black and so is parent, not to mention sibling
   * node's children are black as well, which must mean that invariant 5 is
   * still broken. In order to remedy this, we must paint the sibling red and
   * recursively call case 1. Node's parent, sibling, and sibling's children,
   * must be black to ensure this works, otherwise we proceed to the next
   * case.
   * @param node The node that has recently replaced the removed node.
   */
  private void rBRemoveCase2(Node node)
  {
    // Grab our sibling.
    Node siblingNode = getSibling(node);
    // check if node parent is black, sibling node is black, sibling left
    // child is black, and if sibling right child is black
    if(getNodeColor(node.parent) == COLOR.BLACK &&
            getNodeColor(siblingNode) == COLOR.BLACK &&
            getNodeColor(siblingNode.leftChild) == COLOR.BLACK &&
            getNodeColor(siblingNode.rightChild) == COLOR.BLACK)
    {
      // paint sibling red.
      siblingNode.color = COLOR.RED;
      // call node parent recursively and check invariants again.
      rBRemoveCase1(node.parent);
    }
    else
      rBRemoveCase3(node);
  }

  /**
   * Checks if node's parent is red, and the sibling and sibling's children
   * are all black. If so, we go about remedying this by repainting the
   * sibling node red, and the node's parent black. If this is not the case,
   * we move on to the next case.
   * @param node The node that has recently replaced the removed node.
   */
  private void rBRemoveCase3(Node node)
  {
    // Grab our sibling.
    Node siblingNode = getSibling(node);
    // If parent node is red, sibling node is black, sibling left child is
    // is black, and if sibling node is black.
    if(getNodeColor(node.parent) == COLOR.RED &&
            getNodeColor(siblingNode) == COLOR.BLACK &&
            getNodeColor(siblingNode.leftChild) == COLOR.BLACK &&
            getNodeColor(siblingNode.rightChild) == COLOR.BLACK)
    {
      // paint sibling red and parent black.
      siblingNode.color = COLOR.RED;
      node.parent.color = COLOR.BLACK;
    }
    else
      rBRemoveCase4(node);
  }

  /**
   * Checks where the node is located, based on which child is it for its
   * parent, then goes about determining if the the sibling node and one of
   * its children are black, and one of its children are red. If so, we must
   * repaint the sibling node red, and its red child black. Once doing so, we
   * perform a rotation based on which child the sibling node is on the parent.
   * After this is performed, we go about the next case for final fixing.
   * @param node The node that has recently replaced the removed node.
   */
  private void rBRemoveCase4(Node node)
  {
    Node siblingNode = getSibling(node);

    //If node is the left child of its parent,
    // sibling is black but left child is red and right child is black.
    if(node == node.parent.leftChild &&
            getNodeColor(siblingNode) == COLOR.BLACK &&
            getNodeColor(siblingNode.leftChild) == COLOR.RED &&
            getNodeColor(siblingNode.rightChild) == COLOR.BLACK)
    {
      // repaint sibling red and left child black in order to maintain
      // invariant 4, then rotate since sibling is external, left child
      // is internal.
      siblingNode.color = COLOR.RED;
      siblingNode.leftChild.color = COLOR.BLACK;
      rotateRight(siblingNode);
    }
    // If node is right child of parent and sibling is black with red right
    // child and black left child.
    else if(node == node.parent.rightChild &&
            getNodeColor(siblingNode) == COLOR.BLACK &&
            getNodeColor(siblingNode.rightChild) == COLOR.RED &&
            getNodeColor(siblingNode.leftChild) == COLOR.BLACK)
    {
      // paint sibling red and right child black, then rotate left since
      // sibling is external, right child is internal.
      siblingNode.color = COLOR.RED;
      siblingNode.rightChild.color = COLOR.BLACK;
      rotateLeft(siblingNode);
    }
    // proceed to fifth case, since we've only rotated the sibling subtree.
    // We must now work with the node's parent.
    rBRemoveCase5(node);
  }

  /**
   * Repaints the sibling node to the color of the node's parent, and repaints
   * the node's parent to black, then repaints one of the children of the
   * sibling node black if the child is red, as this corresponds oppositely to
   * which side the node is located at. Since we have only rotated about the
   * sibling's subtree, one more rotation is done to the parent in order to
   * complete the tree fix.
   * @param node The node that has recently replaced the removed node.
   */
  private void rBRemoveCase5(Node node)
  {
    Node siblingNode = getSibling(node);
    // No idea what parent color may be, so we color the sibling with it,
    // and color the parent black, in case we break invariant 4.
    siblingNode.color = node.parent.color;
    node.parent.color = COLOR.BLACK;

    // Node is the left child of its parent.
    if(node == node.parent.leftChild)
    {
      // check sibling's right child if red
      if(getNodeColor(siblingNode.rightChild) == COLOR.RED)
      {
        // paint sibling's right child black.
        siblingNode.rightChild.color = COLOR.BLACK;
        // rotate node's parent to the left.
        rotateLeft(node.parent);
      }
    }
    else
    {
      // check sibling's left child is red
      if(getNodeColor(siblingNode.leftChild) == COLOR.RED)
      {
        // paint sibling's left child black.
        siblingNode.leftChild.color = COLOR.BLACK;
        // rotate node's parent to the right.
        rotateRight(node.parent);
      }
    }
  }

  /**
   * Check if the node has a right and left child.
   * @param node The node which is to be check for fullness.
   * @return Tree if the node is full, false otherwise.
   */
  private boolean isFull(Node node)
  {
    return node.leftChild != null && node.rightChild != null;
  }

  /**
   * Grabs the in order predecessor of the Red-Black Tree.
   * @param remNode The node to find the in order predecessor.
   * @return The right most node of the left subtree of remNode.
   */
  private Node getInOrderPredecessor(Node remNode)
  {
    Node currentNode = remNode.leftChild;

    while( currentNode.rightChild != null )
      currentNode = currentNode.rightChild;

    return currentNode;
  }

//    private boolean childLess(Node node)
//    {
//        return node.leftChild == null && node.rightChild == null;
//    }

  /**
   * Looks up the node for use or to extract the value of the node.
   * @param key The key used to find the node in the Red-Black tree.
   * @return The node that corresponds to the key.
   */
  private Node nodeLookUp(K key)
  {
    Node currentNode = root;

    while( currentNode != null && currentNode.key.compareTo(key) != 0)
    {
      if( currentNode.key.compareTo(key) < 0)
        currentNode = currentNode.rightChild;
      else
        currentNode = currentNode.leftChild;
    }

    return currentNode;
  }

  /**
   * Looks up the value of a node by the key. If no value is found that
   * corresponds to the key, we must simply return null.
   * @param key The key used to find the value in the Red-Black Tree.
   * @return The value that corresponds to the key.
   */
  @Override
  public V lookup(K key)
  {
    V result = null;

    if( nodes > 0 )
      result = nodeLookUp(key).value;

    return result;
  }

  /**
   * Finds and returns the grandparent of the node. If no grandparent is found
   * or if the node or parent is null, we simply return null.
   * @param node The node used to find the grandparent.
   * @return The grandparent of the node.
   */
  private Node getGrandParent(Node node)
  {
    if( (node != null) && (node.parent != null))
      return node.parent.parent;
    else
      return null;
  }

  /**
   * Finds and returns the uncle of the node. This uncle is the sibling of the
   * node's parent.
   * @param node The node used to find the uncle of it.
   * @return The uncle node if found, otherwise null.
   */
  private Node getUncle(Node node)
  {
    // Get off me lawn ye darn kids!!
    Node granPappyNode = getGrandParent(node);

    if(granPappyNode == null)
      return null;
    else
    {
      if(node.parent == granPappyNode.leftChild)
        return granPappyNode.rightChild;
      else
        return granPappyNode.leftChild;
    }
  }

  /**
   * Finds and returns the sibling of the node, which is the other child of
   * the node's parent.
   * @param node The node used to find the sibling.
   * @return The sibling of the node, otherwise null if node has no sibling.
   */
  private Node getSibling(Node node)
  {
    if(node == node.parent.leftChild)
      return node.parent.rightChild;
    else
      return node.parent.leftChild;
  }

  /**
   * Returns a pyramid structured string diagram of the Red-Black Tree.
   * @return The Red-Black Tree in a pyramid fashion.
   */
  @Override
  public String toPrettyString()
  {
    String treeString = "";
    int items = 0;
    int levelLimit = 1;

    if(nodes > 0)
    {
      LinkedList<Node> queue = new LinkedList<>();
      queue.add(root);

      while(!queue.isEmpty())
      {
        Node traverse = queue.removeFirst();
        items++;
        if(items >= levelLimit)
        {
          treeString += "\n";
          levelLimit *= 2;

        }

        if(traverse != null)
        {
          queue.add(traverse.leftChild);
          queue.add(traverse.rightChild);
          treeString += "[" + traverse.value + " "
                  + traverse.color.name() + "] ";
        }
        else
          treeString += "[NILL BLACK] ";
      }
    }
    return treeString;
  }
}
