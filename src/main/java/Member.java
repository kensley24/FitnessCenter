import java.sql.*;
import java.util.Scanner;

public class Member extends User{
    int id;
    String name;
    int goal_weight;
    int goal_fitness;
    int weight;
    int fitness_lvl;
    int mem_fee;
    Scanner scan;

    public Member() {
        this.id = id;
        this.name = name;
        this.goal_weight = goal_weight;
        this.goal_fitness = goal_fitness;
        this.weight = weight;
        this.fitness_lvl = fitness_lvl;
        this.mem_fee = 50;
        scan = new Scanner(System.in);
    }

    //Every new member gets a unique ID and a membership fee automatically
    public void signup(){
        Scanner s = new Scanner(System.in);
        System.out.println("Name: ");
        this.name = s.next();
        System.out.println("goal weight: ");
        this.goal_weight = s.nextInt();
        System.out.println("goal fitness (between 1 to 10): ");
        this.goal_fitness = s.nextInt();
        System.out.println("Current weight: ");
        this.weight = s.nextInt();
        System.out.println("Current fitness level (between 1 to 10): ");
        this.fitness_lvl = s.nextInt();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            String add = "INSERT INTO Members VALUES(DEFAULT,?, ?, ?, ?, ?, 150);";
            PreparedStatement addNewMember = conn.prepareStatement(add);
            addNewMember.setString(1, this.name);
            addNewMember.setInt(2, this.goal_weight);
            addNewMember.setInt(3, this.goal_fitness);
            addNewMember.setInt(4, this.weight);
            addNewMember.setInt(5, this.fitness_lvl);
            addNewMember.executeUpdate();
            System.out.println("you have been added successfully");

            PreparedStatement getId = conn.prepareStatement("SELECT mem_id FROM Members WHERE first_name = ? AND goal_weight = ? AND goal_fitness = ? AND weight = ?");
            getId.setString(1, this.name);
            getId.setInt(2, this.goal_weight);
            getId.setInt(3, this.goal_fitness);
            getId.setInt(4, this.weight);
            ResultSet rs = getId.executeQuery();
            while(rs.next()){
                this.id = rs.getInt(1);
                System.out.println("Your ID is: " + this.id);
            }

        }catch(Exception e){
            System.out.println(e);
            return;
        }
        System.out.println();


    }

    //Functionality to update profile information including
    //-name
    //-goal weight
    //-goal fitness
    //-current weight
    //-current fitness
    public void updateProfile(){
        Scanner s = new Scanner(System.in);
        int choice = -1;
        int updateValue = -3;
        String update = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            while (choice != 0) {
                System.out.println();
                System.out.println("Choose:");
                System.out.print("1: name \n2: goal_weight \n3: goal_fitness \n4: current weight \n5: current fitness\n0: exit\nChoice: ");
                choice = scan.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("What do you want to update your name to: ");
                        String newName = scan.next();
                        update = "UPDATE Members SET first_name = ? WHERE mem_id = ?";
                        PreparedStatement updateName = conn.prepareStatement(update);
                        updateName.setString(1, newName);
                        updateName.setInt(2, this.id);
                        updateName.executeUpdate();
                        System.out.println("Your name has been changed to "+newName);
                        this.name = newName;
                        break;

                    case 2:
                        System.out.print("What do you want to update your goal weight to: ");
                        updateValue = scan.nextInt();
                        update = "UPDATE Members SET  goal_weight = ? WHERE mem_id = ?";
                        this.goal_weight = updateValue;
                        break;

                    case 3:
                        System.out.print("What do you want to update your goal fitness to: ");
                        updateValue = scan.nextInt();
                        update = "UPDATE Members SET goal_fitness = ? WHERE mem_id = ?";
                        this.goal_fitness = updateValue;
                        break;
                    case 4:
                        System.out.print("What do you want to update your current weight to: ");
                        updateValue = scan.nextInt();
                        update = "UPDATE Members SET weight = ? WHERE mem_id = ?";
                        this.weight = updateValue;
                        break;

                    case 5:
                        System.out.print("What do you want to update your current fitness level to: ");
                        updateValue = scan.nextInt();
                        update = "UPDATE Members SET fitness_lvl = ? WHERE mem_id = ?";
                        this.fitness_lvl = updateValue;
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Not valid input");

                }

                if(choice >=2 && choice <=5){
                    PreparedStatement updateOther = conn.prepareStatement(update);
                    updateOther.setInt(1, updateValue);
                    updateOther.setInt(2, this.id);
                    updateOther.executeUpdate();
                    System.out.println("has been changed to "+ updateValue);
                }

            }

        }catch(Exception e){
            System.out.println(e);
            return;
        }
        System.out.println();

    }

    //Functionality to create a new exercise routine that consists of
    //Squats, lunges, push-ups and yoga
    //Added to profile
    public void updateExercise(){

        System.out.println("Select the number of exercises you want to do for this routine: ");
        System.out.print("Squats: ");
        int squats = scan.nextInt();

        System.out.print("Lunges: ");
        int lunges = scan.nextInt();

        System.out.print("pushups: ");
        int pushups = scan.nextInt();

        System.out.print("yoga (minutes): ");
        int yoga = scan.nextInt();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            String add = "INSERT INTO Exercise_routine VALUES(DEFAULT,?, ?, ?, ?, ?);";
            PreparedStatement addNewExercise = conn.prepareStatement(add);

            addNewExercise.setInt(1, this.id);
            addNewExercise.setInt(2, squats);
            addNewExercise.setInt(3, lunges);
            addNewExercise.setInt(4, pushups);
            addNewExercise.setInt(5, yoga);
            addNewExercise.executeUpdate();
            System.out.println("a new exercise routine has been added successfully");

        }catch(Exception e){
            System.out.println(e);
            return;
        }
        System.out.println();

    }


    //Personal Trainer Schedule
    public int schedule(int train_id, String train_name){
        try {

            Trainer t = new Trainer();
            if(t.login(train_id, train_name) < 0){
             return -1;
            }

            System.out.println("Select which time slot you would like to schedule that is not already scheduled");
            int time_slot = scan.nextInt();

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            String get = "SELECT scheduled FROM Trainer_times WHERE train_id = ? AND time_slot = ?";
            PreparedStatement timeSchedule = conn.prepareStatement(get);
            timeSchedule.setInt(1, train_id);
            timeSchedule.setInt(2, time_slot);

            ResultSet rs = timeSchedule.executeQuery();

            if((!rs.isBeforeFirst())){
                System.out.println("you have entered an invalid time slot");
                return -1;
            }
            while(rs.next()){
                if(rs.getBoolean(1)){
                    System.out.println("That time is not available");
                    return -1;
                }
                else{

                    String add = "INSERT INTO Trainer_member VALUES(?, ?, ?, ?)";
                    PreparedStatement addMemberTime = conn.prepareStatement(add);
                    addMemberTime.setInt(1, train_id);
                    addMemberTime.setInt(2, this.id);
                    addMemberTime.setInt(3, time_slot);
                    addMemberTime.setInt(4, t.fee);
                    addMemberTime.executeUpdate();

                    String update = "UPDATE Trainer_times SET scheduled = TRUE WHERE train_id = ? AND time_slot = ?";
                    PreparedStatement updateScheduled = conn.prepareStatement(update);
                    updateScheduled.setInt(1, train_id);
                    updateScheduled.setInt(2, time_slot);
                    updateScheduled.executeUpdate();
                    System.out.println("your requested time is now scheduled");
                }
            }

        }
        catch(Exception e){
            System.out.println(e);

        }
        System.out.println();
        return 1;
    }

    //Personal Trainer cancel session -Can only cancel a session if you're already signed up for it
    public int cancel(int train_id){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            System.out.print("session number: ");
            int time_slot = scan.nextInt();

            String delete = "DELETE FROM Trainer_member WHERE train_id = ? AND mem_id = ? AND time_slot = ?";
            PreparedStatement addMemberTime = conn.prepareStatement(delete);
            addMemberTime.setInt(1, train_id);
            addMemberTime.setInt(2, this.id);
            addMemberTime.setInt(3, time_slot);

            int d = addMemberTime.executeUpdate();
            System.out.println(d);
            if(d <= 0){
                System.out.println("Session not scheduled, cannot delete");
                return -1;
            }

            String update = "UPDATE Trainer_times SET scheduled = FALSE WHERE train_id = ? AND time_slot = ?";
            PreparedStatement updateScheduled = conn.prepareStatement(update);
            updateScheduled.setInt(1, train_id);
            updateScheduled.setInt(2, time_slot);
            updateScheduled.executeUpdate();
            System.out.println("your session is now deleted as requested");


        }
        catch(Exception e){
            System.out.println(e);
            return -1;
        }
        System.out.println();

        return 1;

    }

    //Personal Trainer reschedule
    public void reschedule(int train_id, String train_name){
        int d = cancel(train_id);
        if(d >0){
            schedule(train_id, train_name);
        }
        System.out.println();
    }

    //Schedule group classes -Can only sign up for group classes they have not already scheduled
    public void group_class(){
        try {

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            Statement classSched = conn.createStatement();
            classSched.executeQuery( "SELECT * FROM Class");

            ResultSet set = classSched.getResultSet();

            if((!set.isBeforeFirst())){
                System.out.println("No classes are scheduled yet");
                return;
            }
            System.out.println("Classes:");
            while(set.next()){
                System.out.print(set.getInt(1) + "| " + set.getString(2) + " $" + set.getInt(3) + ", room number: ");
                int room = set.getInt(4);
                if(room == 0){
                    System.out.println("Not Scheduled");
                }
                else{
                    System.out.println(room);
                }
            }

            System.out.print("Select the class number you would like to join: ");
            int class_session = scan.nextInt();

            PreparedStatement test1 = conn.prepareStatement("SELECT * FROM Class WHERE class_id = ?");
            test1.setInt(1, class_session);
            ResultSet l = test1.executeQuery();
            if(!l.isBeforeFirst()){
                System.out.println("not good input");
                return;
            }
            int fee = 0;
            while(l.next()){
                fee = l.getInt("fee");
            }
            PreparedStatement test2 = conn.prepareStatement("SELECT * FROM Class_member WHERE mem_id = ? AND class_id = ?");
            test2.setInt(1, this.id);
            test2.setInt(2, class_session);
            ResultSet j = test2.executeQuery();
            if(j.next()){
                System.out.println("You have already scheduled this class");
                return;
            }

            PreparedStatement add = conn.prepareStatement("INSERT INTO Class_member VALUES(?,?,?)");
            add.setInt(1, this.id);
            add.setInt(2, class_session);
            add.setInt(3, fee);
            add.executeUpdate();
            System.out.println("Class Scheduled successfully");
            System.out.println();

        }
        catch(Exception e){
            System.out.println(e);

        }

    }



    public void display(){
        int choice = -1;

        while(choice != 0){

            System.out.println("Menu: ");
            System.out.println("1: Update your profile information");
            System.out.println("2: Add an exercise routine");
            System.out.println("3: Schedule with a personal trainer");
            System.out.println("4: Schedule group class");
            System.out.println("5: view profile");
            System.out.println("0: exit");
            choice = scan.nextInt();

            switch (choice) {
                case 1:
                    System.out.println();
                    updateProfile();
                    break;

                case 2:
                    System.out.println();
                    updateExercise();
                    break;

                case 3:
                    System.out.println();
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
                        Statement getTrainers = conn.createStatement();
                        getTrainers.executeQuery("SELECT * FROM Trainer");
                        ResultSet allTrainers = getTrainers.getResultSet();
                        System.out.println("---------------Trainers---------------");
                        while(allTrainers.next()){
                            System.out.println();
                            Trainer t = new Trainer();
                            t.login(allTrainers.getInt(1), allTrainers.getString(2));
                        }

                    }catch(Exception e){
                            System.out.println(e);
                    }
                    Scanner s = new Scanner(System.in);
                    System.out.println();
                    System.out.println("Give the trainer information you would like to schedule, re-schedule or cancel a class with: ");
                    System.out.print("id: ");
                    int train_id = s.nextInt();
                    System.out.println("Trainer name: ");
                    String train_name = s.next();
                    System.out.println();
                    System.out.println("1: schedule");
                    System.out.println("2: reschedule");
                    System.out.println("3: cancel");
                    int schoice = s.nextInt();

                    switch (schoice) {
                        case 1:
                            schedule(train_id, train_name);
                            break;
                        case 2:
                            reschedule(train_id, train_name);
                            break;
                        case 3:
                            cancel(train_id);
                            break;
                    }
                    break;

                case 4:
                    System.out.println();
                    group_class();
                    break;

                case 5:
                    System.out.println();
                    login(this.id, this.name);
                    break;

                case 0:
                    System.out.println("goodbye!");
                    break;
            }

        }


    }

    //login shows profile including:
    //-weight/ fitness level
    //-goal weight / goal fitness level
    //-fitness achievements
    //-exercise routines
    //-schedules with personal trainers or classes
    public int login(int id, String name){

        this.id = id;
        this.name = name;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            String get = "SELECT * FROM Members WHERE mem_id = ? AND first_name = ?";
            PreparedStatement prepared = conn.prepareStatement(get);
            prepared.setInt(1, this.id);
            prepared.setString(2, this.name);

            ResultSet result = prepared.executeQuery();
            if((!result.isBeforeFirst())){
                System.out.println("you have entered the wrong user id or name");
                return -1;
            }

            while (result.next()) {

                this.goal_weight = result.getInt("goal_weight");
                this.goal_fitness = result.getInt("goal_fitness");
                this.weight = result.getInt("weight");
                this.fitness_lvl = result.getInt("fitness_lvl");
            }

            System.out.println();
            System.out.println(name);
            System.out.println("------------------DASHBOARD------------------");
            System.out.println("Member id: "+id);
            System.out.println("Health stats: ");
            System.out.println("-Current Weight: "+weight);
            System.out.println("-Current fitness level: "+fitness_lvl);
            System.out.println();
            System.out.println("Fitness Achievements: ");
            if(weight <= goal_weight){
                System.out.println("weight goal achieved!");
            }
            else{
                System.out.println((weight - goal_weight) + " lbs close to your weight goal");
            }
            if(fitness_lvl >= goal_fitness){
                System.out.println("fitness level goal achieved!");
            }
            else{
                System.out.println((goal_fitness - fitness_lvl) + " levels close to your fitness goal");
            }

            System.out.println();

            System.out.println("Exercise routines: ");

            String exerciseQuery = "SELECT * FROM Exercise_routine WHERE mem_id = ?";
            PreparedStatement getExercise = conn.prepareStatement(exerciseQuery);
            getExercise.setInt(1, this.id);
            ResultSet exerciseRows = getExercise.executeQuery();
            if(!exerciseRows.isBeforeFirst()) {
                System.out.println("You have no exercise entries");

            }else{
                int i = 1;
                while(exerciseRows.next()) {


                    System.out.println("************************************************");

                    System.out.println("Routine number " + i++);
                    System.out.println("Squats: " + exerciseRows.getInt("squats"));
                    System.out.println("lunges: " + exerciseRows.getInt("lunges"));
                    System.out.println("push-ups: " + exerciseRows.getInt("pushups"));
                    System.out.println("yoga (minutes): " + exerciseRows.getInt("yoga"));

                }
                System.out.println("************************************************");

            }
            System.out.println();

            System.out.println("Scheduled with personal trainer: ");

            String trainerQuery = "SELECT * FROM Trainer_member WHERE mem_id = ?";
            PreparedStatement getTrainer = conn.prepareStatement(trainerQuery);
            getTrainer.setInt(1, this.id);
            ResultSet trainerRows = getTrainer.executeQuery();
            if(!trainerRows.isBeforeFirst()) {
                System.out.println("You have no Personal trainer times scheduled");

            }else{
                while(trainerRows.next()) {


                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

                    System.out.println("Trainer id: " + trainerRows.getInt(1));
                    System.out.println("Time slot number: " + trainerRows.getInt(3));
                    int charged = trainerRows.getInt(4);
                    if(charged == 0){
                        System.out.println("Class has been paid for");
                    }
                    else{
                        System.out.println("Class cost: $" + charged);
                    }

                }
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            }

            System.out.println();

            System.out.println("Class Schedule: ");

            String classQuery = "SELECT * FROM Class_member WHERE mem_id = ?";
            PreparedStatement getClass = conn.prepareStatement(classQuery);
            getClass.setInt(1, this.id);
            ResultSet classRows = getClass.executeQuery();
            if(!classRows.isBeforeFirst()) {
                System.out.println("You have no classes scheduled");

            }else{
                while(classRows.next()) {


                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    System.out.println("Class id: " + classRows.getInt(2));
                    int charged = classRows.getInt(3);
                    if(charged == 0){
                        System.out.println("Class has been paid for");
                    }
                    else{
                        System.out.println("Class cost: $" + charged);
                    }

                }
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            }

            System.out.println();

        }
        catch(Exception e){
            System.out.println(e);
            return -1;
        }
        return 1;

    }
    //functionality will only show if the login is correct
    public void profileDisplay(int id, String name){
        int login = login(id, name);
        if(login > 0){
            System.out.println();
            System.out.println("------------------DISPLAY OPTIONS------------------");

            display();

        }

    }


}
