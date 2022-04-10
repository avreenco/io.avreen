import java.util.Map;

/**
 * The class E kryo test.
 */
public class EKryoTest {
    /**
     * The X 1.
     */
    Integer x1;
    /**
     * The X 2.
     */
    Integer x2;

    /**
     * The Map.
     */
    Map map;

    /**
     * Gets x 1.
     *
     * @return the x 1
     */
    public Integer getX1() {
        return x1;
    }

    /**
     * Sets x 1.
     *
     * @param x1 the x 1
     */
    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    /**
     * Gets x 2.
     *
     * @return the x 2
     */
    public Integer getX2() {
        return x2;
    }

    /**
     * Sets x 2.
     *
     * @param x2 the x 2
     */
    public void setX2(Integer x2) {
        this.x2 = x2;
    }

    /**
     * Instantiates a new E kryo test.
     *
     * @param x1 the x 1
     */
    public EKryoTest(int x1) {
        this.x1 = x1;
        System.out.println("build EKryoTest with x1");
    }

    private EKryoTest() {
        System.out.println("build EKryoTest");
    }

}
