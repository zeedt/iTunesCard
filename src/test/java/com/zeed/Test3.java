package com.zeed;

public class Test3 {


    public static void main(String[] args) throws Exception {
        callMethod(1);
    }


    public static void callMethod(int a) throws Exception {
        switch (a) {
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
        }

        System.out.println("Done");
        throw new Exception("Error message");
    }

}
