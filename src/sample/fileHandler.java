package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


//Define an object 'dish' to hold the attributes
//in this case the name of the dish and the type
class dish
{

    String type;
    String name;

    //This is a constructor, this is for when you want to
    //actually create a dish you can do something like
    //dish my_new_dish = new dish(taco,mexican);
    dish(String type, String name)
    {
        this.type = type;
        this.name = name;
    }
}


public class fileHandler {

    //Here we are creating an array or 'list'
    // the things in <> are the data type its
    //storing. So for example menu is an array
    //of dishes.
    ArrayList<dish> table = new ArrayList();
    ArrayList<dish> menu = new ArrayList();
    ArrayList<String> arr = new ArrayList<>();


    //These be the variables for days and max freq currently initialized
    //to their default values. (this is janky, they shouldnt be hard
    //coded like this but idc I made it and it works)
    int days = 7;


    int maxFreq = 3;


    //returns true for valid or false for invalid
    boolean sanitizeConfigs(int max, String input)
    {
        int num;

        if(input.isEmpty())
        {
            return false;
        }


        if(input.length() > 9)
        {
            return false;
        }

        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }

        num = Integer.parseInt(input);


        if(num > max)
        {
            return false;
        }
        if(num < 1)
        {
            return false;
        }


        return true;
    }


    ArrayList<String> dynamicSearch(String input)
    {
        ArrayList<String> results = new ArrayList<>();

        int len = input.length();

        for(int i=0; i<table.size(); i++)
        {
            if(table.get(i).name.length() >= len)
            {
                if(table.get(i).name.substring(0,len).toUpperCase().compareTo(input.toUpperCase()) == 0)
                {
                    results.add(table.get(i).name);
                }
            }

        }

        return results;
    }



    //Turn all table elements into 1 formatted string to output
    //on the main panel
    String outputTable() {
        String out = "";

        for (int i = 0; i < table.size(); i++) {
            if (i < table.size()) {
                out = out + "Dish " + (i + 1) + "\n-------------------------------------------\n";
            }

            out = out + "Dish: " + table.get(i).name + "\n" + "Category: " + table.get(i).type + "\n-------------------------------------------\n\n";
        }

        return out;
    }


    //Same as outputTable but for the menu
    String outputMenu() {
        String out = "";

        out = "Your " + days + " day menu is:\n\n\n";

        for (int i = 0; i < menu.size(); i++) {
            if (i < menu.size()) {
                out = out + "Dish " + (i + 1) + ":\n~";
            }

            out = out + table.get(i).name + "\n\n";
        }

        return out;
    }


    //Parse text file and initialize table array
    //includes exception handling. Call only once,
    //remove/write methods include re-reading text file
    int readData(String fileName) {
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);


            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String name = line.substring(0, line.indexOf("*"));
                String type = line.substring(line.indexOf("*") + 1);

                table.add(new dish(type, name));
            }


            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            //Error opening File
            return -1;

        } catch (IOException ex) {
            //Error Reading file
            return -2;
        }

        return 1;
    }


    //This method takes the dish from the user and
    //adds it to the text file as well as adds it
    //to the array
    int writeData(String fileName, dish toAdd) {

        try {
            FileWriter fileWriter = new FileWriter(fileName, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(toAdd.name + "*" + toAdd.type);
            bufferedWriter.newLine();
            table.add(toAdd);

            bufferedWriter.close();

        } catch (IOException ex) {

            return -1;
        }

        return 1;
    }


    //returns 2 if dish is not in database
    //returns 1 for successful delete
    //returns -1 for error
    //We delete it from array and text file
    int deleteDish(String fileName, String delName) {

        String name, type;
        int index = findDish(delName);


        if (index == -1) {
            return 2;
        }

        table.remove(table.get(index));


        try {
            FileWriter fileWriter = new FileWriter(fileName);


            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            for (int i = 0; i < table.size(); i++) {
                name = table.get(i).name;
                type = table.get(i).type;

                bufferedWriter.write(name + "*" + type);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException ex) {
            return 2;
        }

        return 1;
    }


    //Fisher Yates Modern shuffle algorithm
    //im not explaining how this works in
    //comments, look it up....
    void shuffleArray() {
        Random random = new Random();

        for (int i = table.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            dish temp = (dish) table.get(index);
            table.set(index, table.get(i));
            table.set(i, temp);
        }
    }


    //Returns true if dish has not been added to menu
    //more then maxFreq times else false
    boolean canUseMeal(String type) {
        int count = 0;

        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).type == type) {
                count++;
            }
        }

        if (count >= maxFreq) {
            return false;
        }


        return true;
    }


    //initialize menu array
    void makeMenu() {

        menu.clear();

        for (int i = 0; i < table.size(); i++) {
            if (menu.size() >= days)
                break;

            if (canUseMeal(table.get(i).type) == true) {
                menu.add(table.get(i));
            }
        }


    }


    //Returns -1 if not found else index of dish
    //if found.
    int findDish(String name) {

        String compName = "";

        name = name.toUpperCase();

        for (int i = 0; i < table.size(); i++) {
            compName = table.get(i).name.toUpperCase();

            if (name.equals(compName) == true) {
                return i;
            }
        }

        return -1;
    }


    //Joke stuff*****************************************************************************


    //This method is gonna be stupid long because it will store random jokes ;-;
    String getJoke() {
        int next;

        Random ran = new Random();

        next = ran.nextInt((arr.size() - 1));

        return arr.get(next);
    }


    //This is such a bad thing to do but im gonna do it anyway
    //because I can.
    void initJokes() {

        arr.add("People don't get my puns. They think they're funny.");
        arr.add("What do you call a Spanish guy with a rubber toe? Roberto");
        arr.add("If a church wants a better pastor, It only needs to pray for the one it has.");
        arr.add("It is easier to preach ten sermons than it is to live one.");
        arr.add("When wearing a bikini, women reveal 90 % of their body...\nmen are so polite they only look at the covered parts.");
        arr.add("Isn't it great to live in the 21st century?\nWhere deleting history has become more important than making it.");
        arr.add("Just read that 4,153,237 people got married last year,\nnot to cause any trouble but shouldn't that be an even number?");
        arr.add("Today a man knocked on my door and asked for a small\ndonation towards the local swimming pool. I gave him a glass of water.");
        arr.add("I want to die peacefully in my sleep, like my grandfather..\nNot screaming and yelling like the passengers in his car.");
        arr.add("A recent study has found that women who carry a little\nextra weight live longer than the men who mention it.");
        arr.add("If i had a dollar for every girl that found me unattractive,\nthey would eventually find me attractive.");
        arr.add("When my boss asked me who is the stupid one, me or him?\nI told him everyone knows he doesn't hire stupid people.");
        arr.add("I think my neighbor is stalking me as she's been googling\nmy name on her computer. I saw it through my telescope last night.");
        arr.add("Life is all about perspective. The sinking of the\nTitanic was a miracle to the lobsters in the ship's kitchen.");
        arr.add("Team work is important; it helps to put the blame on someone else.");
        arr.add("I find it ironic that the colors red, white, and blue\nstand for freedom until they are flashing behind you.");
        arr.add("When Miley Cyrus gets naked & licks a hammer it's \"art\" & \"music\".\nBut when I do it, I'm \"drunk\" and \"have to leave the hardware store\".");
        arr.add("My therapist says I have a preoccupation with vengeance.\nWe'll see about that.");
        arr.add("You're not fat, you're just... easier to see.");
        arr.add("Relationships are a lot like algebra.\nHave you ever looked at your X and wondered Y?");
        arr.add("That awkward moment when you leave a store without\nbuying anything and all you can think is \"act natural, you're innocent\".");
        arr.add("Feeling pretty proud of myself. The Sesame Street\npuzzle I bought said 3-5 years, but I finished it in 18 months.");
        arr.add("My wife and I were happy for twenty years.\nThen we met.");
        arr.add("You know you're ugly when it comes to a group\npicture and they hand you the camera.");
        arr.add("You know that tingly little feeling you get\nwhen you like someone? That's your common sense leaving your body.");
        arr.add("I'm great at multitasking. I can waste time,\nbe unproductive, and procrastinate all at once.");
        arr.add("Strong people don't put others down.\nThey lift them up and slam them on the ground for maximum damage.");
        arr.add("If you think nobody cares whether you're alive,\ntry missing a couple of payments.");
        arr.add("If you're not supposed to eat at night,\nwhy is there a light bulb in the refrigerator?");
        arr.add("Before I criticize a man, I like to walk a mile in his shoes.\nThat way, when I do criticize him, I'm a mile away and I have his shoes.");
        arr.add("Politicians and diapers have one thing in common.\nThey should both be changed regularly, and for the same reason.");
        arr.add("People used to laugh at me when I would say\n\"I want to be a comedian\", well nobody's laughing now.");
        arr.add("I used to think I was indecisive, but now I'm not too sure.");
        arr.add("Intelligence is like an underwear.\nIt is important that you have it, but not necessary that you show it off.");
        arr.add("Improve your memory by doing unforgettable things.");
        arr.add("I bought a vacuum cleaner six months ago and\nso far all it's been doing is gathering dust.");
        arr.add("Alcohol is a perfect solvent:\nIt dissolves marriages, families and careers.");
        arr.add("I can totally keep secrets.\nIt's the people I tell them to that can't.");
        arr.add("I changed my password to \"incorrect\". So whenever\nI forget what it is the computer will say \"Your password is incorrect\".");
        arr.add("I started out with nothing, and I still have most of it.");
        arr.add("I'm not saying I hate you, but I would unplug\nyour life support to charge my phone.");
        arr.add("If you can smile when things go wrong,\nyou have someone in mind to blame.");
        arr.add("I asked God for a bike, but I know God\ndoesn't work that way. So I stole a bike and asked for forgiveness.");
        arr.add("It's funny, when I walk into a spider web I demolish\nhis home and misplace his dinner yet I still feel like the victim.");
        arr.add("Entered what I ate today into my new fitness app and\nit just sent an ambulance to my house.");
        arr.add("Keep the dream alive: Hit the snooze button.");
        arr.add("Hospitality: making your guests feel like they're\nat home, even if you wish they were.");
        arr.add("A straight face and a sincere-sounding \"Huh?\" have\ngotten me out of more trouble than I can remember.");
        arr.add("I hate when I am about to hug someone really\nsexy and my face hits the mirror.");
        arr.add("Best friends: Ready to die for each other,\nbut will fight to the death over the last slice of pizza.");
        arr.add("When an employment application asks who is\nto be notified in case of emergency, I always write, \"A very good doctor\".");
        arr.add("Waking up this morning was an eye-opening experience.");
        arr.add("Whatever you do always give 100 %.\nUnless you are donating blood.");
        arr.add("What's worse than waking up at a party and\nfinding a penis drawn on your face? Finding out it was traced.");
        arr.add("A clean house is the sign of a broken computer.");
        arr.add("Eat right. Stay fit. Die anyway.");
        arr.add("I didn't say it was your fault, I said I was blaming you.");
        arr.add("When I call a family meeting I turn off\nthe house wifi and wait for them all to come running.");
        arr.add("I hate people who use big words just to\nmake themselves look perspicacious.");
        arr.add("Funny how they say we need to talk when\nthey really mean you need to listen.");
        arr.add("If at first you don't succeed, we have a lot in common.");
        arr.add("I asked my North Korean friend how it was there,\nhe said he couldn't complain.");
        arr.add("I'm currently boycotting any company\nthat sells items I can't afford.");
        arr.add("My wife had her driver's test the other day.\nShe got 8 out of 10. The other 2 guys jumped clear.");
        arr.add("I was born to be a pessimist. My blood type is B Negative.");
        arr.add("Did you hear about the guy who got hit in the head\nwith a can of soda? He was lucky it was a soft drink.");
        arr.add("People are making end of the world jokes.\nLike there is no tomorrow.");
        arr.add("I can't believe I got fired from the calendar factory.\nAll I did was take a day off.");
        arr.add("Nothing ruins a Friday more than an\nunderstanding that today is Tuesday");
        arr.add("Any room is a panic room if you've lost your phone in it.");
        arr.add("Nothing is fool proof to a sufficiently talented fool.");
        arr.add("I wasn't originally going to get a brain transplant,\nbut then I changed my mind.");
        arr.add("I gave up my seat to a blind person in the bus.\nThat is how I lost my job as a bus driver.");
        arr.add("If procrastionation was an Olympic sport,\nI'd compete in it later.");
        arr.add("Regular naps prevent old age,\nespecially if you take them while driving.");
        arr.add("How did I escape Iraq? Iran.");
        arr.add("Why was Cinderella thrown off the basketball team?\nShe ran away from the ball.");
        arr.add("There is a new trend in our office; everyone is putting\nnames on their food. I saw it today, while I was eating a sandwich named Kevin.");

        //I regret this so much, this took so long to format ;-;

    }

}
