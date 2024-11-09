import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Uno {
	
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static Random rand = new Random();
	private static Card topOnDeck;
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to Uno Banana Edition (TM)! v0.2");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		WL: while(true) {
			
			if(players.size() < 2) {
				System.out.println("Enter your name here -> ");
				String temp = sc.nextLine();
				players.add(new Player(temp));
			}
			else {
				System.out.println("Enter your name here, or if all players are entered, type 'END' -> ");
				String temp = sc.nextLine();
				if(temp.toUpperCase().equals("END")) {
					break WL;
				}
				else {
					players.add(new Player(temp));
				}
			}
		}
		
		// Players loaded...
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----------------------");
		StringBuilder bob = new StringBuilder();
		bob.append("Welcome ");
		for(int i = 0; i<players.size()-1; i++) {bob.append(players.get(i).getName() + ", ");}bob.append("and "+players.get(players.size()-1).getName()+"!");
		System.out.println(bob.toString());
		
		System.out.println();
		
		// Randomizing top cards and giving random cards to the players...
		
		if(rand.nextInt(100) < 30) {
			topOnDeck = new Card(Card.colors[rand.nextInt(Card.colors.length)], Card.properties[rand.nextInt(Card.properties.length)], Card.nums[rand.nextInt(10)]);
		}
		else {
			topOnDeck = new Card(Card.colors[rand.nextInt(Card.colors.length)], Card.nums[rand.nextInt(Card.nums.length)]);
		}
		
		for(Player p : players) {
			p.drawCard();
			p.drawCard();
			p.drawCard();
			p.drawCard();
		}
		
		////////// BELOW IS FOR DEBUGGING PURPOSES
		
		/*System.out.println("TOP CARD ON DECK:" + topOnDeck.printCard());
		
		for(Player p : players) {
			System.out.println(p.getName() +" -->  " +p.printAllCards());
		}*/
		
		////////// ABOVE IS FOR DEBUGGING PURPOSES
		
		while(true) {
			
			for(int i = 0; i < players.size(); i++) {
				
				System.out.println("THE TOP CARD IS..." + topOnDeck.printCard());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Player " + players.get(i).getName() + " ... here are your cards: " + players.get(i).printAllCards());
				
				
				
				// GOING INTO "playCard" METHOD
				
				if(doesPlayerHaveAnyCardsThatMatchTopCard(topOnDeck, players.get(i))) {
					Card cardToPlay;
					do {

						cardToPlay = getPlayerSelectedCard(sc, i);
					}while(cardToPlay == null);
				}else {
					System.err.println("NO CARDS MATCH: AUTO DRAW ACTIVATED");
					players.get(i).drawCard();
				}
				
				// GOING INTO "playCard" METHOD
				
				
			}
		}
	}
	
	public static Card getPlayerSelectedCard(Scanner sc, int i) {
		System.out.println("Which one would you like to play?");
		String input = sc.nextLine();
		
		String[] inputArr = input.split(" "); // CHECK LATER <----
		
		for(Card c : players.get(i).getCards()) {

			try {
				
				if(c.isCardDescription(inputArr[0], Integer.parseInt(inputArr[1]))) {
					
					// ENTERED TEXT MATCHES CARD DESCRIPTION
					// STUFF GOES HERE
					System.err.println("Card detected! String, Integer"); //DEBUG
					return c;
					
				}
			}catch(Exception e) {
				// NOTHING GOES HERE
			}
			
			try {
				if(c.isCardDescription(inputArr[0], inputArr[1])) {
					
					// ENTERED TEXT MATCHES CARD DESCRIPTION
					// STUFF GOES HERE
					System.err.println("Card detected! String, String"); //DEBUG
					return c;
					
				}else {
					return null;
				}
			}catch(Exception e) {
				// NOTHING GOES HERE
			}
			
		}
		return null;
	}
	
	public static boolean doesPlayerHaveAnyCardsThatMatchTopCard(Card topCard, Player p) {
		for(Card card : p.getCards()) {
			if(card.matches(topCard)) {
				return true;
			}
		}
		return false;
		
	}
}

class Card{
	
	private final String color;
	private final String property;
	private final int number;
	
	public final static String[] properties = new String[]{"Wild 4 / NO COLOR", "Draw 2", "Draw 2", "Reverse", "Reverse", "Reverse", "Reverse"};
	public final static String[] colors = new String[] {"Red", "Yellow", "Green", "Blue"};
	public final static int[] nums = new int[] {1,2,3,4,5,6,7,8,9};
	
	public Card(String color, String property, int number) {
		super();
		this.color = color;
		this.property = property;
		this.number = number;
	}
	public Card(String color, int number) {
		super();
		this.color = color;
		this.number = number;
		this.property = "none";
	}
	
	public String printCard() {
		if(this.getProperty() == "none") {
			return " " + this.getColor() + " " + this.getNumber() + " ";
		}
		else if(this.getProperty() == "Wild 4 / NO COLOR") {
			return " WILD 4 ";
		}
		else {
			return " " + this.getProperty() + " " + this.getColor() + " "; 
		}
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getProperty() {
		return this.property;
	}
	public int getNumber() {
		return this.number;
	}
	
	
	public boolean matches(Card c) {
		if(this.color == c.getColor() || this.number == c.getNumber() || this.property == c.getProperty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isCardDescription(String color, int number) {
		return (color.equals(this.color)) && (number == this.number);
	}
	public boolean isCardDescription(String property, String color) {
		return (property.equals(this.property)) && (color.equals(this.color));
	}
}

class Player{
	
	private static Random rand = new Random();
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	private String name;
	
	public Player(String n) {
		this.name = n;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void drawCard() {
		
		if(rand.nextInt(100) < 30) {
			cards.add(new Card(Card.colors[rand.nextInt(Card.colors.length)], Card.properties[rand.nextInt(Card.properties.length)], Card.nums[rand.nextInt(9)]));
		}
		else {
			cards.add(new Card(Card.colors[rand.nextInt(Card.colors.length)], Card.nums[rand.nextInt(9)]));
		}
	}
	
	public String printCard(Card c) {
		if(c.getProperty() == "none") {
			return " " + c.getColor() + " " + c.getNumber() + " ";
		}
		else if(c.getProperty() == "Wild 4 / NO COLOR") {
			return " WILD 4 ";
		}
		else {
			return " " + c.getProperty() + " " + c.getColor() + " "; 
		}
	}
	
	public String printAllCards() {
		
		StringBuilder bob = new StringBuilder();
		for(Card c : cards) {
			bob.append(this.printCard(c));
			bob.append("|");
		}
		
		return bob.toString().substring(0, bob.length()-1);
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
}
