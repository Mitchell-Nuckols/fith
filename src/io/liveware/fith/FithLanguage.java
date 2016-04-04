package io.liveware.fith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class FithLanguage {
    public Stack<Object> fithStack = new Stack<Object>();
    public HashMap<String, Object> fithVars = new HashMap<String, Object>();
    public ArrayList<String> reservedWords = new ArrayList<String>();

    public static void main(String[] args) {
        FithLanguage f = new FithLanguage();
        f.registerWords();
        f.processInput(f.lexer("VAR HW \"Hello World!\" STORE HW FETCH HW PRINT"));
    }

    public void registerWords() {
        reservedWords.add("PRINT");
        reservedWords.add("PSTACK");
        reservedWords.add("+");
        reservedWords.add("-");
        reservedWords.add("*");
        reservedWords.add("/");
        reservedWords.add("SQRT");
        reservedWords.add("DUP");
        reservedWords.add("DROP");
        reservedWords.add("SWAP");
        reservedWords.add("OVER");
        reservedWords.add("ROT");
        reservedWords.add("VAR");
        reservedWords.add("STORE");
        reservedWords.add("FETCH");
        reservedWords.add("\"");
    }

    public static String[] lexer(String rawInput) {
        String[] lexed = rawInput.split(" ");
        return lexed;
    }

    public static void checkSize(Stack<Object> stack, int size) {
        if (stack.size() < size) throw new Error("Not enough items on the stack!");
    }

    public void processInput(String[] input) {
        String[] inputProcess = input;
        String recordedString = "";
        boolean recordingString = false;

        for (int i = 0; i < inputProcess.length; i++) {
            String s = inputProcess[i];
            if(s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                recordingString = false;
                recordedString = s;
                fithStack.push(recordedString.replaceAll("\"", ""));
                continue;
            }else if(s.charAt(0) == '"') {
                recordingString = true;
                recordedString += s + " ";
                continue;
            }else if(s.charAt(s.length() - 1) == '"') {
                recordingString = false;
                recordedString += s;
                fithStack.push(recordedString.replaceAll("\"", ""));
                continue;
            }else if(recordingString) {
                recordedString += s + " ";
                continue;
            }

            switch (s.toUpperCase()) {
                case "PRINT":
                    print(fithStack);
                    continue;
                case "PSTACK":
                    printStack(fithStack);
                    continue;
                case "+":
                    add(fithStack);
                    continue;
                case "-":
                    subtract(fithStack);
                    continue;
                case "*":
                    multiply(fithStack);
                    continue;
                case "/":
                    divide(fithStack);
                    continue;
                case "SQRT":
                    sqrt(fithStack);
                    continue;
                case "DUP":
                    dup(fithStack);
                    continue;
                case "DROP":
                    drop(fithStack);
                    continue;
                case "SWAP":
                    swap(fithStack);
                    continue;
                case "OVER":
                    over(fithStack);
                    continue;
                case "ROT":
                    rot(fithStack);
                    continue;
                case "VAR":
                    var(fithStack, fithVars, inputProcess, i);
                    continue;
                case "STORE":
                    store(fithStack, fithVars, inputProcess, i);
                    continue;
                case "FETCH":
                    fetch(fithStack, fithVars, inputProcess, i);
                    continue;
                default:
                    if(isInt(s)) {
                        fithStack.push(Integer.parseInt(s));
                        continue;
                    }else if (!fithVars.containsKey(s)) throw new Error("Unknown word: " + s);
            }
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            //System.out.println(str + " is an integer");
            return true;
        } catch (NumberFormatException e) {
            //System.out.println(str + " is not an integer");
            return false;
        }
    }

    public void print(Stack<Object> stack) {
        checkSize(stack, 1);
        Object tos = stack.pop();
        System.out.println(tos);
    }

    public void printStack(Stack<Object> stack) {
        checkSize(stack, 1);
        String sOut = "";
        for (int i = 0; i < stack.size(); i++) {
            sOut += (i == stack.size() - 1) ? stack.get(i) : stack.get(i) + ", ";
        }
        System.out.println(sOut);
    }

    public void add(Stack<Object> stack) {
        checkSize(stack, 2);
        int tos1 = (int) stack.pop();
        int tos2 = (int) stack.pop();
        stack.push(tos1 + tos2);
    }

    public void subtract(Stack<Object> stack) {
        checkSize(stack, 2);
        int tos1 = (int) stack.pop();
        int tos2 = (int) stack.pop();
        stack.push(tos2 - tos1);
    }

    public void multiply(Stack<Object> stack) {
        checkSize(stack, 2);
        int tos1 = (int) stack.pop();
        int tos2 = (int) stack.pop();
        stack.push(tos1 * tos2);
    }

    public void divide(Stack<Object> stack) {
        checkSize(stack, 2);
        int tos1 = (int) stack.pop();
        int tos2 = (int) stack.pop();
        stack.push(tos2 / tos1);
    }

    public void sqrt(Stack<Object> stack) {
        checkSize(stack, 1);
        int tos1 = (int) stack.pop();
        stack.push((int) Math.sqrt((double) tos1));
    }

    public void dup(Stack<Object> stack) {
        checkSize(stack, 1);
        Object tos1 = stack.pop();
        stack.push(tos1);
        stack.push(tos1);
    }

    public void drop(Stack<Object> stack) {
        checkSize(stack, 1);
        stack.pop();
    }

    public void swap(Stack<Object> stack) {
        checkSize(stack, 2);
        Object tos1 = stack.pop();
        Object tos2 = stack.pop();
        stack.push(tos1);
        stack.push(tos2);
    }

    public void over(Stack<Object> stack) {
        checkSize(stack, 2);
        Object tos1 = stack.pop();
        Object tos2 = stack.pop();
        stack.push(tos2);
        stack.push(tos1);
        stack.push(tos2);
    }

    public void rot(Stack<Object> stack) {
        checkSize(stack, 3);
        Object tos1 = stack.pop();
        Object tos2 = stack.pop();
        Object tos3 = stack.pop();
        stack.push(tos2);
        stack.push(tos1);
        stack.push(tos3);
    }

    public void var(Stack<Object> stack, HashMap<String, Object> vars, String[] program, int loc) {
        String name;

        try {
            name = program[loc + 1].toUpperCase();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Error("No variable name specified");
        }

        if (reservedWords.contains(name)) throw new Error(name + " is a reserved word");

        if (!isInt(name) && name.length() >= 1) {
            vars.put(name, 0);
        }else {
            throw new Error(name + " is an invalid variable name assignment");
        }
    }

    public void store(Stack<Object> stack, HashMap<String, Object> vars, String[] program, int loc) {
        checkSize(stack, 1);

        String name;

        try {
            name = program[loc + 1].toUpperCase();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Error("No variable name specified");
        }

        if (!fithVars.containsKey(name)) throw new Error("Variable " + name + " has not been defined");

        Object tos1 = stack.pop();
        fithVars.put(name, tos1);
        //System.out.println("PUT " + tos1 + " VALUE FOR VAR " + name);
    }

    public void fetch(Stack<Object> stack, HashMap<String, Object> vars, String[] program, int loc) {
        String name;

        try {
            name = program[loc + 1].toUpperCase();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Error("No variable name specified");
        }

        if (!fithVars.containsKey(name)) throw new Error("Variable " + name + " has not been defined");

        Object val = vars.get(name);
        stack.push(val);
        //System.out.println("GOT " + val + " VALUE FOR VAR " + name);
    }
}
