import java.util.*;
import java.math.*;

class User{
	private String name;					
	private String status;
	private int age;						/* Private variable declaration for User class */
	private boolean isDependent;
	private ArrayList parents = new ArrayList<User>();
	private ArrayList friends = new ArrayList<User>();		
	
	User(String name,String status,int age){			/* Constructor to initialize User instance with inputs including name,status,age*/
		this.name = name;
		this.status = status;
		this.age = age;
		if(this.age < 16){
			this.isDependent = true;			/* Conditional statement checking if the age is less than 16, then the user’s profile is dependent on father and mother */
		}	
	}
	
	public String getName() {					/* Getter and Setter methods provide access to the value a variable holds */
									 
		return name;
	}

	public void setName(String name) {				
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isDependent() {
		return isDependent;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}
	
	public ArrayList getParents() {					
		return parents;
	}

	public ArrayList getFriends() {
		return friends;
	}
	
	public void addParents(User user){
		this.parents.add(user);
	}
	
	public void addFriends(User user){
		this.friends.add(user);
	}
	
}

class Driver{											/*The driver class is where user interaction occurs*/
	ArrayList users;
	
	Driver(){											// constructor to initialize list of users
		users  = new ArrayList<User>();
	}
	
	public User createUser(String name,String status,int age){				// Creation and Addition of a person into the network				
		User newUser = new User(name,status,age);							
		users.add(newUser);
		return newUser;														//return user for further use
	}
	
	public void displayUsers(){								/* Information display of the person which includes name, status and age*/
		for(int i=0;i<users.size();i++){
			User temp = (User)users.get(i);
			System.out.println(temp.getName()+" "+temp.getStatus()+" "+temp.getAge());
		}
	}
	
	public User lookUP(String name){							// Select a person by name. Done using a temporary variable and using the compareTo function
		for(int i=0;i<users.size();i++){						
			User temp = (User)users.get(i);
			if(temp.getName().compareTo(name) == 0)
				return temp;
		}
				
		System.out.println("Cannot find user with name: "+name);	// returns null if user is not found
		return null;
				
	}
	
	// Example of polymorphism, two function with same name working differently based on the arguments
	
	public void deleteUser(String name){							/* Deletion of the profile of the selected person*/
		User toDelete = this.lookUP(name);
		
		if(toDelete == null)
			System.out.println("Cannot find user");
		else
			this.deleteUser(toDelete);
	}
	

	public void deleteUser(User user){							// Remove the user instance from the user list
		this.users.remove(user);
	}
}

public class MiniNet {
	
	private User findUser(Scanner scanner, Driver driver, String name){
		User temp = null;
		
		while(true){							// to find user in infinite loop until exited explicitly  
			//driver.displayUsers();
			
			System.out.println("Enter username");
			
			name = scanner.next();
			
			temp = driver.lookUP(name);
			
			if(temp != null)
				break;
			
			System.out.println("Do you want to try again[y/n]");
			
			if(scanner.next().toLowerCase().compareTo("y") != 0)
				break;
		}
		
		return temp;
	}
	
	private int addParents(User temp, Driver driver, Scanner scanner){
		System.out.println("Enter name for father");
		User father = driver.lookUP(scanner.next());	// Lookup for father and mother user in the network
		
		if(father == null)
			return 0;
		
		System.out.println("Enter name for mother");
		User mother = driver.lookUP(scanner.next());
		
		if(mother  == null)								// Return 0 if not found
			return 0;
		
		if(mother.getName() == temp.getName() || father.getName() == temp.getName() || mother.getName() == father.getName()){	// checks if mother and father are to same
			System.out.println("Same users cannot be connected!!");
			return 0;
		}
	
		temp.addParents(mother);	// adding parents to parent list of user
		temp.addParents(father);
		
		mother.addFriends(father);	// connecting mother and father as friends
		father.addFriends(mother);
		
		return 1;
	}

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);	//initializing all required variables
		String name = "", status = "";
		int age = 0;
		User temp = null;
		
		Driver driver = new Driver();				//initializing instances of Driver and MiniNet class
		MiniNet mininet = new MiniNet();
		
