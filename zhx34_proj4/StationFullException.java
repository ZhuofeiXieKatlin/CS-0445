/**
 * An exception that is thrown when a station operation cannot be completed because
 * the station does not have the available capacity. 
 */
public class StationFullException extends Exception {
    public StationFullException() { super(); }
    public StationFullException(String e) { super(e); }
}

