/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.avalon.javapp.devj120.scriptfiledemo;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author rompop
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String file;

        if (args.length == 1) {

            file = args[0];
            ScriptFileDemo sf = new ScriptFileDemo(file);
            sf.processFile();

        } else {
            System.out.println("Must be 1 argument");
        }

    }
}
