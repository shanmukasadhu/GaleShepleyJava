import java.util.ArrayList;

class Main {

    public static class Man {
        public String name = "";
        public boolean isFree;
        public ArrayList<Woman> preferences = new ArrayList<>();
        public ArrayList<Woman> proposedTo = new ArrayList<>();
        public ArrayList<Woman> engaged = new ArrayList<>();

        public Man(String name) {
            this.name = name;
            this.isFree = true;  
            System.out.println("Man created: " + name);
        }
    }

    public static class Woman {
        public String name = "";
        public boolean isFree;
        public ArrayList<Man> preferences = new ArrayList<>();
        public ArrayList<Man> proposedTo = new ArrayList<>();
        public ArrayList<Man> engaged = new ArrayList<>();

        public Woman(String name) {
            this.name = name;
            this.isFree = true;  
            System.out.println("Woman created: " + name);
        }
    }

    // Static method for finding free men
    public static ArrayList<Man> findFreeMen(Man[] allMen) {
        ArrayList<Man> freeMen = new ArrayList<>();
        for (Man man : allMen) {
            if (man.isFree) {
                freeMen.add(man);
            }
        }
        return freeMen;
    }

    public static void main(String[] args) {

        int menCount = 6;
        int womanCount = 3;

        Man[] allMen = new Man[menCount];
        Woman[] allWomen = new Woman[womanCount];

        for (int i = 0; i < menCount; i++) {
            allMen[i] = new Man("Man " + (i + 1));
        }
        for (int j = 0; j < womanCount; j++) {
            allWomen[j] = new Woman("Woman " + (j + 1));
        }

        //Define Preferences

        allMen[0].preferences.add(allWomen[0]);
        allMen[0].preferences.add(allWomen[1]);
        allMen[0].preferences.add(allWomen[2]);

        allMen[1].preferences.add(allWomen[1]);
        allMen[1].preferences.add(allWomen[0]);
        allMen[1].preferences.add(allWomen[2]);

        allMen[2].preferences.add(allWomen[0]);
        allMen[2].preferences.add(allWomen[2]);
        allMen[2].preferences.add(allWomen[1]);

        allMen[3].preferences.add(allWomen[1]);
        allMen[3].preferences.add(allWomen[2]);
        allMen[3].preferences.add(allWomen[0]);

        allMen[4].preferences.add(allWomen[2]);
        allMen[4].preferences.add(allWomen[0]);
        allMen[4].preferences.add(allWomen[1]);

        allMen[5].preferences.add(allWomen[0]);
        allMen[5].preferences.add(allWomen[1]);
        allMen[5].preferences.add(allWomen[2]);

        // Assigning preferences for the allWomen
        allWomen[0].preferences.add(allMen[1]);
        allWomen[0].preferences.add(allMen[3]);
        allWomen[0].preferences.add(allMen[0]);
        allWomen[0].preferences.add(allMen[5]);
        allWomen[0].preferences.add(allMen[2]);
        allWomen[0].preferences.add(allMen[4]);

        allWomen[1].preferences.add(allMen[2]);
        allWomen[1].preferences.add(allMen[0]);
        allWomen[1].preferences.add(allMen[4]);
        allWomen[1].preferences.add(allMen[1]);
        allWomen[1].preferences.add(allMen[3]);
        allWomen[1].preferences.add(allMen[5]);

        allWomen[2].preferences.add(allMen[0]);
        allWomen[2].preferences.add(allMen[2]);
        allWomen[2].preferences.add(allMen[5]);
        allWomen[2].preferences.add(allMen[1]);
        allWomen[2].preferences.add(allMen[4]);
        allWomen[2].preferences.add(allMen[3]);






        // Stable Matching Algorithm
        ArrayList<Man> allFreeMen = findFreeMen(allMen);

        while (!allFreeMen.isEmpty()) {
            Man firstFreeMan = allFreeMen.get(0); 
            Woman w = null; 

            for (Woman woman : firstFreeMan.preferences) {
                if (!firstFreeMan.proposedTo.contains(woman)) {
                    w = woman;
                    break;
                }
            }

            firstFreeMan.proposedTo.add(w);

            if (w.engaged.size() < 2) {
                w.engaged.add(firstFreeMan);
                firstFreeMan.engaged.add(w);
                firstFreeMan.isFree = false;
            } else {
                ArrayList<Man> currentPartners = w.engaged;
                Man leastPreferred = null;
                int highestPreferenceIndex = -1;

                for (Man partner : currentPartners) {
                    int preferenceIndex = w.preferences.indexOf(partner);
                    if (preferenceIndex > highestPreferenceIndex) {
                        highestPreferenceIndex = preferenceIndex;
                        leastPreferred = partner;
                    }
                }

                if (w.preferences.indexOf(firstFreeMan) < w.preferences.indexOf(leastPreferred)) {
                    w.engaged.remove(leastPreferred);
                    leastPreferred.engaged.remove(w);
                    leastPreferred.isFree = true;

                    w.engaged.add(firstFreeMan);
                    firstFreeMan.engaged.add(w);
                    firstFreeMan.isFree = false;
                }
            }

            allFreeMen = findFreeMen(allMen);
        }

        System.out.println("Stable matching process completed.");
        for (Man man : allMen) {
            if (!man.engaged.isEmpty()) {
                System.out.println(man.name + " is engaged to " + man.engaged.get(0).name);
            } else {
                System.out.println(man.name + " is not engaged.");
            }
        }
    }
}

