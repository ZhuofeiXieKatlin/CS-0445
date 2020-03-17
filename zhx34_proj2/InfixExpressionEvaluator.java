// Zhuofei Xie 4222848
import java.io.*;

/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream. It should not create a full postfix expression along the way; it
 * should convert and evaluate in a pipelined fashion, in a single pass.
 */
public class InfixExpressionEvaluator {
	// Tokenizer to break up our input into tokens
	StreamTokenizer tokenizer;

	// Stacks for operators (for converting to postfix) and operands (for
	// evaluating)

	int open = 0;
	boolean haveOperands;
	StackInterface<Character> operatorStack;
	StackInterface<Double> operandStack;

	/**
	 * Initializes the evaluator to read an infix expression from an input
	 * stream.
	 * @param input the input stream from which to read the expression
	 */
	public InfixExpressionEvaluator(InputStream input) {
		// Initialize the tokenizer to read from the given InputStream
		tokenizer = new StreamTokenizer(new BufferedReader(
						new InputStreamReader(input)));

		// StreamTokenizer likes to consider - and / to have special meaning.
		// Tell it that these are regular characters, so that they can be parsed
		// as operators
		tokenizer.ordinaryChar('-');
		tokenizer.ordinaryChar('/');

		// Allow the tokenizer to recognize end-of-line, which marks the end of
		// the expression
		tokenizer.eolIsSignificant(true);

		// Initialize the stacks
		operatorStack = new ArrayStack<Character>();
		operandStack = new ArrayStack<Double>();
	}