		while(true){
			System.out.println( "\n****MAIN MENU****\n"+										/* The driver class displaying a simple menu system*/
								"1 Add a person into the network\n"+ 
								"2 Select a person by name\n"+ 
								"3 Connect two persons in a meaningful way e.g. friend, parent\n"+ 
								"4 Find out whether a person is a direct friend of another person\n"+ 
								"5 Find out the name(s) of a personís child(ren) or the names of the parents\n"+
								"6 Exit");
			
			int choice = scanner.nextInt();
			
			if(choice == 6){							// System will exit if user select choice 6 from the main menu
				System.out.print("Exiting now...");
				break;
			}
					
			switch(choice){
			
				case 1:							/* Adding a person in the network*/
					System.out.println("Enter Name");
					name = scanner.next();
					
					System.out.println("Enter Status");
					status = scanner.next();
					
					System.out.println("Enter Age");
					age = scanner.nextInt();
					
					temp = driver.createUser(name, status, age);
					
					if(age < 16){				// if user's age is less than 16 then parents information has to be provided to proceed further
						System.out.println("User is under age. Please provide name of parents to create user profile.\n\nDo you proceed[y/n]");
						if(scanner.next().toLowerCase().compareTo("y") == 0){
							
								//if parents are not found
							if(mininet.addParents(temp, driver, scanner) != 1){
								System.out.println("Something went wrong with parents addition.. Rolling back on updation");
								driver.deleteUser(temp);
							}
							else
								System.out.println("User created successfully");
						}
						else{// if user doesn't want to continue
							driver.deleteUser(temp);
						}
					}else
						System.out.println("User created successfully");
					
					break;
				case 2:								/* Selecting a person by name */
					temp = mininet.findUser(scanner, driver, name);
					
					if(temp != null){				// Sub menu of operations can be done for selected user
						System.out.println("\t1.)Display the profile of the selected person\n"+
								   "\t2.)Update the profile information of the selected person\n"+  
								   "\t3.)Delete the selected person");
				
						int subChoice = scanner.nextInt();
						
						switch(subChoice){
						case 1:
							System.out.println(temp.getName()+" "+temp.getStatus()+" "+temp.getAge());	//Profile display
							break;
							
						case 2:										//Taking all values again for profile updation
							System.out.println("Enter Name");
							name = scanner.next();
							
							System.out.println("Enter Status");
							status = scanner.next();
							
							System.out.println("Enter Age");
							age = scanner.nextInt();
							
							if(temp.getAge()>16 && age < 16){		// It will check if the new age is less than 16 and needs to be treated as dependent from now
								System.out.println("New age is less than 16!! Please provide details for user's parents");
								if(mininet.addParents(temp, driver, scanner) != 1){
									System.out.println("Something went wrong with parents addition.. Rolling back on updation");
								}
								else{
									temp.setDependent(true);
									System.out.println("User updated successfully");
								}
							}
							else{
								if(temp.getAge()<16){ //update status of user to dependent
									temp.setDependent(false);
								}
								temp.setName(name);
								temp.setAge(age);
								temp.setStatus(status);
								
								System.out.println("User updated successfully");
							}
							break;
							
						case 3:								//Profile deletion
							driver.deleteUser(temp);
							System.out.println("User Succesfully removed");
							break;
							
						default:
							System.out.println("Invalid Option. Going to main menu");
						}
					}
					break;
				case 3:									//Connecting two people in a meaningful way
					temp = mininet.findUser(scanner, driver, name);			//Either parents or friends
					
					if(temp != null){
						System.out.println("\tDo you want to add\n"
								+ "\t\t1.)Friends\n"
								+ "\t\t2.)Parents");
						
						int subChoice = scanner.nextInt();
						
						switch(subChoice){
						case 1:							//Connecting two friends
							System.out.println("Please enter username for friend");
							User friend = mininet.findUser(scanner, driver, name);
							if(friend != null){
								
								if(temp.getName() == friend.getName())
									System.out.println("Same users cannot get connected");
								else if(temp.getAge()<=2 || friend.getAge()<=2){
									System.out.println("User with age less than 2 cannot have friends");
								}
								else if(temp.getAge()<16 && friend.getAge()<16 && Math.abs(temp.getAge() - friend.getAge())>3){
									System.out.println("The age difference between these two young friends cannot be more than 3 years");
								}
								else if(temp.getAge()<16 && temp.getFriends().size()>0 ||  friend.getAge()<16 && friend.getFriends().size()>0){
									System.out.println("Under age user cannot get connected with more one dependent user");
								}
								else{
									temp.addFriends(friend);
									friend.addFriends(temp);
									System.out.println("Users successfully connected as friend");
								}
								
							}
							else{
								System.out.println("Cannot find friend!! Going back to main menu");
							}
							break;
						case 2:							//Connecting parents, i.e mother and father
							if(mininet.addParents(temp, driver, scanner) != 1){
								System.out.println("Something went wrong with parents addition!! Going back to main menu");
							}
							else{
								temp.setDependent(true);
								System.out.println("User connections added successfully");
							}
							
							break;
						default:
							System.out.println("Invalid Option. Going to main menu");
						}
					}
					
					break;
				case 4:								//Finding out whether a person is a direct friend of another person
					System.out.println("Enter name of person1");
					name = scanner.next();
					User person1 = driver.lookUP(name);
					
					if(person1 == null){
						System.out.println("Cannot find!! exiting...");
						break;
					}
					
					System.out.println("Enter name of person2");
					name = scanner.next();
					User person2 = driver.lookUP(name);
					
					if(person2 == null){
						System.out.println("Cannot find!! exiting...");
						break;
					}
					
					if(person1.getFriends().contains(person2))			//Checking if person1 is contained in the friends list of person2 
						System.out.println("Yes both are direct friends");
					else
						System.out.println("No they are not connected directly");
					
					break;
				case 5:									//Finding out the name(s) of a person’s parents
					temp = mininet.findUser(scanner, driver, name);
					
					if(temp != null){
						if(temp.getParents().size() == 0)
							System.out.println("No parents of this user in the system\n");
						else{
							System.out.println("Parents name as follows");
							
							for(int i=0;i<temp.getParents().size();i++){
								User temp2 = (User)temp.getParents().get(i);
								System.out.println(temp2.getName()+" "+temp2.getStatus()+" "+temp2.getAge());
							}
						}
					}
					break;
				
				
				default:			
					System.out.println("Invalid option");
			
			}
			
		}

	}

}
