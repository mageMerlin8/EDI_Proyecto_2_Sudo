
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author EMENAG
 */

public class Sudoku {
    private int[][] grid;
    private final ArraySet<Integer>[] rows;
    private final ArraySet<Integer>[] columns;
    private final ArraySet<Integer>[] cuadros;
    private static  ArraySet<Integer> nums;
    
    public Sudoku(){
        grid = new int[9][9];
        rows = new ArraySet[9];
        columns = new ArraySet[9];
        cuadros = new ArraySet[9];
        nums = new ArraySet(9);
        for(int i = 0; i < 9; i++){
            rows[i] = new ArraySet(9);
            columns[i] = new ArraySet(9);
            cuadros[i] = new ArraySet(9);
            nums.add(i+1);
        }
    }

    public int[][] getGrid() {
        return grid;
    }
    
    private void checaFila(Celda cell){
        int row = cell.getRow();
        ArraySet<Integer> nueva =new ArraySet(9);
        for(int i = 0; i < 9; i++)
            if(grid[row][i] != 0)
                nueva.add(grid[row][i]);
        rows[row] = nueva;
    }
    private void checaColumna(Celda cell){
        int col = cell.getCol();
        ArraySet<Integer> nueva = new ArraySet(9);
        for(int i = 0; i < 9; i++)
            if(grid[i][col] != 0)
                nueva.add(grid[i][col]);
        columns[col] = nueva;
    }
    private void checaCuadro(Celda cell){
        ArraySet<Integer> nueva = new ArraySet(9);
        int iCol, iRow;
        iCol = (cell.getCol()/3)*3;
        iRow = (cell.getRow()/3)*3;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(grid[iRow + i][iCol + j] != 0)
                    nueva.add(grid[iRow + i][iCol + j]);
        cuadros[encuentraCuadro(cell)] = nueva;
    }
    public void insertaNumero(int num, Celda cell){
        grid[cell.getRow()][cell.getCol()]=num;
        checaFila(cell);
        checaColumna(cell);
        checaCuadro(cell);
    }
    public void borraCelda(Celda cell){
        insertaNumero(0, cell);
    }
    public int getVal(int col, int row){
        return grid[row][col];
    }
    public int getVal(Celda cell){
        return getVal(cell.getCol(), cell.getRow());
    }
    private int encuentraCuadro(Celda cell) {
        return 3*(cell.getRow()/3) + cell.getCol()/3;   
    }
    private boolean isFull(){
        boolean resp=true;
        int contR=0;
        int contC=0;
        while(contR < 9 && resp){
            while(contC < 9 && resp){
                if(grid[contR][contC] == 0)
                    resp = false;
                contC++;
            }
            contC = 0;
            contR++;
        }
        return resp;
    }
    public boolean solveSudo(Celda cell, int num){
        boolean resp = false;
        insertaNumero(num, cell);
        if(isFull()) 
            resp = true;
        else{
            Celda nueva = nextEmptySpace();
            if(!numerosPosibles(nueva).isEmpty()){
                for(int num2: numerosPosibles(nueva)){//preueba cada uno de los numeros posibles en la celda
                    resp = solveSudo(nueva, num2);
                    if(resp) break;
                }
                if(!resp)
                    borraCelda(nueva);
            } else 
                resp = false; //si no hay numeros disponibles ha llegado a una no solucion completa
            
            if(!resp)//si al llegar a este punto no encuentra una solucion correcta, retrocede
                borraCelda(cell);
        }
        return resp;
    }
    public boolean solveSudo(){
        return solveSudo(nextEmptySpace(), 0);
    }
    public ArraySet<Integer> numerosPosibles(Celda cell) {
        ArraySet<Integer> resp;
        resp = new ArraySet();
        for(Integer dato:nums)
            resp.add(dato);
        resp = resp.difference(rows[cell.getRow()]);
        resp = resp.difference(columns[cell.getCol()]);
        resp = resp.difference(cuadros[encuentraCuadro(cell)]);
        return resp;
    }
    private Celda nextEmptySpace(){
        boolean found=false;
        Celda resp = null;
        int contR=0;
        int contC=0;
        while(contR < 9 && !found){
            while(contC < 9 && !found){
                if(grid[contR][contC] == 0){
                    resp = new Celda(contR,contC);
                    found = true;
                }else
                    contC++;
            }
            contC = 0;
            contR++;
        }
        return resp;
    }
   
}
