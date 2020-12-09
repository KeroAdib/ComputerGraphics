/**
 * This class represents a 4D vector or homogenous point.
 */
public class Vector
{

    double x, y, z, w; //values for the vector

    /**
     * Creates a new vector with the given values
     *
     * @param x x value of vector
     * @param y y value of vector
     * @param z z value of vector
     * @param w w value of vector
     */
    public Vector(double x, double y, double z, double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * No arg constructor that makes a vector with 0s for each value
     */
    public Vector()
    {
        this(0, 0, 0, 0);
    }


    /**
     * Computes the dot product of two vectors. The
     * dot product is commutative.
     *
     * @param other the vector to be multiplied
     * @return the value of the dot product
     */
    public double dotProduct(Vector other)
    {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    public static void subtract(Vector difference, Vector minuend, Vector subtrahend)
    {
        difference.x = minuend.x - subtrahend.x;
        difference.y = minuend.y - subtrahend.y;
        difference.z = minuend.z - subtrahend.z;
    }

    public static void normalize(Vector x)
    {
        double length = Math.sqrt(Math.pow(x.x, 2) + Math.pow(x.y, 2) + Math.pow(x.z, 2));
        x.x = x.x / length;
        x.y = x.y / length;
        x.z = x.z / length;
    }

    public static void crossProduct(Vector v, Vector a, Vector b)
    {
        v.x = a.y * b.z - a.z * b.y;
        v.y = a.z * b.x - a.x * b.z;
        v.z = a.x * b.y - a.y * b.x;
        v.w = 0;
    }

    public String toString()
    {
        return "[" + x + " " + y + " " + z + " " + w + "]";
    }
}
