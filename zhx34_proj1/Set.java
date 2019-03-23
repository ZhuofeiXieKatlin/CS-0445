// Zhuofei Xie
// Also include any notes to the grader here, if anything is not working, or
// commented out, etc.

import java.util.Arrays;

public class Set<E> implements SetInterface<E> {
    E[] movieSet;
    int size;
    private static final int Defaultsize = 100;

    public Set(){
        this(Defaultsize);
        // the reasonable size in the array
    }

    public Set(int entry) throws IllegalArgumentException{
        if(entry <0) {
            throw new IllegalArgumentException("The entry is illegal.");
        }
        movieSet = (E[]) new Object[entry];
        // movieSet = (E[]) new Object(entry);
        this.size = entry;
    }

    public Set(E[] Entry){
        this(Entry.length);
        int newlength = movieSet.length;
        if(Entry.length >= movieSet.length) {
            newlength = movieSet.length * 2;
        }
        movieSet = Arrays.copyOf(Entry, newlength);
        size =   Entry.length;
    }

    @Override
    public int getSize() {
        return size;
        // return movieSet.length;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean add(E newEntry) throws SetFullException, NullPointerException {

        if(newEntry == null){
            throw new NullPointerException("null newEntry");
        }else if(size == movieSet.length){
            // if the movie set is full and has no space
            throw new SetFullException("The set is full");
        }

        for(int i=0; i<movieSet.length;  i++){
            if(newEntry.equals(movieSet[i])){
                return false;
            }
        }

        movieSet[size] = newEntry;
        size ++;
        return true;

    }

    @Override
    public E remove(E entry) throws NullPointerException {
        if(entry==null){
            throw new NullPointerException("Null entry");
        }
        for(int i=0; i<movieSet.length;i++){
            if(entry.equals(movieSet[i])){
                movieSet[i]=movieSet[size-1];
                movieSet[size-1]= null;
                size --;
                return entry;
            }
        }
        return null;
    }

    @Override
    public E remove() {
        if(isEmpty()) {
            return null;
        }
        return this.remove(movieSet[0]);
    }

    @Override
    public void clear() {
        for(int i=0; i<movieSet.length; i++){
            movieSet[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if(entry == null){
            throw new NullPointerException("The entry is null");
        }

        for(int i=0; i<movieSet.length;i++){
            if(entry .equals(movieSet[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(movieSet,size);
    }
}