/* Block comment */
import java.util.Date;
/**
 * Doc comment here for <code>SomeClass</code>
 * @param T type parameter
 * @see Math#sin(double)
 */
@Annotation (name=value)
public class SomeClass<T extends Runnable> { // some comment
    private T field = null;
    private double unusedField = 12345.67890;
    private UnknownType anotherString = "";
    public static int staticField = 0;
    public final int instanceFinalField = 0;

    /**
     */
    public SomeClass(AnInterface param1, int[] reassignedParam,
                     int param2,
                  int param3) {
        int reassignedValue = this.staticField + param2 + param3;
        long localVar1, localVar2, localVar3, localVar4;
        int localVar = "IntelliJ"; // Error, incompatible types
        System.out.println(anotherString + toString() + localVar);
        long time = Date.parse("1.2.3"); // Method is deprecated
        reassignedValue ++;
        field.run();
        new SomeClass() {
            {
                int a = localVar;
            }
        };
        reassignedParam = new ArrayList<String>().toArray(new int[0]);
    }
}
enum AnEnum { CONST1, CONST2 }
interface AnInterface {
    int CONSTANT = 2;
    void method();
}
abstract class SomeAbstractClass {
}