package com.qf.jedis;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/258:50
 * @description: TODO
 */
public class test11 {

    public static void main(String[] args) {
        String s1 = new String("zs");
        String s2 = new String("zs");
        System.out.println(s1 == s2);//false
        System.out.println(s1.equals(s2));//true
        String s3 = "zs";
        String s4 = "zs";
        System.out.println(s3 == s4);//true
        System.out.println(s3 == s1);//false
        System.out.println(s3.equals(s1));//true
        String s5 = "zszs";
        String s6 = s3+s4;
        System.out.println(s5 == s6);//false
        System.out.println(s6.equals(s5));//true
        final String s7 = "zs";
        final String s8 = "zs";
        String s9 = s7+s8;
        System.out.println(s5 == s9);//true
        final String s10 = s3+s4;
        System.out.println(s5 == s10);//false

        System.out.println(test(6));
    }


    //菲波那切数列 1 1 2 3 5 8 13 21 。。。
    public static int test(int n){
        int num = 0;
        if(n==1|n==2){
            num=1;
        }else if(n>2){
            num=test(n-1)+test(n-2);
        }
        return num;
    }
}
