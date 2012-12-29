/*
New BSD License
Copyright (c) 2012, Norman David <normandavid67@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Norman David BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
  
 For a copy of the License type 'license'
 */
package org.jini.commands.helper;


import java.util.ArrayList;
/**
 * The table printer classes takes a matrix of data and prints it.
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */
public class TablePrinter {

    /**
     * Class Constructor - pass in columns as an array, or hard coded
     */
    public TablePrinter(String... names) {
        cols = new Col[names.length];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = new Col();
            cols[i].name = names[i];
            cols[i].maxWidth = names[i].length();
        }

        rows = new ArrayList<Row>();
    }

    /**
     * The row class represents one row of data.
     * Yes, it's just a wrapper for String[], but it helps
     * keep it simple.
     */
    private static class Row {

        String[] data;

        Row(String[] v) {
            data = v;
        }
    }

    /**
     * Contains column header and max width information
     */
    private static class Col {

        String name;
        int maxWidth;
    }
    // matrix information
    Col[] cols;
    ArrayList<Row> rows;

    /**
     * Adds a row - pass in an array or hard coded
     */
    public void addRow(String... values) {
        if (values.length != cols.length) {
            throw new IllegalArgumentException("invalid number of columns in values");
        }

        Row row = new Row(values);
        rows.add(row);
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() > cols[i].maxWidth) {
                cols[i].maxWidth = values[i].length();
            }
        }
    }

    /**
     *  Helper method to make sure column headers and 
     *  row information are printed the same
     *  @param v
     *  @param w 
     */
    private void print(String v, int w) {
        System.out.print(" ");
        System.out.print(v);
        System.out.print(spaces(w - v.length()));
        System.out.print(" |");
    }

  
    public void print() {
        
        System.out.println("");
        int numDashes = cols.length * 3 + 1;
        for (Col col : cols) {
            numDashes += col.maxWidth;
        }
        System.out.println(dashes(numDashes));

        System.out.print("|");

        for (Col col : cols) {
            print(col.name, col.maxWidth);
        }
        System.out.println("");


        // TODO make columns have + instead of -
        System.out.println(dashes(numDashes));
        for (Row row : rows) {
            System.out.print("|");
            int i = 0;
            for (String v : row.data) {
                print(v, cols[i++].maxWidth);
            }
            System.out.println("");
        }
        System.out.println(dashes(numDashes));
       
    }

    /**
     * Print a specific number of spaces for padding
     * 
     * @param i
     * @return String
     */
    private static String spaces(int i) {
        StringBuilder sb = new StringBuilder();
        while (i-- > 0) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * print a specific number of dashes
     * @param i
     * @return 
     */
    private static String dashes(int i) {
        StringBuilder sb = new StringBuilder();
        while (i-- > 0) {
            sb.append("-");
        }
        return sb.toString();
    }

    // test driver
    public static void main(String[] args) {
        System.out.println("TablePrinter test driver");

        TablePrinter table = new TablePrinter("MyTest", "OtherTest", "SillyColumn", "sadad");
        table.addRow("ABC Tabl", "DEF", " TablePrinter class TablePrinter class ", "sadad");
        table.addRow("This is a test", "of the", "TablePrinter class", "dsadsa");

        table.print();
    }
}
