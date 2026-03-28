package com.example.demo;

public class LambdaClassSuper {
    LambdaClass.LambdaInterface sf(){
        return null;
    }
    class LambdaClass extends LambdaClassSuper {

        @FunctionalInterface
        public interface LambdaInterface {
            void f();
        }

        public static LambdaInterface staticF() {
            return null;
        }

        public LambdaInterface f() {
            return null;
        }

        void show() {
            //1.调用静态函数，返回类型必须是functional-interface
            LambdaInterface t = LambdaClass::staticF;


            LambdaInterface lif = LambdaClass::staticF;

            //2.实例方法调用
            LambdaClass lambdaClass = new LambdaClass();
            LambdaInterface lambdaInterface = lambdaClass::f;

            //3.超类上的方法调用
            LambdaInterface superf = super::sf;

            //4. 构造方法调用
            LambdaInterface tt = LambdaClassSuper::new;
        }
    }
}