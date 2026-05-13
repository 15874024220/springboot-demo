package com.example.demo.arithmetic;

/**
 * 算数相关测试
 */
public class ArithmeticTest {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    public static void main(String[] args) {
        System.out.println("COUNT_BITS :::" + COUNT_BITS + ":::" + Integer.toBinaryString(COUNT_BITS));
        System.out.println("COUNT_MASK :::" + COUNT_MASK + ":::" + Integer.toBinaryString(COUNT_MASK));
        System.out.println("RUNNING :::" + RUNNING + ":::" + Integer.toBinaryString(RUNNING));
        System.out.println("SHUTDOWN :::" + SHUTDOWN + ":::" + Integer.toBinaryString(SHUTDOWN));
        System.out.println("STOP :::" + STOP + ":::" + Integer.toBinaryString(STOP));
        System.out.println("TIDYING :::" + TIDYING + ":::" + Integer.toBinaryString(TIDYING));
        System.out.println("TERMINATED :::" + TERMINATED + ":::" + Integer.toBinaryString(TERMINATED));

        System.out.println("ctlOf(RUNNING, workerCountOf(32))  :::" + ctlOf(RUNNING, workerCountOf(32))
                + ":::" + Integer.toBinaryString(ctlOf(RUNNING, workerCountOf(32)))
                + ":::" + Integer.toBinaryString(workerCountOf(32)));
    }

    private static int workerCountOf(int c)  { return c & COUNT_MASK; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }
}
