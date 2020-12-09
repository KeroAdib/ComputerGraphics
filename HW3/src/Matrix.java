/**
 * This class represents a 4x4 matrix to be used
 * in graphics transformations. It is stored as
 * an array of Vectors with each Vector representing
 * a row of the matrix.
 *
 */
public class Matrix {
	
	Vector[] matrix; // each vector is a row of the array
	
	public static final Matrix IDENTITY = new Matrix(new Vector(1, 0, 0, 0), 
											   		 new Vector(0, 1, 0, 0),
											         new Vector(0, 0, 1, 0),
											         new Vector(0, 0, 0, 1));
	
	/**
	 * Public constructor that makes a matrix from four given vectors.
	 * Each vector is a row of the matrix starting from the top.
	 * 
	 * @param vec1 vector for row 0
	 * @param vec2 vector for row 1
	 * @param vec3 vector for row 2
	 * @param vec4 vector for row 3
	 */
	public Matrix(Vector vec1, Vector vec2, Vector vec3, Vector vec4) {
		matrix = new Vector[4];
		matrix[0] = vec1;
		matrix[1] = vec2;
		matrix[2] = vec3;
		matrix[3] = vec4;
	}
	
	/**
	 * Public no-arg constructor that makes a matrix of all 0s
	 */
	public Matrix() {
		this(new Vector(), new Vector(), new Vector(), new Vector());
	}
	
	/**
	 * Returns the vector from a row of the matrix
	 * 
	 * @param row the row to return
	 * @return Vector the vector from that row
	 */
	private Vector getRow(int row) {
		return matrix[row];
	}
	
	/**
	 * Returns a column from the matrix. Constructs
	 * a vector from the four values in the columns.
	 * The vector values (x, y, z, w) are obtained
	 * from the matrix starting at row1 down. Used
	 * for matrix multiplication.
	 * 
	 * @param col column of the matrix
	 * @return Vector constructed from the numbers in 
	 *                the given column
	 */


	private Vector getColumn(int col) {
		Vector ret = new Vector();
		
		switch (col) {
			case 0:
				ret.x = matrix[0].x;
				ret.y = matrix[1].x;
				ret.z = matrix[2].x;
				ret.w = matrix[3].x;
				break;
			case 1:
				ret.x = matrix[0].y;
				ret.y = matrix[1].y;
				ret.z = matrix[2].y;
				ret.w = matrix[3].y;
				break;
			case 2:
				ret.x = matrix[0].z;
				ret.y = matrix[1].z;
				ret.z = matrix[2].z;
				ret.w = matrix[3].z;
				break;
			case 3:
				ret.x = matrix[0].w;
				ret.y = matrix[1].w;
				ret.z = matrix[2].w;
				ret.w = matrix[3].w;
				break;
			default:
				break;
		}
		return ret;
	}

	/**
	 * Overloaded function for matrix multiplication. This version
	 * takes two matrices as arguments, multiplies the left one
	 * by the right one, and returns a new matrix containing the result.
	 * 
	 * @param m1 left matrix
	 * @param m2 right matrix
	 * @return Matrix the result of the multiplication
	 */
	public static Matrix multiply(Matrix m1, Matrix m2) {
		Matrix result = new Matrix();
		
		for (int i = 0; i < 4; i++) {
				result.matrix[i].x = m1.getRow(i).dotProduct(m2.getColumn(0));
				result.matrix[i].y = m1.getRow(i).dotProduct(m2.getColumn(1));
				result.matrix[i].z = m1.getRow(i).dotProduct(m2.getColumn(2));
				result.matrix[i].w = m1.getRow(i).dotProduct(m2.getColumn(3));
		}
		
		return result;
	}
	
	/**
	 * Overloaded function for matrix multiplication. This version
	 * takes a matrix and a vector, multiplies the two and returns
	 * a new vector containing the result.
	 * 
	 * @param m1 matrix to be multiplied
	 * @param vec the result of the multiplication
	 */
	public static Vector multiply(Matrix m1, Vector vec) {
		Vector result = new Vector();

		result.x = m1.getRow(0).dotProduct(vec);
		result.y = m1.getRow(1).dotProduct(vec);
		result.z = m1.getRow(2).dotProduct(vec);
		result.w = m1.getRow(3).dotProduct(vec);

		return result;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(matrix[i].toString());
			sb.append("\n");
		}
		return sb.toString();
	}
		
		
}



