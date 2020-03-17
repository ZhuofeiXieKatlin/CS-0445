import java.util.NoSuchElementException;
/**
 * This abstract data type represents the backend for a streaming radio service.
 * It stores the songs, stations, and users in the system, as well as the
 * ratings that users assign to songs.
 */
public interface StreamingRadio {

	/*
	 * The abstract methods below are declared as void methods with no
	 * parameters. You need to expand each declaration to specify a return type
	 * and parameters, as necessary.
	 *
	 * You also need to include a detailed comment for each abstract method describing
	 * its effect, its return value, any corner cases that the client may need to
	 * consider, any exceptions the method may throw (including a description of the
	 * circumstances under which this will happen), and so on.
	 *
	 * You should include enough details that a client could use this data structure
	 * without ever being surprised or not knowing what will happen, even though they
	 * haven't read the implementation.
	 */

	/**
	 * Adds a new song to the system.
	 * <p>If the song is not in the System, the system still has availiable
	 * positions to put the song into and there is a song that can be added 
	 * to the system, then the song will be added to the System. The system 
	 * will be modified with a new song in it. The method will return true in this situation
	 * If the song cannot be successfully added to the system due to other unexpected situations. 
	 * Then the method will return false. The system will not be modified in this situation. 
	 * <p>If the song is already in the system (either the song has already been added before 
	 * or the song always in the system), it will throws IllegalArgumentException.
	 * Otherwise, if the song is a null, it will throw into the NullPointerException. 
	 * 
	 * @param theSong song that should be added to the system
	 * @return true: if the song is suceddfully added to the list 
	 * false: if the song cannot be added to the list 
	 * @throws NullPointerException if the song is null 
	 * @throws IllegalArgumentException the song has already been added in the system before, the song always in the system 
	 */
	public boolean addSong(Song theSong) 
	throws NullPointerException, IllegalArgumentException;

	/**
	 * Removes an existing song from the system.
	 * <p>If the song is in the system and the song need to be removed 
	 * from the system, then the song will be removed from the system. If the song can be 
	 * removed successfully from the system, then the method will return true. The system will be 
	 * modified due to this situation. 
	 * <p>If the song has already been removed from the system or the song that 
	 * never appear in the system, then this method will throw an IllegalArgumentException. 
	 * If the song is a null, the method will throw into a NullPointerException. If there is no such
	 * an element that can be found in the system, then the method will return NoSuchElementException
	 *
	 * @param theSong the song need to be removed from the system
	 * @return true: if the song is succefully removed from the system
	 *         false: if the song cannot be removed from the system  
	 * @throws NullPointerException if the song is null
	 * @throws IllegalArgumentException if the song has already been removed or never in the system   
	 * @throws NoSuchElementException if there is no such a song that can be found in the method 
	 * 
	 */
	public boolean removeSong(Song theSong) 
	throws NullPointerException, IllegalArgumentException, NoSuchElementException;

	/**
	 * Adds an existing song to the playlist for an existing radio station.
	 * <p>If there is an existing song that not in the playlist for an existing  
	 * radio station (still has enough size), then theSong will be added in to the playlist for the 
	 * existing radio station. 
	 * <p>If there is no such a song and/or radio station, then 
	 * the method will throw an IllegalArgumentException. If the song has already been added
	 * in the station or the song exist in the station from the beginning, then it will also 
	 * throw into the IllegalArgumentException.If the station doesn't have enough size to put
	 * the song into, then it will throws into StationFullException. 
	 * If the song and/or the Station is null, then the method will throw into the NullPointerException.  
	 * 
	 * @param theSong the song need to be added to the existing radio station 
	 * @param theStation the station that can use to add a song into 
	 * @return boolean: true: if the song can be succefully added into 
	 *                  false: if the song cannot be successfully added 
	 * @throws IllegalArgumentException the radio station is not existed, the song 
	 * is not existed, the song has already been added into the station or the song always 
	 * in the station, the station doesn't have enough potition to put the song into 
	 * @throws NullPointerException if the song is null, if the station is null 
	 * @throws StationFullException if the station is full, and the size cannot be changed 
	 */
	public boolean addToStation(Song theSong, Station theStation) 
	throws NullPointerException, IllegalArgumentException, StationFullException;

