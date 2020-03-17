import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

class Expression {
	private String _type;
	private String _value;
	private Expression _left, _right;

	private Expression(String type, String value) {
		this(type, value, null, null);
	}

	private Expression(String type, String value, Expression left, Expression right) {
		_type = type;
		_value = value;
		_left = left;
		_right = right;
	}

	/**
	* Creates an operator expression.
	*/
	public static Expression Operator(Expression left, String operator, Expression right) {
		return new Expression("Operator", operator, left, right);
	}

	/**
	* Creates a number expression.
	*/
	public static Expression Number(double value) {
		return new Expression("Number", Double.toString(value));
	}

	/**
	* Creates a variable expression.
	*/
	public static Expression Variable(String name) {
		return new Expression("Variable", name);
	}

	/**
	* Very quick-and-dirty expression parser; doesn't really do any error checking.
	* But it's enough to build an Expression from a (known-to-be-correct) String.
	*/
	public static Expression quickParse(String input) {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
		tokenizer.ordinaryChar('-');
		tokenizer.ordinaryChar('/');
		Stack<Character> operators = new Stack<>();
		Stack<Expression> operands = new Stack<>();
		try { tokenizer.nextToken(); }
		catch (IOException e) { throw new RuntimeException(e); }
		while(tokenizer.ttype != StreamTokenizer.TT_EOF) {
			int prec = 2;
			switch(tokenizer.ttype) {
				case StreamTokenizer.TT_NUMBER: operands.push(Number(tokenizer.nval));   break;
				case StreamTokenizer.TT_WORD:   operands.push(Variable(tokenizer.sval)); break;
				case '^': case '(': operators.push((char)tokenizer.ttype);  break;
				case ')':
					while(operators.peek() != '(')
						poperator(operators, operands);
					operators.pop();
					break;
				case '+': case '-': prec = 1; // fall thru
				case '*': case '/':
					while(!operators.empty()) {
						char top = operators.peek();
						int topPrec = (top == '^') ? 3 : (top == '*' || top == '/') ? 2 : 1;
						if(top == '(' || topPrec < prec) break;
						poperator(operators, operands);
					}
					operators.push((char)tokenizer.ttype);
					break;
				default: throw new RuntimeException("wat");
			}
			try { tokenizer.nextToken(); }
			catch (IOException e) { throw new RuntimeException(e); }
		}
		while(!operators.empty()){ poperator(operators, operands); }
		return operands.pop();
	}

	private static void poperator(Stack<Character> operators, Stack<Expression> operands) {
		Expression r = operands.pop();
		Expression l = operands.pop();
		operands.push(Operator(l, operators.pop() + "", r));
	}

	// These can be used to quickly check if an Expression is an Operator, Number, or Variable.
	public boolean isOperator() { return _type.equals("Operator"); }
	public boolean isNumber()   { return _type.equals("Number");   }
	public boolean isVariable() { return _type.equals("Variable"); }

	/**
	* For Numbers, converts the _value to a double and returns it.
	* Will crash for non-Numbers.
	*/
	private double getNumberValue() { return Double.parseDouble(_value); }

	/**
	* Recursively clones an entire Expression tree.
	* Note how this method works: operators are the recursive case, and
	* numbers and variables are base cases.
	*/
	public Expression clone() {
		if(this.isOperator()) {
			return Expression.Operator(_left.clone(), _value, _right.clone());
		} else if(this.isVariable()) {
			return Expression.Variable(_value);
		} else {
			return Expression.Number(getNumberValue());
		}
	}

	/**
	* Converts this expression to an infix expression representation.
	*/
	public String toString() {
	    if(isNumber()){
	        return _value;
	    }
	    if(isVariable()){
	        return _value;
	    }
	    if(isOperator()){
	        return "("+ _left.toString() + " "+ _value +" "+ _right.toString() + ")";
	    }
		return "<not implemented>";
	}

	/**
	* Converts this expression to a postfix expression representation.
	*/
	public String toPostfix() {
		if(isNumber()){
		    return _value;
        }
        if(isVariable()){
            return _value;
        }
        if(isOperator()){
            return _left.toPostfix() + " " + _right.toPostfix() +" "+ _value;
        }

		return "<not implemented>";
	}

