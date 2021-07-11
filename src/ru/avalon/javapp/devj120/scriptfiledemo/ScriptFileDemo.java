/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.avalon.javapp.devj120.scriptfiledemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rompop
 */
public class ScriptFileDemo {

    private final Map<String, Integer> vars = new HashMap<>();
    private File file;

    public ScriptFileDemo(String file) throws IOException {
        this.file = new File(file);
    }

    public void processFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.isEmpty()) {
                    continue;
                }
                if (s.charAt(0) == '#') {
                    continue;
                }

                String[] lineVars = s.split(" ");

                if (lineVars[0].equals("print")) {

                    String str = s.substring(6, s.length());
                    String resultStr = "";
                    String tempStr = "";
                    boolean startWritingString = false;
                    boolean startWritingValue = false;
                    char[] chars = str.toCharArray();
                    StringBuffer resultStrBuffer = new StringBuffer(resultStr);
                    StringBuffer tempStrBuffer = new StringBuffer(tempStr);
                    
                    for (int i = 0; i < chars.length; i++) {

                        if (chars[i] == '"' && startWritingString == false) {
                            startWritingString = true;
                        } else if (chars[i] == '"' && startWritingString) {
                            startWritingString = false;
                        } else if (chars[i] != '"' && startWritingString) {
                            resultStrBuffer.append(chars[i]);
                        } else if (chars[i] == '$' && startWritingValue == false) {
                            startWritingValue = true;
                            tempStrBuffer.append(chars[i]);
                        } else if (chars[i] != ',' && (startWritingValue && i != chars.length - 1)) {
                            tempStrBuffer.append(chars[i]);
                        } else if (chars[i] != ',' && (startWritingValue && i == chars.length - 1)) {
                            tempStrBuffer.append(chars[i]);
                            startWritingValue = false;
                            resultStrBuffer.append(get(tempStrBuffer.toString()));
                            tempStrBuffer.setLength(0);
                        } else if (chars[i] == ',' && startWritingValue) {
                            startWritingValue = false;
                            resultStrBuffer.append(get(tempStrBuffer.toString()));
                            tempStrBuffer.setLength(0);
                        }
                    }

                    System.out.println(resultStrBuffer.toString());

                } else if (lineVars[0].equals("set")) {

                    String var = "";
                    int value = 0;
                    boolean plus = true;
                    boolean equality = false;

                    for (int i = 1; i < lineVars.length; i++) {

                        if (lineVars[i].charAt(0) == '$') {
                            if (equality) {
                                
                                if (plus) {
                                    value += get(lineVars[i]);
                                } else {
                                    value -= get(lineVars[i]);
                                }
                            } else {
                                var = lineVars[i];
                            }
                        } else if (lineVars[i].matches("[-+]?\\d+")) {
                            if (plus) {
                                value += Integer.parseInt(lineVars[i]);
                            } else {
                                value -= Integer.parseInt(lineVars[i]);
                            }
                        } else if (lineVars[i].charAt(0) == '-') {
                            plus = false;
                        } else if (lineVars[i].charAt(0) == '+') {
                            plus = true;
                        } else if (lineVars[i].charAt(0) == '=') {
                            equality = true;
                        } else {
                            throw new IOException("Unsupported letter");
                        }
                    };

                    set(var, value);

                } else {
                    throw new IOException("Unsupported command");
                };
            }
        }
    }

    public Integer get(String varName) {
        
        Integer ing = vars.get(varName);
                
        if (ing == null) {
            throw new IllegalArgumentException("'varName' " + varName +" is not found");
        }
        
        return vars.get(varName);
    }

    public Integer set(String varName, int value) {
        if (varName == null) {
            throw new IllegalArgumentException("'varName' is null");
        }

        return vars.put(varName, value);
    }

    public Integer remove(String varName) {

        return vars.remove(varName);
    }

    public boolean contains(String varName) {

        return vars.containsKey(varName);
    }

}
