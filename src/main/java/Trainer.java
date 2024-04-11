import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class Trainer extends User{
    int id;
    int fee;
    String name;
    Scanner scan;

    public Trainer() {
        this.id = id;
        this.fee = fee;
        this.name = name;
        scan = new Scanner(System.in);
    }

    //All new trainers will be automatically assigned a unique ID
    public void signup(){
        Scanner s = new Scanner(System.in);
        System.out.print("Name: ");
        this.name = s.next();
        System.out.print("fee per session: ");
        this.fee = s.nextInt();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            String add = "INSERT INTO Trainer\n" +
                    "VALUES(DEFAULT,?, ?);";
            PreparedStatement addNewTrainer = conn.prepareStatement(add);
            addNewTrainer.setString(1, this.name);
            addNewTrainer.setInt(2, this.fee);
            addNewTrainer.executeUpdate();
            System.out.println("you have been added successfully");
            PreparedStatement getId = conn.prepareStatement("SELECT train_id FROM Trainer WHERE first_name = ?");
            getId.setString(1, this.name);
            ResultSet rs = getId.executeQuery();
            while(rs.next()){
                this.id = rs.getInt(1);
                System.out.println("Your ID is: " + this.id);
            }

        }catch(Exception e){
            System.out.println(e);
            return;
        }


    }

    //functionality to view a member profile by making a specified member incident and viewing their profile
    public void viewMember(){
        Scanner scan = new Scanner(System.in);
        System.out.println("enter the user information you would like to view");
        System.out.print("id: ");
        int user_id = scan.nextInt();
        System.out.print("name: ");
        String user_name = scan.next();
        Member m = new Member();
        m.login(user_id, user_name);

    }

    //Functionality to create a new time slot
    //Time slot ID added automatically
    public void setSchedule(){

        System.out.println("Select the day and time you would like to schedule a training session");
        System.out.print("time (24h): ");
        int time = scan.nextInt();
        System.out.print("day: ");
        int day = scan.nextInt();
        System.out.print("month: ");
        String month = scan.next();
        System.out.print("year: ");
        int year = scan.nextInt();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            String add = "INSERT INTO Trainer_times VALUES(?, DEFAULT, ?, ?, ?, ?, FALSE)";
            PreparedStatement addTime = conn.prepareStatement(add);
            addTime.setInt(1, this.id);
            addTime.setInt(2, time);
            addTime.setInt(3, day);
            addTime.setString(4, month);
            addTime.setInt(5, year);

            addTime.executeUpdate();
            System.out.println("Your time has been added successfully");

        } catch (Exception e) {
            System.out.println(e);
            return;
        }

    }

    public void display(){
        int choice = -1;
        while(choice != 0){

            System.out.println("Menu: ");
            System.out.println("1: set your schedule");
            System.out.println("2: see a member profile");
            System.out.println("3: see your profile");
            System.out.println("0: exit");
            choice = scan.nextInt();

            switch (choice){
                case 1:
                    setSchedule();
                    break;

                case 2:
                    viewMember();
                    break;

                case 3:
                    login(this.id, this.name);
                    break;

                case 0:
                    System.out.println("goodbye!");
                    break;
            }

        }



    }

    //Profile information shown upon login
    //-current fee
    //-all current scheduled times and if its scheduled by a member or not
    public int login(int id, String name) {
        System.out.println("getting info for " + name + " id: " + id);
        this.id = id;
        this.name = name;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            //display(conn);

            String get = "SELECT * FROM Trainer WHERE train_id = ? AND first_name = ?";
            PreparedStatement getTrainer = conn.prepareStatement(get);
            getTrainer.setInt(1, this.id);
            getTrainer.setString(2, this.name);

            ResultSet result = getTrainer.executeQuery();
            if(!result.isBeforeFirst()){
                System.out.println("you entered your ID or name wrong");
                return -1;
            }
            while (result.next()) {
                System.out.println(name);
                this.fee = result.getInt("fee");
            }
            System.out.println("Current fee: " + this.fee);

            String getSchedule = "SELECT * FROM Trainer_times WHERE train_id = ?";
            PreparedStatement trainSched = conn.prepareStatement(getSchedule);
            trainSched.setInt(1, this.id);
            ResultSet rs = trainSched.executeQuery();
            System.out.println("-----------Schedule----------");
            if(!rs.isBeforeFirst()) {
                System.out.println("There is no time slots scheduled");

            }else {
                while (rs.next()) {

                    System.out.println("************************************************");

                    System.out.println("Time Slot number: "+ rs.getInt("time_slot"));
                    System.out.println("Time available: ");
                    System.out.println(rs.getString("month")+ " " + rs.getInt("day")+ " "+rs.getInt("year")+ " at hour "+ rs.getInt("time"));
                    if(rs.getBoolean("scheduled")){
                        System.out.println("This time has been scheduled by a member");
                    }
                    else{
                        System.out.println("This time has not been scheduled by a member");
                    }
                }
                System.out.println("************************************************");
            }

        } catch (Exception e) {
            System.out.println(e);
            //return -1;
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
