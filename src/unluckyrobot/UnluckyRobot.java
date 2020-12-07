package unluckyrobot;

import java.util.Random;
import java.util.Scanner;

/**
 * About the Unlucky Robot Program: 
 *      A robot walks into a 5*5 grid where its starting point is (X = 0, y = 0), 
 *  it will run a maximum number of 20 times. The initial total score is 300.
 *  Each time, the program informs user the current value of current x,y, iteration, 
 *  and total score. The program will ask user to enter a direction: 
 *  ('u' for up, 'd' for down, 'l' for left, 'r' for right). Each move will add
 *  1 to the coordinate (x or y) towards that direction. The user will lose 10 points
 *  for choosing up and lose 50 points for choosing other directions. A reduce 
 *  of 2000 points if the robot exits the grid. It will toss a 6 dimensions-dice 
 *  to determine a reward(negative or positive). If the entered direction is 'u' and the 
 *  reward is negative, flip a coin. If head, the reward is not applied. Everything else,
 *  the reward is applied. 
 *  The program will end if:
 *      1) the number of steps/iterations exceeds 20 where 0 is the first step 
 *      2) the total score falls under -1000
 *      3) the total score exceeds 2000
 *      4) the robot reaches one of the ends locations (x = 4, y = 4) or (x = 4, y = 0)
 *  After one of the conditions above is satisfied, the program will ask for the user name. 
 *  If the user won the game ( total score >= 2000), 
 *      print victory message + user name + total score 
 *  Else, print mission-failed message + user name + total score 
 * 
 * @author Hong Hien Pham
 */
public class UnluckyRobot {
    public static void main(String[] args) {        
        Scanner console = new Scanner(System.in);
        
        int x = 0;// the x coordinate of the robot 
        int y = 0;// the y coordinate of the robot 
        char direction; 
        int itrCount;// the number of iterations 
        int totalScore = 300;// the totalscore obtained 
        int reward;
        
        for (itrCount = 0; itrCount < 20; itrCount++) {
            
            // Showing the current value/number of current x, y, iteration, and total score 
            displayInfo(x, y, itrCount, totalScore); 
            
            /* Asking a user to input a direction and store to direction:
                'u' for up
                'd' for down
                'l' for left
                'r' for right
            */
            direction = inputDirection();       

            switch (direction) {                
                case 'u':// if entered up
                    y += 1;// add 1 to value of the current y  
                    totalScore -= 10;// score spent: 10
                    if (doesExceed(x, y, direction))// if y > 4
                    {
                        y--;// make the y's value as the one of the previous iteration
                        totalScore -= 2000;//penalize -2000 points 
                        System.out.println("Exceed boundary, -2000 damage applied");
                    }
                    break;
                case 'd':// if entered down
                    y -= 1;// minus 1 from the value of the current y
                    totalScore -= 50;//score spent: 50
                    if (doesExceed(x, y, direction))// if y < 0
                    {
                        y++;// make the y's value as the one of the previous iteration
                        totalScore -= 2000;//penalize -2000 points 
                        System.out.println("Exceed boundary, -2000 damage applied");                        
                    }
                    break;
                case 'l':// if entered left
                    x -= 1;// minus 1 from the current value of x
                    totalScore -= 50;// score spent: 50  
                    if (doesExceed(x, y, direction))// if x < 0
                    {
                        x++;// make the x's value as the one of the previous iteration
                        totalScore -= 2000;//penalize -2000 points 
                        System.out.println("Exceed boundary, -2000 damage applied");                        
                    }    
                    break;
                default://if entered right
                    x += 1;;//add 1 to the current value of x
                    totalScore -= 50;//score spent: 50
                    if (doesExceed(x, y, direction))//if x < 0
                    {
                        x--;// make the x's value as the one of the previous iteration
                        totalScore -= 2000;//penalize -2000 points 
                        System.out.println("Exceed boundary, -2000 damage applied");                        
                    }
            }// end of switch (direction) starting at line 54
                   
            // roll a dice, decide the value reward/punishment, and store to reward  
            reward = reward(); 
            
            /* if 'u' as direction and reward < 0,
                  if toss a coin if head, apply reward
                  if tail, reward = 0
               if not 'u', apply the reward 
            */  
            reward = punishOrMercy(direction, reward);            
            
            // add reward's value to total score
            totalScore += reward;
            
            System.out.println("");// space            
            
            /* The game is over if:
                1) the number of the iteration is greater than or equal to 20 times
                2) the total score is less than -1000
                3) the total score is greater than or equal to 2000
                4) reach one of the end states: (x = 4 and y = 0) or (x = 4 and y = 4)
            */
            if (isGameOver(x, y, totalScore, itrCount))
                break;// if the game is over, get out the loop
        }// end of for loop starting at line 41  
        
        System.out.print("Please enter your name (only two words): ");
        String str = console.nextLine();// store entered name of the user to str 
        
        str = toTitleCase(str);//convert entered name to title case 
        
        // verify the current score to decide if win or lose 
        evaluation(totalScore, str);

        console.close(); 
    }
    