	/**
	* Given the variables map (which tells what values each variable has),
	* evaluates the expression and returns the computed value.
	*/
	public double evaluate(Map<String, Double> variables) {
		if(isNumber()){
		    return getNumberValue();
        }
        if(isVariable()){
            if(!variables.containsKey(_value)){
                throw new ExpressionError("The variable name didn't exist");
            }
            return variables.get(_value);
        }
        if(isOperator()){
            switch(_value){
                case "+":
                    return _left.evaluate(variables) +  _right.evaluate(variables);
                case "-":
                    return _left.evaluate(variables) -  _right.evaluate(variables);
                case "*":
                    return _left.evaluate(variables) *  _right.evaluate(variables);
                case "/":
                    return _left.evaluate(variables) /  _right.evaluate(variables);
                case "^":
                    return Math.pow(_left.evaluate(variables),_right.evaluate(variables));
            }
        }
		return 0;
	}

	/**
	* Creates a new Expression that is the reciprocal of this one.
	*/
	public Expression reciprocal() {
	    Expression reciprocal = new Expression(_type,_value,_left,_right);
	    double value = 0;
	    Expression temp;
		if(isNumber()){
		    reciprocal = reciprocal.clone();
		    value = 1/reciprocal.getNumberValue();
		    return Number(value);
        }
        if(isOperator()){
            if(_value.equals("/")){
                reciprocal = reciprocal.clone();
                temp = reciprocal._left;
                reciprocal._left= reciprocal._right.clone();
                reciprocal._right= temp.clone();
                return reciprocal;
            }
            else {
                temp = reciprocal.clone();
                reciprocal._left = Number(1.0);
                reciprocal._right = temp;
                reciprocal = Operator(reciprocal._left,"/",reciprocal._right);
                return reciprocal;
            }
        }
		return Number(0);
	}

	/**
	* Gets a set of all variables which appear in this expression.
	*/
	public Set<String> getVariables() {
		Set<String> variables = new HashSet<>();
		if(isNumber()){
		    variables.add("");
        }
		if(isVariable()){
		    variables.add(_value) ;
        }
        if(isOperator()){
            variables = find(variables,_left,_right);
        }
		return variables;
	}

	private Set<String> find(Set<String> variables,Expression _left, Expression _right){
	    if(_left.isVariable()){
	        variables.add(_left._value);
        }
        if(_right.isVariable()){
            variables.add(_right._value);
        }
        if(_left.isOperator()){
            find(variables,_left._left,_left._right);
        }
        if(_right.isOperator()){
            find(variables,_right._left,_right._right);
        }
	    return variables;
    }

	/**
	* Constructs a new Expression of the form:
	* 	(numbers[0] * numbers[1] * ... numbers[n-1]) ^ (1 / n)
	* and returns it.
	*/
	public static Expression geometricMean(double[] numbers) {
	    Expression number = Number(numbers.length);
	    Expression operator = null;
	    if(numbers.equals(null)){
	        throw new NullPointerException("The input array is an empty array");
        }
        if(numbers.length==1){
            return Number(numbers[0]);
        }
        else {
            operator = Operator(Number(numbers[0]), "*", Number(numbers[1]));
        }
        for(int i=2; i<numbers.length; i++){
            operator = Operator(operator, "*", Number(numbers[i]));
        }
        Expression whole = new Expression("Operator", "^", operator, number.reciprocal());

        if(whole!=null){
            return whole;
        }
		return Number(0);
	}

	/**
	* EXTRA CREDIT: converts this expression to an infix expression representation,
	* but only places parentheses where needed to override the order of operations.
	*/
	public String toNiceString() {
	    int precedence =0;
		if(isNumber()){
		    return _value;
        }
        if(isVariable()){
            return _value;
        }
        if(isOperator()){
            precedence = getprecedence(_value);

//            if(precedence ==3){
//                return "(" + _left.toNiceString() +")"+ _value +_right.toNiceString();
//            }
//            if(precedence==1){
//                return _left.toNiceString() +" "+ _value +" "+_right.toNiceString();
//            }else
            if(_left.isOperator() && getprecedence(_left._value)<precedence){
                return "(" + _left.toNiceString() +")"+ " " + _value + " "+_right.toNiceString();
            }
            if(_right.isOperator()&& getprecedence(_right._value)<precedence){
                return  _left.toNiceString() +" " +_value + "( "+ _right.toNiceString() + ")";
            }
            return _left.toNiceString() +" "+ _value +" "+_right.toNiceString();
        }
		return "<not implemented>";
	}

	public int getprecedence(String _value){
	    if(_value.equals("^")){
	        return 3;
        }
        else if(_value.equals("*") || _value.equals("/")){
            return 2;
        }
        else if(_value.equals("+") || _value.equals("-")){
            return 1;
        }
        return 0;
    }
}