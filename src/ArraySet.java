/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author agomezdg
 * @param <T>
 */
public class ArraySet<T> implements SetADT<T> {
    private static final int DEFAULT_CAPACITY=100;
    private static Random rand=new Random();
    private static final int NOT_FOUND=-1;
    private int count;  // the current number of elements in the set
    private T[] contents;
    

    public ArraySet(int initialCapacity) {
        count=0;
        contents=(T[])(new Object[initialCapacity]);
    }

    public ArraySet() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(T element) {
        if(!(contains(element))) {
            if(size()==contents.length)
                expandCapacity();
            contents[count]=element;
            count++;
        }
    }

    private void expandCapacity() {
        T[] larger=(T[])(new Object[contents.length*2]);
        System.arraycopy(contents, 0, larger, 0, contents.length);
        contents=larger;
    }

    @Override
    public boolean contains(T target) {
        return searchTarget(target)!=NOT_FOUND;
    }

    private int searchTarget(T target) {
        int search=NOT_FOUND;
        int index=0;
        while(index<count&&search==NOT_FOUND) {
            if(contents[index].equals(target))
                search=index;
            index++;
        }
        return search;
    }
    private void borraDato(int pos){
        for(int i = pos; i < count-1; i++)
            this.contents[i]= contents[i+1];
        contents[count-1] = null;
        count--;
    }
    @Override
    public T removeRandom() {
        if(isEmpty())
            throw new EmptyCollectionException();
        else {
            int choice=rand.nextInt(count);
            T result=contents[choice];
            /*contents[choice]=contents[count-1];  // fill the gap
            contents[count-1]=null;
            count--;
            */
            borraDato(choice);
            return result;
        }
    }

    /**
     *
     * @param target
     * @return
     */
    @Override
    public T remove(T target) {
        if(isEmpty())
            throw new EmptyCollectionException();
        else {
            int search=searchTarget(target);
            if(search==NOT_FOUND)
                throw new NoSuchElementException();
            else {
                T result=contents[search];/*
                contents[search]=contents[count-1];
                contents[count-1]=null;
                count--;*/
                borraDato(search);
                return result;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void addAll(SetADT<T> set) {
        for(T element:set)
            add(element);
    }

    @Override
    public boolean equals(SetADT<T> set) {
        boolean result=false;
        int countEquals=0;
        
        if(size()==set.size()) {
            for(T element:set)
                if(contains(element))
                    countEquals++;
            result=countEquals==size();
        }
        
        return result;
    }

    // Podría haberse escrito de una manera un poco más eficiente así:
/*    public boolean equals(SetADT<T> set) {
        boolean result = false;
        Iterator<T> it=set.iterator();

        if(size()==set.size()) {
            while(it.hasNext()&&contains(it.next()));
            if(!it.hasNext())
                result=true;
        }

        return result;
    }*/
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(contents, count);
    }

    @Override
    public String toString() {
    	StringBuilder result=new StringBuilder();
    	for(int index=0;index<count;index++)
            result.append(contents[index].toString()).append(" ");
    	
        return result.toString();
    }

    /**
     *
     * @param set
     * @return
     */
    public ArraySet<T> intersection(SetADT<T> set) {
        ArraySet<T> resp = new ArraySet();
        for(T dato:this)
            if(set.contains(dato))
                resp.add(dato);
        return resp;
    }
    
    public ArraySet<T> difference(SetADT<T> set){
        ArraySet<T> resp;
        resp = new ArraySet(this.size());
        
        for(T dato:this)
            resp.add(dato);
        
        if(set!=null)
            for(T dato2:this)
                if(set.contains(dato2))
                    resp.remove(dato2);
        
        return resp;
    }
    private ArraySet<T> cloneSet(){
        ArraySet<T> resp = new ArraySet();
        for(T dato:this)
            resp.add(dato);
        return resp;
    }
}
