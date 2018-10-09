package com.example.indalamar.myapplication2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Parser {
    public static BigDecimal parse (String expression) throws NullPointerException , NotEnoughOperands , NotCorrectInput ,ArithmeticException{
        ArrayList<String> PN = parseToArray(expression);
        PN=parseToRPN(PN);
        return compute(PN);
    }
    private static ArrayList<String> parseToArray(String expression)
    {
        ArrayList<String> PN = new ArrayList<>(0);
        String number ="";
        for (int i = 0 ; i < expression.length(); ++i) {
            if (!isNumber(expression.charAt(i))) {
                if (!number.isEmpty()) {
                    PN.add(number);
                }
                if (isUnarMinus(expression,i))
                    PN.add(Character.toString('0'));
                PN.add(Character.toString(expression.charAt(i)));
                number = "";
            }
            else
            {
                number=number.concat(Character.toString(expression.charAt(i)));
            }
        }
        if (!number.isEmpty()) {
            PN.add(number);
        }
        return PN;
    }
    private static ArrayList<String> parseToRPN(ArrayList<String> PN) throws ArithmeticException{
        ArrayList<String> answer = new ArrayList<>();
        ArrayList<String> stack = new ArrayList<>();
        for (int i = 0 ; i <PN.size();++i)
        {
            if (isNumber(PN.get(i).charAt(0)))
            {
                answer.add(PN.get(i));
            }
            else
            {
                if (PN.get(i).charAt(0)==')'){
                    for (;stack.get(stack.size()-1).charAt(0)!='(' ; )
                    {
                        if (stack.size()==1)
                        {
                            throw new ArithmeticException();
                        }
                        answer.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    stack.remove(stack.size()-1);
                    continue;
                }
                if (stack.size()!=0&&getPriority(stack.get(stack.size()-1).charAt(0))<getPriority(PN.get(i).charAt(0)))
                {
                    stack.add(PN.get(i));
                }
                else{
                    for (int j = stack.size()-1;(j >= 0) &&(!stack.get(stack.size()-1).equals("(")); --j)
                    {
                        answer.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    stack.add(PN.get(i));
                }
            }
        }
        for (int j = stack.size()-1;j >= 0 ; --j)
        {
            if (getPriority(stack.get(stack.size()-1).charAt(0))==10)
            {
                throw new ArithmeticException();
            }
            answer.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }
        return answer;
    }
    private static BigDecimal compute(ArrayList<String> RPN) throws NotCorrectInput , NotEnoughOperands , NullPointerException {
        ArrayList<BigDecimal> stack = new ArrayList<> ();
        for (int i = 0 ; i <RPN.size();++i)
        {
            if (isNumber(RPN.get(i).charAt(0)))
            {
                if (!isCorrectNumber(RPN.get(i)))
                {
                    throw new NotCorrectInput();
                }
                stack.add(new BigDecimal(RPN.get(i)));
            }
            else
            {
                if (stack.size()>=2)
                {

                    BigDecimal res = computeSimple(stack,RPN.get(i));
                    stack.remove(stack.size()-1);
                    stack.remove(stack.size()-1);
                    stack.add(res);

                }
                else
                {
                    throw new NotEnoughOperands();
                }
            }
        }
        return stack.get(0);
    }
    private static BigDecimal computeSimple(ArrayList<BigDecimal> stack , String operation) throws NullPointerException
    {
        BigDecimal result = new BigDecimal("0") ;
        switch (operation)
        {
            case "+":
                result = stack.get(stack.size()-2).add(stack.get(stack.size()-1));
                break;
            case "-":
                result = stack.get(stack.size()-2).subtract(stack.get(stack.size()-1));
                break;
            case "*":
                result = stack.get(stack.size()-2).multiply(stack.get(stack.size()-1));
                break;
            case "/":
                if (stack.get(stack.size()-1).equals(new BigDecimal("0")))
                {
                    throw new NullPointerException();
                }
                BigDecimal first = stack.get(stack.size()-2).add(new BigDecimal("0.0"));
                result = first.divide(stack.get(stack.size()-1),4, RoundingMode.CEILING);
                break;
        }
        return result;
    }
    private static boolean isUnarMinus (String string , int iterator)
    {
        if (string.charAt(iterator)=='-')
        {
            if (iterator==0)
                return true;
            else
            {
                if (string.charAt(iterator-1)=='(')
                    return true;
            }
        }
        return false;
    }
    private static boolean isNumber(char schar) {
        return (schar>='0'&&schar<='9')||schar=='.';
    }
    private static int getPriority(char operation)
    {
        if (operation=='+'||operation=='-')
            return 1;
        if (operation =='*'|| operation=='/')
            return 2;
        return 10;
    }
    private static boolean isCorrectNumber(String box)
    {
        int count = 0;
        int count2 = 0;
        int i ;
        for (i = 0 ; i<box.length()&&box.charAt(i) !='.' ; ++i)
        {
            count++;
        }
        if (i==box.length())
        {
            return true;
        }
        i++;
        for (;i<box.length()&&box.charAt(i) !='.';++i)
        {
            count2++;
        }
        return count >0  &&count+count2==box.length()-1;
    }
}
