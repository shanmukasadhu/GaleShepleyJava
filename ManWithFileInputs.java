import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class ManWithFileInputs {

    public static class Player {
        public String name = "";
        public boolean isFree;
        public ArrayList<Team> preferences = new ArrayList<>();
        public ArrayList<Team> proposedTo = new ArrayList<>();
        public ArrayList<Team> engaged = new ArrayList<>();

        public Player(String name) {
            this.name = name;
            this.isFree = true;
            System.out.println("Player created: " + name);
        }
    }

    public static class Team {
        public String name = "";
        public boolean isFree;
        public ArrayList<Player> preferences = new ArrayList<>();
        public ArrayList<Player> proposedTo = new ArrayList<>();
        public ArrayList<Player> engaged = new ArrayList<>();

        public Team(String name) {
            this.name = name;
            this.isFree = true;
            System.out.println("Team created: " + name);
        }
    }

    public static ArrayList<Player> findFreePlayer(Player[] allPlayers) {
        ArrayList<Player> freePlayer = new ArrayList<>();
        for (Player player : allPlayers) {
            if (player.isFree) {
                freePlayer.add(player);
            }
        }
        return freePlayer;
    }

    public static void main(String[] args) {
        try {
            File inputFile = new File("/Users/shanmukasadhu/example1.txt");
            Scanner inputScanner = new Scanner(inputFile);

            int lineCount = 1;
            HashMap<String, Player> playerVariables = new HashMap<>();
            HashMap<String, Team> teamVariables = new HashMap<>();
            int playerCount = 0;
            int teamCount = 0;

            Player[] allPlayers = null;  
            Team[] allTeams = null;  
            

            while (inputScanner.hasNextLine()) {
                String data = inputScanner.nextLine();
                System.out.println(data);

                // Get all players
                if (lineCount == 1) {
                    String[] splitArray = data.split(",\\s*");

                    // Initialize the men
                    playerCount = splitArray.length;
                    allPlayers = new Player[playerCount];
                    int count = 1;
                    for (String player : splitArray) {
                        playerVariables.put(player, new Player(player));
                        allPlayers[count-1] = playerVariables.get(player);
                        count += 1;
                        
                    }
                    
                    
                }
                else if (lineCount == 2) {
                    String[] splitArray = data.split(",\\s*");

                    teamCount = splitArray.length;
                    int count = 1;
                    allTeams = new Team[teamCount];
                    for (String team : splitArray) {
                        teamVariables.put(team, new Team(team));
                        allTeams[count-1] = teamVariables.get(team);
                        count += 1;
                    }
                }
                else if (lineCount <= 2 + (playerCount)) {
                    String[] splitArray = data.split(":|,\\s*");
                    

                    int count = 0;
                    for (String thing : splitArray) {
                        if (count != 0) {
                            playerVariables.get(splitArray[0]).preferences.add(teamVariables.get(thing.trim()));
                        }
                        count += 1;
                    }
                }
                else
                {
                    String[] splitArray = data.split(":|,\\s*");
                    
                    int count = 0;
                    for (String thing : splitArray) {
                        if (count != 0) {
                            teamVariables.get(splitArray[0]).preferences.add(playerVariables.get(thing.trim()));
                        }
                        count += 1;
                    }                    
                }


                lineCount += 1;
            }
            inputScanner.close();


            
            // Stable Matching Algorithm
            ArrayList<Player> allFreePlayers = findFreePlayer(allPlayers);

            while (!allFreePlayers.isEmpty()) {
                Player firstFreePlayer = allFreePlayers.get(0); 
                Team w = null; 

                for (Team team : firstFreePlayer.preferences) {
                    if (!firstFreePlayer.proposedTo.contains(team)) {
                        w = team;
                        break;
                    }
                }

                firstFreePlayer.proposedTo.add(w);

                if (w.engaged.size() < (playerCount/teamCount)) {
                    w.engaged.add(firstFreePlayer);
                    firstFreePlayer.engaged.add(w);
                    firstFreePlayer.isFree = false;
                } else {
                    ArrayList<Player> currentPartners = w.engaged;
                    Player leastPreferred = null;
                    int highestPreferenceIndex = -1;

                    for (Player partner : currentPartners) {
                        int preferenceIndex = w.preferences.indexOf(partner);
                        if (preferenceIndex > highestPreferenceIndex) {
                            highestPreferenceIndex = preferenceIndex;
                            leastPreferred = partner;
                        }
                    }

                    if (w.preferences.indexOf(firstFreePlayer) < w.preferences.indexOf(leastPreferred)) {
                        w.engaged.remove(leastPreferred);
                        leastPreferred.engaged.remove(w);
                        leastPreferred.isFree = true;

                        w.engaged.add(firstFreePlayer);
                        firstFreePlayer.engaged.add(w);
                        firstFreePlayer.isFree = false;
                    }
                }

                allFreePlayers = findFreePlayer(allPlayers);
            }
            System.out.println("");
            System.out.println("Stable matching process completed.");
            for (Team team : allTeams) {
                if (!team.engaged.isEmpty()) {
                    System.out.print(team.name+": ");
                    for (Player player : team.engaged) {
                        System.out.print(player.name+" ");
                    }
                    System.out.println("");
                } else {
                    System.out.println(team.name + " does not have any engaged players.");
                }
            }
            

            

        } catch (FileNotFoundException e) {
            System.out.println("Error: The file was not found.");
            e.printStackTrace();
        }
    }
}
