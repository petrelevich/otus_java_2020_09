package ru.otus.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

//VM option: -Djdk.attach.allowAttachSelf
//-XX:ObjectAlignmentInBytes=256
//-XX:-UseCompressedOops
public class JolExample {
    public static void main(String[] args) {
        new JolExample().demo();
    }

    public void demo() {

        //Какой будет результат?
        //System.out.println("boolean:" + VM.current().sizeOfField("boolean"));
        //Как этот результат получился?

        System.out.println(ClassLayout.parseClass(TestB.class).toPrintable());

        //Layout объекта
        //System.out.println(ClassLayout.parseInstance(new TestB()).toPrintable());

        //
        //        System.out.println(ClassLayout.parseClass(TestB2.class).toPrintable());
        //        System.out.println(ClassLayout.parseClass(TestB4.class).toPrintable());
        //        System.out.println(ClassLayout.parseClass(TestInt.class).toPrintable());

        //       System.out.println(ClassLayout.parseClass(TestInt2.class).toPrintable());
        //        System.out.println(ClassLayout.parseClass(TestInt4.class).toPrintable());

        //       System.out.println(ClassLayout.parseClass(Mix.class).toPrintable());
        //       System.out.println(ClassLayout.parseClass(TestL.class).toPrintable());
    }

    public class TestB {
        boolean valBool;
    }

    public class TestB2 {
        boolean valBool1;
        boolean valBool2;
    }

    public class TestB4 {
        boolean valInt1;
        boolean valInt2;
        boolean valInt3;
        boolean valInt4;
    }

    public class TestInt {
        int valInt1;
    }

    public class TestInt2 {
        int valInt1;
        int valInt2;
    }

    public class TestInt4 {
        int valInt1;
        int valInt2;
        int valInt3;
        int valInt4;
    }

    public class Mix {
        boolean boolVal;
        int intVal;
        boolean isBoolVal;
    }

    public class TestL {
        long l1;
        long l2;
    }
}
