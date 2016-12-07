package cs311.hw4;

public class SlowMatrix implements IMatrix, IMeasurable{
	public int columns, rows;
	public boolean multiply;
	public int[][] matrix;
	
	public SlowMatrix(int columns, int rows, boolean multiply){
		this.matrix = new int[columns][rows];
		this.columns = columns;
		this.rows = rows;
		this.multiply = multiply;
	}
	
	@Override
	public IMatrix subMatrix(int upperLeftRow, int upperLeftCol, int lowerRightRow, int lowerRightCol) throws IllegalArgumentException {
		int rows = lowerRightRow - upperLeftRow + 1;
		int columns = lowerRightCol - upperLeftCol + 1;
		
		if(Math.abs(upperLeftRow + upperLeftCol + lowerRightRow + lowerRightCol + rows + columns) != upperLeftRow + upperLeftCol + lowerRightRow + lowerRightCol + rows + columns || 
				upperLeftRow + lowerRightRow + rows > this.rows*3 || upperLeftCol + lowerRightCol + columns > this.columns*3){
			throw new IllegalArgumentException();
		}
		
		SlowMatrix sub = new SlowMatrix(columns, rows, true);
		for(int j = 0; j < sub.columns; j++){
			for(int k = 0; k < sub.rows; k++){
				sub.setElement(k, j, this.matrix[upperLeftCol + j][upperLeftRow + k]);
			}
		}
		
		return sub;
	}

	@Override
	public void setElement(int row, int col, Number val) throws IllegalArgumentException {
		if(this.rows < row || this.columns < col || rows < 0 || col < 0){
			throw new IllegalArgumentException();
		}
		
		this.matrix[col][row] = (int)val;
	}

	@Override
	public Number getElement(int row, int col) throws IllegalArgumentException {
		if(this.rows < row || this.columns < col || rows < 0 || col < 0){
			throw new IllegalArgumentException();
		}
		
		return this.matrix[col][row];
	}

	@Override
	public IMatrix multiply(IMatrix mat) throws IllegalArgumentException {
		if(mat.getClass() != this.getClass()){
			throw new IllegalArgumentException();
		}
		SlowMatrix b = (SlowMatrix)mat;
		if(this.columns != b.rows){
			throw new IllegalArgumentException();
		}
		
		SlowMatrix product = new SlowMatrix(b.columns, this.rows, true);
		int val = 0;
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < b.columns; j++){
				for(int k = 0; k < this.columns; k++){
					val += (int)this.getElement(i,k)*(int)b.getElement(k, j);
				}
				product.setElement(i, j, val);
				val = 0;
			}
		}
		return product;
	}

	@Override
	public IMatrix add(IMatrix mat) throws IllegalArgumentException {
		if(mat.getClass() != this.getClass()){
			throw new IllegalArgumentException();
		}
		SlowMatrix b = (SlowMatrix)mat;
		if(this.columns != b.columns || this.rows != b.rows){
			throw new IllegalArgumentException();
		}
		
		SlowMatrix product = new SlowMatrix(b.columns, this.rows, false);
		
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < b.columns; j++){
				product.setElement(i, j, (int)this.getElement(i,j) + (int)b.getElement(i,j));
			}
		}
		return product;
	}

	@Override
	public void execute() {
		if(this.multiply){
			this.multiply(this);
		}else{
			this.add(this);
		}
		
	}

}
