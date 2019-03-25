package com.subject.opencvdemo;

public class Test {

   public  String str= new String("good");
    char[] ch = {'a','b','c'};

    public static void main(String args[]) {

        Test test = new Test();
        test.change(test.str,test.ch);
        System.out.print(test.str);


    }
    public void change(String str,char[] ch){
        str = "test ok";
        ch[0] = 'g';

    }
}
