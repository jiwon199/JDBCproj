 package dbproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
public class dbproject {
	public static void main(String args[]) {
		String userID = "testuser";
		String userPW = "testpw";
		String dbName = "dbprj";
		String url =  "jdbc:mariadb://localhost:3306/" + dbName;
		Scanner sc=new Scanner(System.in);
		
		Connection myConn = null;
		Statement myState = null;
		ResultSet myResSet = null;
		
		 
		 

		try {
			
			/*
			 * Class.forName("com.mysql.jdbc.Driver");
			 * This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'.
			 * The driver is automatically registered via the SPI and manual loading of the driver class 
			  is generally unnecessary.
			 * 
			 */
			
			myConn = DriverManager.getConnection(url, userID, userPW);
			System.out.println("... Connected to databse " + dbName + " in MySQL with " + myConn.toString() + " ...");
			
			myState = myConn.createStatement();
			 
			 
			while(true){
				System.out.println("1.show all. 2.insert 3.delete 4.update 5.search 6.finish");
				int num; //user input.
				num=sc.nextInt();
				// requirements 16. if input is 6, escape the loop.
				if(num==6) break;
				// requirements 15.print all table
				else if(num==1) {
					 //print Artist
					 myResSet=myState.executeQuery("select * from Artist");				 
					 System.out.println("Artist");
					 while(myResSet.next()) { //read table 
						 String name=myResSet.getString("name");
						 int age=myResSet.getInt("age");
						 String belongGroup=myResSet.getString("belongGroup");
						 System.out.println(String.format("Name: %15s | age: %15d | belongGroup: %5s", name, age, belongGroup));
						 System.out.println();
					 }
					 //print Album 
					 myResSet=myState.executeQuery("select * from Album");
					 System.out.println("Album");
					 while(myResSet.next()) {
						 String title=myResSet.getString("albumTitle");
						 String artist=myResSet.getString("artist");
						 int year=myResSet.getInt("releaseYear");						 
						 System.out.println(String.format("albumTitle: %25s | artist: %15s | releaseYear: %7d", title, artist, year));
						 System.out.println();
					 }
					 //print Song 
					 myResSet=myState.executeQuery("select * from Song");
					 System.out.println("Song");
					 while(myResSet.next()) {
						 String title=myResSet.getString("songTitle");
						 float songLength=myResSet.getFloat("songLength");
						 String album=myResSet.getString("albumTitle");						 
						 System.out.println(String.format("songTitle: %22s | album: %23s | Length(m): %5.2f", title, album, songLength));
						 System.out.println();
					 }
				}
				//insert // requirements 8
				else if(num==2) {
					 						 
						System.out.println("insert into Song.which album include new song? enter the number");
						myResSet=myState.executeQuery("select albumTitle from Album");
						int indexnum=1;
						while(myResSet.next()) {
							String title=myResSet.getString("albumTitle");
							System.out.print(indexnum+" : "+title+" ");
							indexnum++;
						}						 
						int albumnum=sc.nextInt(); //user input=select album to include new song
						indexnum=1;
						//init sql
						myResSet=myState.executeQuery("select albumTitle from Album");
						//Find the index's album and save it in string albumTitle.
						while(myResSet.next()) {							 
							String albumTitle=myResSet.getString("albumTitle");
							if(albumnum==indexnum) {
								int checknum=1; //if 0, marked group's album. 
								//When the right album is released, the user enters the rest of the information.
								System.out.println("enter the Song title");
								String songTitle=sc.next();
								System.out.println("enter the Song length");
								float songLength=sc.nextFloat();
								 
								//select belongGroup from Artist where name = (select artist frmo Album where albumTitle=?)
								  
								PreparedStatement check=myConn.prepareStatement("select belongGroup from Artist where name = (select artist from Album where albumTitle=?)");
								check.setString(1, albumTitle);
								ResultSet rs=check.executeQuery();
								while(rs.next()) {
									String group=rs.getString("belongGroup");
									if(group.substring(0,1).equals("¢¾")) checknum=0;
									
								}
								//use prepareStatement, insert info into database
								PreparedStatement psmt=myConn.prepareStatement("insert into Song values(?,?,?)");
								if(checknum==1) psmt.setString(1, songTitle);
								else psmt.setString(1, "¢¾"+songTitle);
							 
								psmt.setFloat(2, songLength);
								psmt.setString(3, albumTitle);
								psmt.executeUpdate(); 
								break;
							}
							indexnum++;
						}							 
					 
				}
				//delete. requirements 11
				else if(num==3) {
					System.out.println("delete from Song. which one to delete? enter the number");
					myResSet=myState.executeQuery("select songTitle from Song");
					int indexnum=1;
					while(myResSet.next()) {
						String title=myResSet.getString("songTitle");
						System.out.println(indexnum+" : "+title+" ");
						indexnum++;
					}		
					int albumnum=sc.nextInt(); //user input number of song which want to delete
					indexnum=1;
					myResSet=myState.executeQuery("select songTitle from Song");
					//search that song and delete. same with insert menu
					while(myResSet.next()) {							 
						String songTitle=myResSet.getString("songTitle");
						if(albumnum==indexnum) {							 
					 
							PreparedStatement psmt=myConn.prepareStatement("delete from Song where songTitle = ?");
							psmt.setString(1, songTitle);
							psmt.executeUpdate(); 
							break;
						}
						indexnum++;
					}			
				}
				//update. 9,10 requirements
				else if(num==4) {
					 System.out.println("1.song update 2.make mark");
					 int selectUpdate=sc.nextInt();
					 //9 requirements
					 if(selectUpdate==1) {
						 //show list of Song. user select song which want to update
						 System.out.println("update from Song. which one to update? enter the number");
							myResSet=myState.executeQuery("select songTitle from Song");
							int indexnum=1;
							while(myResSet.next()) {
								String title=myResSet.getString("songTitle");
								System.out.println(indexnum+" : "+title+" ");
								indexnum++;
							}		
							int songnum=sc.nextInt(); 
							indexnum=1;
							myResSet=myState.executeQuery("select songTitle from Song");
							//user input=new song title
							System.out.println("enter new song title(spacing is done by '-')");
							String newSongtitle=sc.next();
							while(myResSet.next()) {							 
								String songTitle=myResSet.getString("songTitle");
								//update the selected song title
								if(songnum==indexnum) {							 
									//myState.executeUpdate("update Song set SongTitle = '"+newSongtitle+"' where songTitle = '"+songTitle+"'");
									PreparedStatement psmt=myConn.prepareStatement("update Song set SongTitle = ? where songTitle = ?");
									//If it's a song by a group that has been marked, put a heart on the new title.
									if(songTitle.substring(0,1).equals("¢¾")) psmt.setString(1, "¢¾"+newSongtitle);
									else psmt.setString(1, newSongtitle);
									 
									psmt.setString(2, songTitle);
									psmt.executeUpdate(); 
									break;
								}
								indexnum++;
							}			
					 }
			 
			 //10 requirement
			  else {
				  
				 
				// put a heart on the name of my favorite group and the song made by the group member.
				 //Other menus receive user input through numbers, so adding hearts does not interfere with search and update
				System.out.println("you can mark ¢¾ on your favorite group and the song of your favorite group");
				System.out.println("which is your favorite group? enter the number");
				myResSet=myState.executeQuery("select belongGroup from Artist");
				int indexnum=1;
				//show group list 
				while(myResSet.next()) {
					String group=myResSet.getString("belongGroup");
					System.out.println(indexnum+" : "+group+" ");
					indexnum++;
				}	
				int groupnum=sc.nextInt();
				indexnum=1; 
				myResSet=myState.executeQuery("select belongGroup from Artist");
				while(myResSet.next()) {
					String group=myResSet.getString("belongGroup");
					if(indexnum==groupnum) {
						//transaction part
						try {
							myConn.setAutoCommit(false);  
							//do my work in here!
							 
							//If the first letter of the group starts with a heart, it's already marked. break.
							if(group.substring(0,1).equals("¢¾")) {
								 
								break;
							}
							else {
								PreparedStatement psmt1=myConn.prepareStatement("select songTitle from Song natural join Album where artist in (select name from Artist where belongGroup= ? )");
								psmt1.setString(1, group);
								ResultSet rs=psmt1.executeQuery();
								//Attach hearts to songs that belong to a group
								while(rs.next()) {
								 
									String song=rs.getString("songTitle");
									PreparedStatement psmt=myConn.prepareStatement("update Song set songTitle= ? where songTitle= ?");
									psmt.setString(1,"¢¾"+song);
									psmt.setString(2,song);
									psmt.executeUpdate();
								}
								//Attach heart in group name
								PreparedStatement psmt2=myConn.prepareStatement("update Artist set belongGroup = ? where belongGroup=?");
								psmt2.setString(1, "¢¾"+group);
								psmt2.setString(2, group);
								psmt2.executeUpdate();
								
					 
							}
								 
							myConn.commit(); 
						}catch(SQLException sqle) {
							if(myConn!=null) {
								myConn.rollback(); //when fail,rollback.
								 
							}
						}
					}
					indexnum++;
				}
							 
				 }   
				}
				//search
				else if(num==5) {
					System.out.println("1.search song by album title 2.search group activity 3.search number of songs in the album");
					int searchnum=sc.nextInt();
					// 12 requirements
					if(searchnum==1) {
						//user select album. program show the song of selected album
						System.out.println("which album want to know the list of the song? enter the number.");
						myResSet=myState.executeQuery("select albumTitle from Album");
						int indexnum=1;
						while(myResSet.next()) {
							String title=myResSet.getString("albumTitle");
							System.out.println(indexnum+" : "+title+" ");
							indexnum++;
						}	
						int albumnum=sc.nextInt();
						indexnum=1;
						myResSet=myState.executeQuery("select albumTitle from Album");
						 
					
						while(myResSet.next()) {							 
							String myalbum=myResSet.getString("albumTitle");
							if(albumnum==indexnum) {							 
						 
								PreparedStatement psmt=myConn.prepareStatement("select songTitle from Song where albumTitle=?");
								psmt.setString(1,myalbum);
								ResultSet rs=psmt.executeQuery();
								System.out.println("-song list in "+myalbum+"-");
								//show song list of selected album
								while(rs.next()) {
									String songlist=rs.getString("songTitle");
									System.out.println(songlist);
								}
								break;
							}
							indexnum++;
						}		
						
					}
					//13 requirements
					else if(searchnum==2) {
						 
						 //user select group. program show the artist of that group, his album, and released year of that album
						System.out.println("enter the number of a group that you would like to know about activity history.");
						myResSet=myState.executeQuery("select belongGroup from Artist");
						int indexnum=1;
						//show group list.
						while(myResSet.next()) {
							String group=myResSet.getString("belongGroup");
							System.out.println(indexnum+" : "+group+" ");
							indexnum++;
						}	
						//user select one of the group.
						int artistnum=sc.nextInt();
						indexnum=1;
						myResSet=myState.executeQuery("select belongGroup from Artist");
						while(myResSet.next()) {							 
							String mygroup=myResSet.getString("belongGroup");
							if(artistnum==indexnum) {							 
						        
						        //search album,artist,releasedYear by using nested query and join
								PreparedStatement psmt=myConn.prepareStatement("select distinct albumTitle,artist,releaseYear from Song natural join Album where artist in (select name from Artist where belongGroup=?)");
								psmt.setString(1,mygroup);
								ResultSet rs=psmt.executeQuery();
								System.out.println("-activity of "+mygroup+"-");
								 
								//print song of selected artist
								while(rs.next()) {
									String alb=rs.getString("albumTitle");
									String art=rs.getString("artist");
									int year=rs.getInt("releaseYear");
									System.out.println("the member of "+mygroup+", "+art+" released album "+alb+" in "+year);
								}
								break;
							}
							indexnum++;
						}		
					}
					// 14 requirements
					else {
						//Enter the album you want to know how many songs to include
						 System.out.println("enter the number of album to know the number of songs included.");
						 myResSet=myState.executeQuery("select albumTitle from Album");
							int indexnum=1;
							while(myResSet.next()) {
								String title=myResSet.getString("albumTitle");
								System.out.println(indexnum+" : "+title+" ");
								indexnum++;
							}	
							int albumnum=sc.nextInt();
							indexnum=1;
							//search in view 'mylist'
							myResSet=myState.executeQuery("select * from mylist");
							 
				           			
							while(myResSet.next()) {							 
								String myalbum=myResSet.getString("albumTitle");
								if(albumnum==indexnum) {							 
							 
									PreparedStatement psmt=myConn.prepareStatement("select albumTitle,songNum from mylist where albumTitle=?");
									psmt.setString(1,myalbum);
									ResultSet rs=psmt.executeQuery();
									 
									//print number of song in selected album
									while(rs.next()) {
										String albumTitle=rs.getString("albumTitle");
										int songNum=rs.getInt("songNum");
										System.out.println("the album "+albumTitle+ " has "+songNum+" songs");
									}
									break;
								}
								indexnum++;
							}		
					}
				} 
				 
			  		
		 }
			}catch (SQLException e) { 
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("abnormal end!");
			System.out.println("Please make sure you enter it according to the format.");
		}
		finally {  //disconnecting task
			
			if (myResSet != null) {
				try {
					myResSet.close();
					System.out.println("... Close ResultSet ...");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (myState != null) {
				try {
					myState.close();
					System.out.println("... Close Statement ...");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			 
			if (myConn != null) {
				try {
					myConn.close();
					System.out.println("... Close Connection " + myConn.toString() + " ...");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}