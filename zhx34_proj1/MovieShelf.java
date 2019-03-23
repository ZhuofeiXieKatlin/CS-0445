// Zhuofei Xie
// Also include any notes to the grader here, if anything is not working, or
// commented out, etc.

public class MovieShelf implements MovieShelfInterface{
    // this is composition of the Movie
    //private Movie theMovie;
    // movie [0.... size-1] are the items currently in the set
    //Set<E> MovieShelfItem = new MovieShelf<E>();

    Set<Movie> MovieArray;
    // size is how many items are in the list

    public MovieShelf(){
        MovieArray = new Set<>();
    }

    public MovieShelf(Movie[] MovieList){
        MovieArray = new Set<>(MovieList);
    }


    @Override
    public boolean addItem(Movie item){
        for(int i=0;i<MovieArray.getSize();i++) {
            if (MovieArray.contains(item)) {
                return false;
            }
        }
        if(true){
            try{
                MovieArray.add(item);
            }
            catch(SetFullException e){
                e.printStackTrace();
            }
        }
        return true;

    }

    @Override
    public boolean removeItem(Movie item) {
        if(MovieArray.getSize()==0){
            return false;
        }
        for(int i=0;i<MovieArray.getSize();i++){
            if(MovieArray.contains(item)){
                MovieArray.remove(item);
                return true;
            }
        }
        return false;
    }

    @Override
    public int watchMovie(Movie item) {
        for(int i=0;i<MovieArray.getSize();i++){
            if(MovieArray.contains(item)){
                item.watch();
                return item.getWatchCount();
            }
        }
        return -1;
    }

    @Override
    public void printAll() {
        Object[] array =  MovieArray.toArray();// SHOULD I DECLRE Object[] instead of Movie[]??? YES

        for (int i =0; i < array.length; i++)
        {
            Movie each = (Movie)array[i];
            if(each != null) {
                System.out.printf("    %-40s", each);
                System.out.printf("(rating: %d/5; watched %d times)\n", each.getRating(), each.getWatchCount());
            }
        }
    }
    @Override
    public boolean borrowMovie(MovieShelfInterface other, Movie item) {
        if(other.removeItem(item)){
            boolean inThisShelf = this.addItem(item);
            return inThisShelf;
        }
        return false;
    }
}