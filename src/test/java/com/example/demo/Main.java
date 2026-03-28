package com.example.demo;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");


        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce((a, b) -> a + "-" + b).get();
        String concat1 = Stream.of("A", "B", "C", "D").reduce( "", (a, b) -> a + "+" + b);
        System.out.println(concat);
        System.out.println(concat1);

        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        // 求和，sumValue = 10, 无起始值
        sumValue = 0;
        Stream.of(1, 2, 3, 4).reduce(Integer::sum);
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").
                filter(x -> x.compareTo("Z") > 0).
                reduce("", String::concat);


        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }

        map.forEach((id, val) -> System.out.println(val));//val0 val1 val2 val3 val4 val5 val6 val7 val8 val9

        map.computeIfPresent(6, (num, val) -> val + num);
        System.out.println(map.get(6));

        map.computeIfAbsent(3, num -> "bam");
        map.get(3);




/*

        Consumer<String> printConsumer = a1 -> System.out.println(a1);
        Consumer<String> stringConsumer = (s) -> System.out.println(s.length());

        Arrays.asList("ab", "abc", "a", "abcd").stream().forEach(stringConsumer);

        int i = 1 << 1;
        int b = 4;
        System.out.println(b + ":" + Integer.toBinaryString(b));
        System.out.println(i + ":" + Integer.toBinaryString(i) + " - " + (i & b));

        */
//        System.out.println(~i + ":" +Integer.toBinaryString(~i));

//        for (int i = 1; i <= 5; i++) {
//            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//            System.out.println("i = " + i);
//        }
//        try {
//            test();
//        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//        iteratorTest();
//        ArrayList
//        PriorityQueue
//        Comparable
//        Integer
//        RandomAccess
//        Collections.sort();
//        LinkedList
//        HashMap
//        LinkedHashMap
//        StampedLock
//        ThreadLocal
//        CopyOnWriteArrayList
//        ConcurrentHashMap
//        HashSet
//        ArrayBlockingQueue
//        Objects
//        DelayQueue
//        Selector
//        NumberFormatException
//        InputStream
//        AutoCloseable
//        FileInputStream
//        DataInputStream
//        BufferedInputStream
//        BufferedOutputStream
//        BufferedReader
//        FutureTask

        /*InputStream is = Files.newInputStream(Paths.get("1"));
//        ByteBuffer
        RandomAccessFile reader = new RandomAccessFile("/Users/guide/Documents/test_read.in", "r");
        FileChannel channel = reader.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);

        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buffer.array()));*/

//        Thread.currentThread().interrupt();
//        Thread.interrupted();
//        ThreadLocal.withInitial(() -> "");

//        printThread();

//        Cloneable
//        StringBuffer
//        Class
//        Method
//        RoundingMode
//        StringBuilder
//        Unsafe
//        ServiceLoader
//        Class
//        ConcurrentLinkedQueue
//        AtomicInteger
//        AtomicStampedReference
//        DirectByteBuffer
//        HashSet
//        Thread
//        Object
//        ReentrantLock
//        Arrays.asList(1, 2, 3);

//        TransmittableThreadLocal

    }

    /**
     *
     */
    public static void printThread() {
        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }

    public static void test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HashMap<String,String > hm = new HashMap<>();
        System.out.println(hm.size());
        hm.put("1","1");
        System.out.println(hm.size());
        Class<?> mapType = hm.getClass();
        Method capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity : " + capacity.invoke(hm) + "    size : " + hm.size());

        // 示例：通过反射获取容量
        HashMap<String, String> a = new HashMap<>(2, 0.5f);
        mapType = a.getClass();
        capacity = mapType.getDeclaredMethod("capacity");
        capacity.setAccessible(true);
        System.out.println("capacity : " + capacity.invoke(a) + "    size : " + a.size());
    }

    public static void iteratorTest () {
        // 创建并赋值 HashMap
        Map<Integer, String> map = new HashMap();
        map.put(1, "Java");
        map.put(2, "JDK");
        map.put(3, "Spring Framework");
        map.put(4, "MyBatis framework");
        map.put(5, "Java中文社群");
        // 遍历
        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            System.out.println(key);
            System.out.println(map.get(key));
        }
    }

}