	/**
	 * Removes a song from the playlist for a radio station.
	 * <p>If there is an existing song that in the playlist for an existing 
	 * song station, then theSong will be removed from the playlist for a radio
	 * station. The method will return true if the song has already been removed 
	 * from the station successfully. 
	 * <p>If there is no such a song (either it has already been removed
	 * or the song is not in the playlist) in a radio station, then 
	 * the method will throw an IllegalArgumentException. If there is no such a station
	 * or the station has been distroied, then the method will throw into
	 * IllegalArgumentException. If the station and/or the song is null, 
	 * then the method will throw into the NullPointerException. If there is no such a song that can
	 * be found in the station, then the method will throw into the NoSuchElementException
	 *
	 * @param theSong: the song need to be removed from the existing radio station 
	 * @param theStation: the station that can use to remove a song into 
	 * @return boolean: true if the song can be succefully removed 
	 *                  false if the song cannot be successfully removed
	  *@throws NullPointerException if the song is null, if the station is null
	 * @throws IllegalArgumentException the radio station is not existed, the song 
	 *                                   is not existed, the song has already been removed
	 *                                   from the station, the song never appear in the 
	 * 									 station 
	 * @throws NoSuchElementException the element that cannot be found in the station
	 */
	public boolean removeFromStation(Song theSong, Station theStation) 
	throws NullPointerException, IllegalArgumentException,NoSuchElementException;

	/**
	 * Sets a user's rating for a song, as a number of stars from 1 to 5.
	 * <p>If there is a song that the user can give a rate to it, then the user's rate for
	 * this song will be stored. The method will return the number that the user give to this song. 
	 * <p>If the song has already been rated by the user before, then 
	 * the new rate number will be covered from the perivous song. If there is no such a song 
	 * that can be rated, then this method will throw into an IllegalArgumentException. If the user give a rate 
	 * out of the bound (1-5), then the method will throw into the IllegalArgumentException. 
	 * If there is no such a user, then the method will throw into an IllegalArgumentException also. If the 
	 * user is null or if the song is null, then the method will throw into the NullPointerException. If there is 
	 * no such a song that can be found, then the method will throws into the NoSuchElementException. 
	 * 
	 * @param theUser the user who will give a rate to a song 
	 * @param theSong the song that a user will give a rate 
	 * @return int： the rate number that given by a user 
	 * @throws NullPointerException if the song is null, if the User is null
	 * @throws IllegalArgumentException the user is not existing, the user give the song 
	 * out of the bound (lower than 1 or higher than 5 )
	 * @throws NoSuchElementException the song cannot be found
	 */
	public int rateSong(User theUser, Song theSong) 
		throws NullPointerException, IllegalArgumentException, NoSuchElementException;
	/**
	 * Clears a user's rating on a song. If this user has rated this song and
	 * the rating has not already been cleared, then the rating is cleared and
	 * the state will appear as if the rating was never made. If there is no
	 * such rating on record (either because this user has not rated this song,
	 * or because the rating has already been cleared), then this method will
	 * throw an IllegalArgumentException.
	 *
	 * @param theUser user whose rating should be cleared
	 * @param theSong song from which the user's rating should be cleared
	 * @throws IllegalArgumentException if the user does not currently have a
	 * rating on record for the song
	 * @throws NullPointerException if either the user or the song is null
	 */
	public void clearRating(User theUser, Song theSong)
		throws IllegalArgumentException, NullPointerException;
	/**
	 * Predicts the rating a user will assign to a song that they have not yet
	 * rated, as a number of stars from 1 to 5.
	 * <p>If there is a song that the user haven't given a rate yet, then the method will 
	 * give a rate that a user might give to this song (a random number in range 1-5). If the user never give rate
	 * to any songs before, then the method will random assign a number for this song
	 * for example, the average rate for the song. 
	 * <p>If there is no such a song or the input user is not invalid, then it will throw into the IllegalArgumentException. If the rate is not 
	 * in the range 1 to 5, then the method will throw into the IllegalArgumentException. If the
	 * song and/or the user is null, then the method will throw into the NullPointerException. 
	 * 
	 * @param theUser the user might give rate to this song
	 * @param theSong the song that will be given a rate 
	 * @throws IllegalArgumentException there is no such a song and/or a user in the playlist, the rate 
	 *                                   is out of bounds. 
	 * @throws NullPointerException if either the user or the song is null
	 * @return int: the rate that a user will give to the song 
	 */
	public int predictRating(User theUser, Song theSong) throws NullPointerException, IllegalArgumentException;

	/**
	 * Suggests a song that exist in the system for a user that they are predicted to like.
	 * <p>According to the user's perference and listening records. The method will give the song that 
	 * the user might want to listen to. For example, suggesting song for the user with his favoriate singer
	 * or style. 
	 * <p>If the input user is not invalid, then the method will throw 
	 * to an IllegalArgumentException. If the user is a null, then the method will throw into 
	 * the NullPointerException. 
	 * 
	 * @param theUser the user that the method will give recommonded song to 
	 * @return Song: a song that will recommonded to the user 
	 * @throws NullPointerException the user is null
	 * @throws IllegalArgumentException The user cannot be found 
	 */
	public Song suggestSong(User theUser)throws NullPointerException, IllegalArgumentException;
}