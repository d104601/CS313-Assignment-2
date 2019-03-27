public class BinaryExpressionTree {
    private static final String DIGITS = "0123456789";
    private static final String OPERATORS = "+-/*";
    private BinaryExpressionTree() {} //to prevent initialization

    /**
     * Transforms a fully parenthesized infix expression into a postfix expression.
     * Only integers, whitespace, and the operators *, /, +, - are permitted.
     * May not detect that some inputs are invalid (and return equivalent nonsense).
     * @param infix a fully parenthesized arithmetic expression in infix notation
     * @return postfix expression
     */
    public static String postfixFromInfix(String infix) throws IllegalArgumentException {
        String postfix = "";
        Stack<Character> charStack = new LinkedStack<>();
        for (int i = 0; i < infix.length(); i++) {
            if (infix.charAt(i) == '(') charStack.push('(');
            else if (infix.charAt(i) == ')')
            {
                while(charStack.top() != '(')
                    postfix = postfix + charStack.pop() + ' ';
                charStack.pop(); //pop off the '('
            }
            else if(OPERATORS.indexOf(infix.charAt(i)) != -1)
            {
                charStack.push(infix.charAt(i));
            }
            else if(DIGITS.indexOf(infix.charAt(i)) != -1)
            {
                postfix = postfix + infix.charAt(i);
                if(DIGITS.indexOf(infix.charAt(i+1)) == -1)
                    postfix = postfix + " "; //add a space to separate integers
            }
            else if(infix.charAt(i)==' ') { } //allow and ignore whitespace in our expression
            else throw new IllegalArgumentException("Invalid character in infix expression");
        }
        if(!charStack.isEmpty()) throw new IllegalArgumentException("Invalid infix expression");
        return postfix;
    }

    /**
     * Constructs a LinkedBinaryTree from a postfix expression
     * @param postfix a valid postfix expression
     * @return a LinkedBinaryTree representing the expression
     */
    public static LinkedBinaryTree<String> constructExpressionTreeFromPostfix(String postfix)
            throws IllegalArgumentException
    {
        Stack<LinkedBinaryTree<String>> treeStack = new LinkedStack<>();
        for(int i=0; i<postfix.length(); i++)
        {
            if(DIGITS.indexOf(postfix.charAt(i)) != -1) {
                String temp = "";
                while (DIGITS.indexOf(postfix.charAt(i)) != -1) {
                    temp = temp + postfix.charAt(i);
                    i++;
                }
                LinkedBinaryTree<String> curr = new LinkedBinaryTree<>();
                curr.addRoot(temp);
                treeStack.push(curr);
            }
            else if(postfix.charAt(i) == ' ') {} //do nothing for whitespace
            else if(OPERATORS.indexOf(postfix.charAt(i)) != -1)
            {
                String operator = String.valueOf(postfix.charAt(i));
                LinkedBinaryTree<String> curr = new LinkedBinaryTree<>();
                curr.addRoot(operator);
                LinkedBinaryTree<String> treeRight = treeStack.pop();
                LinkedBinaryTree<String> treeLeft = treeStack.pop();
                curr.attach(curr.root(), treeLeft, treeRight);
                treeStack.push(curr);
            }
            else throw new IllegalArgumentException("Invalid postfix expression");
        }
        return treeStack.pop();
    }

    /**
     * Constructs a LinkedBinaryTree from a infix expression
     * @param infix a valid infix expression
     * @return a LinkedBinaryTree representing the expression
     */
    public static LinkedBinaryTree<String> constructExpressionTree(String infix)
        throws IllegalArgumentException
    {
        String postfix = postfixFromInfix(infix);
        return constructExpressionTreeFromPostfix(postfix);
    }

    /**
     * Evaluates and returns the value associated with a binary expressiont tree.
     * @param tree a binary expression tree
     * @return the value associated with the root
     */
    public static int valueOfExpressionTree(LinkedBinaryTree<String> tree)//postfix로 변경 후 value 계산
    {
        /**
         * I changed from expression tree to postfix first with tree.postorder() method in AbstractBinaryTree Class
         * And then found value from postfix
         */
        LinkedStack<Integer> s = new LinkedStack<>();

        if(tree.isEmpty())
            return 0;
        else
        {
            String toPostfix = "";

            for(Position<String> p : tree.postorder())//change tree to postfix
            {
                toPostfix += p.getElement() + " ";
            }

            for(int i=0; i<toPostfix.length(); i++)
            {
                char c = toPostfix.charAt(i);

                if(DIGITS.indexOf(toPostfix.charAt(i)) != -1)
                {
                    int n = 0;
                    String temp = "";
                    while (DIGITS.indexOf(toPostfix.charAt(i)) != -1)
                    {
                        temp += toPostfix.charAt(i);
                        i++;
                    }

                    n = Integer.parseInt(temp);
                    s.push(n);
                }
                else if(toPostfix.charAt(i) == ' ') {}
                else
                {
                    int num1 = s.pop();
                    int num2 = s.pop();

                    if(toPostfix.charAt(i) == '+')
                        s.push(num2 + num1);
                    else if(toPostfix.charAt(i) == '-')
                        s.push(num2 - num1);
                    else if(toPostfix.charAt(i) == '/')
                        s.push(num2 / num1);
                    else if(toPostfix.charAt(i) == '*')
                        s.push(num2 * num1);
                }
            }
        }
        return s.pop();
    }

    /**
     * Returns a fully parenthesized infix expression
     * represented by the binary expression tree.
     * The binary expression tree is expected to contain only
     * integers (positive or negative) and the operators: +, -, /, *
     * @param tree a binary expression tree
     * @return infix expression represented by the tree
     */
    public static String infixExpressionFromTree(LinkedBinaryTree<String> tree)
    {
        /**
         * I changed from expression tree to postfix first with tree.postorder() method in AbstractBinaryTree Class
         * And then changed it to infix.
         */
        LinkedStack<String> s = new LinkedStack<>();

        String toPostfix = "";

        for(Position<String> p : tree.postorder())//change tree to postfix
        {
            toPostfix += p.getElement() + " ";
        }

        for(int i=0; i<toPostfix.length(); i++)
        {
            if(DIGITS.indexOf(toPostfix.charAt(i)) != -1)
            {
                String temp = "";
                while (DIGITS.indexOf(toPostfix.charAt(i)) != -1)
                {
                    temp += toPostfix.charAt(i);
                    i++;
                }
                s.push(temp);
            }
            else if(toPostfix.charAt(i) == ' ') {}//do nothing for blank.
            else
            {
                String num1 = s.pop();
                String num2 = s.pop();

                s.push("(" + num2 + toPostfix.charAt(i) + num1 + ")");
            }
        }
        return s.top();
    }

    public static void main(String args[])
    {
        String infix1 = "((40-5)*(9/(2+1)))";
        System.out.println(infix1);
        String postfix1 = postfixFromInfix(infix1);
        System.out.println(postfix1);
        LinkedBinaryTree<String> tree1 = constructExpressionTree(infix1);
        System.out.println(infixExpressionFromTree(tree1));
        System.out.println(valueOfExpressionTree(tree1));
    }
}