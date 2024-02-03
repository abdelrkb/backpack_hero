package sae.equipment;

public class Bag {
    private Integer[][] bag;
    private int rows;
    private int cols;
    
    public Bag() {
        this.rows = 3;
        this.cols = 5;
        this.bag = new Integer[rows][cols];
        
        for (int i = 0; i < rows; i ++) {
        	for (int j = 0; j< cols; j++) {
        		bag[i][j] = 0;
        	}
        }
    }
    public int getIndex(int rows, int cols) {
    	return bag[rows][cols];
    }
    public void changeIndex(int rows, int cols, int id) {
    	bag[rows][cols] = id;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
   
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(bag[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}