	/**
	 * Parses and evaluates the expression read from the provided input stream,
	 * then returns the resulting value
	 * @return the value of the infix expression that was parsed
	 */
	public double evaluate() throws ExpressionError {
		// Get the first token. If an IO exception occurs, replace it with a
		// runtime exception, causing an immediate crash.
		try {
			tokenizer.nextToken();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Continue processing tokens until we find end-of-line
		while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
			// Consider possible token types
			switch (tokenizer.ttype) {
				case StreamTokenizer.TT_NUMBER:
					// If the token is a number, process it as a double-valued
					// operand
					handleOperand((double)tokenizer.nval);
					break;
				case '+':
				case '-':
				case '*':
				case '/':
				case '^':
				case '%':
					// If the token is any of the above characters, process it
					// is an operator
					handleOperator((char)tokenizer.ttype);
					break;
				case '(':
				case '{':
				case '[':
					// If the token is open bracket, process it as such. Forms
					// of bracket are interchangeable but must nest properly.
					handleOpenBracket((char)tokenizer.ttype);
					break;
				case ')':
				case '}':
				case ']':
					// If the token is close bracket, process it as such. Forms
					// of bracket are interchangeable but must nest properly.
					handleCloseBracket((char)tokenizer.ttype);
					break;
				case StreamTokenizer.TT_WORD:
					// If the token is a name, process it as such.
					handleName(tokenizer.sval.toLowerCase());
					break;
				default:
					// If the token is any other type or value, throw an
					// expression error
					throw new ExpressionError("Unrecognized token: " +
									String.valueOf((char)tokenizer.ttype));
			}

			// Read the next token, again converting any potential IO exception
			try {
				tokenizer.nextToken();
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Almost done now, but we may have to process remaining operators in
		// the operators stack
		handleRemainingOperators();

		// Return the result of the evaluation
		// TODO: Fix this return statement
		return operandStack.peek();
	}

	/**
	 * This method is called when the evaluator encounters an operand. It
	 * manipulates operatorStack and/or operandStack to process the operand
	 * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
	 * @param operand the operand token that was encountered
	 */
	void handleOperand(double operand) {
		if(Double.isInfinite(operand)){
			throw new ExpressionError("The result is an infinite number " + operand);
		}
		if(operatorStack.isEmpty() && operandStack.isEmpty()){
			operandStack.push(operand);
			haveOperands = true;
		}else if(operandStack.isEmpty()){
			if(!isOpenbracket()){
				throw new ExpressionError("There is no first operand for " + operatorStack.peek());
			}else{
				operandStack.push(operand);
				haveOperands = true;
			}
		}else{
			if(haveOperands ) throw new ExpressionError("No operator between " + operandStack.peek() + " and " + operand);
			operandStack.push(operand);
			haveOperands = true;
		}


		// TODO: Complete this method
	}

	/**
	 * This method is called when the evaluator encounters a name. This is a
	 * special kind of operand, really. See the project description.
	 * @param name the lowercase name that was encountered
	 */
	void handleName(String name) {
		boolean isHex;
		if(name.equals("pi")){
			handleOperand(Math.PI);
		}
		else if(name.equals("e")){
			handleOperand(Math.E);
		} else if(!operandStack.isEmpty() && operandStack.peek().equals(0.0)) {
			haveOperands = false;
			if(name.startsWith("x")){
				name = name.substring(1);
				int isNumber;
				try{
					isNumber = Integer.parseInt(name, 16);
					isHex = true;
				}catch (NumberFormatException e){
					isHex = false;
					throw new ExpressionError("Unknown name " + name);
				}
				if(isHex){
					operandStack.pop();
					handleOperand(isNumber);
				}
			}else{
				throw new ExpressionError("Unknown name " + name);
			}
		}else if (operandStack.isEmpty()){
//			if(name.contains("=")){
//				Scanner userinput = new Scanner(System.in);
//				String input = userinput.nextLine();
//				name.replaceAll(name, input);
//				double value = Double.parseDouble(input);
//				handleOperand(value);
//			}
			throw new ExpressionError("Unknown name " + name);
		}else{
			throw new ExpressionError("Unknown name " + name);
		}
		// TODO: Complete this method
	}

	boolean isOpenbracket(){
		if((operatorStack.peek().equals('(') || operatorStack.peek().equals('[')|| operatorStack.peek().equals('{'))){
			return true;
		}
		return false;
	}

	/**
	 * This method is called when the evaluator encounters an operator. It
	 * manipulates operatorStack and/or operandStack to process the operator
	 * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
	 * @param operator the operator token that was encountered
	 */

	void handleOperator(char operator) {
		if(operandStack.isEmpty() && operatorStack.isEmpty()){
			if(operator != '-'){
				throw new ExpressionError("No first operand for " + operator);
			}
		}
		if(!haveOperands && !operatorStack.isEmpty() ){
			if(isOpenbracket() ){
				throw new ExpressionError("operator " + operator + " after open bracket " + operatorStack.peek());
			}else {
				throw new ExpressionError("Two operators in a row " + operatorStack.peek() + " " + operator);
			}
		}
		int NewPrecedent = getPrecedent(operator);
		int PreviousPrecedent;
		char previousOne;
		outloop:
		while (!operatorStack.isEmpty() && operator != '^') {
			previousOne = operatorStack.peek();
			if(previousOne == '(' || previousOne == '{' || previousOne == '[') {
				break outloop;
			}
			PreviousPrecedent = getPrecedent(previousOne);
			if (NewPrecedent >= PreviousPrecedent) {
				char Operator = operatorStack.pop();
				double input1 = operandStack.pop();
				if(operandStack.isEmpty()){
					calculate(Operator, input1, 0.0);
				}else{
					double input2 = operandStack.pop();
					calculate(Operator,input1,input2);
				}
			} else{
				break outloop;
			}
		}
		operatorStack.push(operator);
		haveOperands = false;
		// TODO: Complete this method
	}

	/**
	 * This method is called when the evaluator encounters an open bracket. It
	 * manipulates operatorStack and/or operandStack to process the open bracket
	 * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
	 * @param openBracket the open bracket token that was encountered
	 */
	void handleOpenBracket(char openBracket) {
		operatorStack.push(openBracket);
		open ++;
		// TODO: Complete this method
	}

	/**
	 * This method is called when the evaluator encounters a close bracket. It
	 * manipulates operatorStack and/or operandStack to process the close
	 * bracket according to the Infix-to-Postfix and Postfix-evaluation
	 * algorithms.
	 * @param closeBracket the close bracket token that was encountered
	 */
	void handleCloseBracket(char closeBracket) {
		char top;
		if(!haveOperands) throw new ExpressionError("No operand after "+ operatorStack.peek()+ " before " + closeBracket);
		if (open == 0) throw new ExpressionError("No opening bracket for " + closeBracket);
		while (!operatorStack.isEmpty()){
			top = operatorStack.peek();
			if(top != '(' && top != ')' && top != '{' && top != '}' && top != '[' && top != ']'){
				char Operator = operatorStack.pop();
				double input1 = operandStack.pop();
				double input2 = operandStack.pop();
				calculate(Operator,input1,input2);
			}else if((top == '(' && closeBracket == ')')|| (top == '{' && closeBracket == '}')||(top == '[' && closeBracket == ']')){
				operatorStack.pop();
				break;
			}else {
				if(operatorStack.peek().equals('('))
				    throw new ExpressionError("MisMatched bracket(found " + closeBracket + " instead of ) )" );
				if(operatorStack.peek().equals('['))
					throw new ExpressionError("MisMatched bracket(found " + closeBracket + " instead of ] )" );
				if(operatorStack.peek().equals('{'))
					throw new ExpressionError("MisMatched bracket(found " + closeBracket + " instead of } )" );
			}
		}
		open --;
		// TODO: Complete this method
	}

	/**
	 * This method is called when the evaluator encounters the end of an
	 * expression. It manipulates operatorStack and/or operandStack to process
	 * the operators that remain on the stack, according to the Infix-to-Postfix
	 * and Postfix-evaluation algorithms.
	 */
	void handleRemainingOperators() {
		char remain;
		while (!operatorStack.isEmpty()){
			remain = operatorStack.peek();
			if(remain == '(' || remain == '{' || remain == '[') throw new ExpressionError("No closing bracket " + remain);
			if(!haveOperands)throw new ExpressionError("No second operand for " + operatorStack.peek());
			char Operator = operatorStack.pop();
			double input1 = operandStack.pop();
			if(operandStack.isEmpty()){
				double in2 = 0.0;
				calculate(Operator, input1, in2);
			}else {
				double input2 = operandStack.pop();
				calculate(Operator, input1, input2);
			}

		}

		// TODO: Complete this method
	}



	int getPrecedent(char operator){
		int Precedent = 0;
		if(operator == '{' || operator == '}' || operator == '(' || operator == ')' || operator == '[' || operator == ']'){
			Precedent = 1;
		}else if(operator == '^'){
			Precedent = 2;
		} else if (operator == '*'|| operator == '/' || operator == '%'){
			Precedent = 3;
		} else if(operator == '+' || operator == '-'){
			Precedent = 4;
		}
		return Precedent;
	}

	void calculate(char operator, double input1, double input2){
		double result = 0.0;
		switch (operator) {
			case '+':
				result = input1 + input2;
				break;
			case '-':
				result = input2 - input1;
				break;
			case '*':
				result = input1 * input2;
				break;
			case '/':
				if (input1 == 0) throw new ExpressionError("The denominator cannot be zero");
				else {
					result = input2 / input1;
				}
				break;
			case '%':
				result = input2 % input1;
				break;
			case '^':
				result = Math.pow(input2, input1);
				break;
			default:
				break;
		}
		if(Double.isInfinite(result)){
			throw new ExpressionError("The result is an infinite number " );
		}
		operandStack.push(result);
	}


	/**
	 * Creates an InfixExpressionEvaluator object to read from System.in, then
	 * evaluates its input and prints the result.
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("Infix expression:");
		InfixExpressionEvaluator evaluator =
						new InfixExpressionEvaluator(System.in);
		Double value = null;
		try {
			value = evaluator.evaluate();
		} catch (ExpressionError e) {
			System.out.println("ExpressionError: " + e.getMessage());
		}
		if (value != null) {
			System.out.println(value);
		} else {
			System.out.println("Evaluator returned null");
		}
	}

}