    /**
     * Print a message on the screen reporting the current x and y coordinate of
     * the robot, the total score, and the number of iterations made 
     * @param x the current x coordinate of the robot
     * @param y the current y coordinate of the robot
     * @param itrCount the number of iterations 
     * @param totalScore the current score
     */
    public static void displayInfo(int x, int y, int itrCount, int totalScore) {
        System.out.printf("For point (X=%d, Y=%d) at iteration: %d the total score is: %d\n",
                x, y, itrCount, totalScore);     
    }
    
    /**
     * Verify if the robot exceed the grids limit after taking a step toward a given direction
     * @param x the x coordinate after taking a step toward the given direction 
     * @param y the coordinate after taking a step toward the given direction
     * @param direction the entered direction
     * @return true if the robot exceeds the grid limits, and false, if not
     */
    public static boolean doesExceed(int x, int y, char direction) {
        switch (direction) {
            case 'u':
                return y > 4; 
            case 'd':
                return y < 0; 
            case 'l':
                return x < 0;
            default:
                return x > 4;   
        }
    }
    
    /**
     * Roll a dice and calculate the reward/punishment of that move
     * @return a number as the reward or the punishment of entering that cell
     */
    public static int reward() {
        Random rand = new Random();
        int dice = rand.nextInt(6) + 1;// generate number betwen [1, 6]
        
        switch (dice) {
            case 1:
                System.out.println("Dice: 1, reward: -100");
                return -100;
            case 2:
                System.out.println("Dice: 2, reward: -200"); 
                return -200;
            case 3:
                System.out.println("Dice: 3, reward: -300");                
                return -300;
            case 4:
                System.out.println("Dice: 4, reward: 300");               
                return 300;
            case 5:
                System.out.println("Dice: 5, reward: 400");               
                return 400;
            default:
                System.out.println("Dice: 6, reward: 600");               
                return 600;
        }
    }   
    
    /**
     * Verify the sign of the reward value and the direction to decide 
     * if the punishment should be applied or removed
     * @param direction the given direction
     * @param reward the calculated reward
     * @return if applied, return reward, else, return 0
     */
    public static int punishOrMercy(char direction, int reward) {
        Random rand1 = new Random();
        /* generate number betwen [0, 1] like flip a coint to choose 0 or 1      
            where 0 is tail and 1 is head 
        */   
        int generatedNumCoin = rand1.nextInt(2);
        
        // if the entered direction is 'u' and reward is a negative number 
        if (direction == 'u' && reward < 0)
            // if tail 
            if (generatedNumCoin == 0) {
                System.out.println("Coin: tail | Mercy, the negative reward is removed.");
                return 0;// reward = 0
            }
            // if head 
            else {
                System.out.println("Coin: head | No mercy, the negative reward is applied.");
                return reward;// the reward is applied 
            }
        else// if direction is not 'u' or if reward is positive number 
            return reward;// the reward is applied
    }
    
    /**
     * Bring a string(user's full name)to title case assuming:
     *      1) the given string contains only 2 words
     *      2) the 2 words of the given string are separated by a space 
     * @param str the given name
     * @return the given string to title case
     */
    public static String toTitleCase(String str) {
        int spaceIdx = str.indexOf(" ");
        
        String firstName = Character.toTitleCase(str.charAt(0)) 
                + str.substring(1, spaceIdx + 1).toLowerCase();    
        String lastName = Character.toTitleCase(str.charAt(spaceIdx + 1)) 
                + str.substring(spaceIdx + 2).toLowerCase();
        
        return firstName + lastName;        
    }
    
    /**
     * Take the given value of the game total score and the user's name
     * and print the corresponding evaluation
     * @param totalScore the given total score
     * @param str the given name
     */
    public static void evaluation(int totalScore, String str) {   
        if (totalScore >= 2000)
            System.out.printf("Victory, %s, your score is %d\n", str, totalScore);
        else 
            System.out.printf("Mission failed, %s, your score is %d\n", str, totalScore);          
    }
    
    /**
     * Ask the user to enter a direction letter ('u' for up, 'd' for down, 'l' for left, 'r' for right)
     * @return the entered direction
     */    
    public static char inputDirection() {
        Scanner console = new Scanner(System.in);
        char direction;
        do {
            System.out.print("Please input a valid direction: ");
            direction = console.next().charAt(0);
            direction = Character.toLowerCase(direction);
        } while (direction != 'u' && direction != 'd' && direction != 'l' && direction != 'r');
        
        return direction;
    }
    
    /**
     * Verify if the game is over based on theses conditions:
     *      1) Robot reached one of the end states: x = 4 and x = 0
     *                                              x = 4 and y = 4
     *      2) The total number of iterations exceeds 20 times
     *      3) The total score is less then -1000
     *      4) The total score is greater or equal to 2000 
     * @param x the current x coordinate of the robot
     * @param y the current y coordinate of the robot
     * @param totalScore the current total score of the game
     * @param itrCount the current number of iterations
     * @return true, if one of the condition is satisfied, false, if not  
     */
    public static boolean isGameOver(int x, int y, int totalScore, int itrCount) {
        return x == 4 && y == 0 || y == 4 && x == 4 || itrCount >= 19 
                || totalScore >= 2000 || totalScore < -1000;
    }
}

