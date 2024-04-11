
import java.sql.*;
import java.util.Scanner;

public class Staff extends User{
    int id;
    String name;
    Scanner scan;

    public Staff(){
        this.id = id;
        this.name = name;
        scan = new Scanner(System.in);
    }

    //All new staff are assigned an ID
    public void signup(){
        Scanner s = new Scanner(System.in);
        System.out.print("Name: ");
        this.name = s.next();

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            String add = "INSERT INTO Staff VALUES(DEFAULT, ?);";
            PreparedStatement addNewStaff = conn.prepareStatement(add);
            addNewStaff.setString(1, this.name);
            addNewStaff.executeUpdate();

            System.out.println("you have been added successfully");
            PreparedStatement getId = conn.prepareStatement("SELECT staff_id FROM Staff WHERE name = ?");
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

    //staff can book a room to a class
    public void roomBooking(){

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            PreparedStatement seeClass = conn.prepareStatement("SELECT * FROM Class WHERE room_num IS NULL");
            ResultSet nullClass = seeClass.executeQuery();

            if(!nullClass.isBeforeFirst()){
                System.out.println("All classes are currently booked with rooms");
                return;
            }
            System.out.println("Classes without rooms: ");
            while(nullClass.next()){
                System.out.println(nullClass.getInt(1) + "| " + nullClass.getString(2));
            }

            System.out.print("Select the class you would like to book a room for: ");
            int classNum = scan.nextInt();


            PreparedStatement seeRoom = conn.prepareStatement("SELECT * FROM Room");
            ResultSet l = seeRoom.executeQuery();

            if(!l.isBeforeFirst()){
                System.out.println("There are no rooms, this building is a lie");
                return;
            }

            System.out.println("Rooms: ");
            while(l.next()){
                System.out.println(l.getInt(1) + "| " + l.getString(2));
            }

            System.out.println("Select a room you would like to book: ");
            int roomNumber = scan.nextInt();

            PreparedStatement bookRoom = conn.prepareStatement("UPDATE Class SET room_num = ? WHERE class_id = ?");
            bookRoom.setInt(1, roomNumber);
            bookRoom.setInt(2, classNum);
            bookRoom.executeUpdate();
            System.out.println("Room Booked Successfully");

        }catch(Exception e){
            System.out.println(e);
            return;
        }

    }

    //Staff can make a class
    public void classSchedule(){
        Scanner s = new Scanner(System.in);
        System.out.print("give the class description: ");
        String description = s.nextLine();
        System.out.print("price per class session: ");
        int price = s.nextInt();
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);
            PreparedStatement addClass = conn.prepareStatement("INSERT INTO Class VALUES(DEFAULT, ?, ?, NULL)");
            addClass.setString(1, description);
            addClass.setInt(2, price);
            addClass.executeUpdate();

            System.out.println("Class made successfully");

        }catch(Exception e){
            System.out.println(e);
            return;
        }


    }


    //getting a bill / charging a member
    //Bill includes total for membership fee, personal trainer sessions and group classes scheduled
    //If a staff chooses to charge a member all outstanding fees will be paid and their total will be 0
    public void getBill(){
        try {
            int trainerFee = 0;
            int membershipFee = 0;
            int classFee = 0;
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            String get = "SELECT * FROM Members";
            PreparedStatement getMem = conn.prepareStatement(get);
            ResultSet result = getMem.executeQuery();
            if(!result.isBeforeFirst()){
                System.out.println("There are no members, your gym is a bust..");
                return;
            }
            while (result.next()) {
                System.out.println(result.getInt(1) + "| " + result.getString(2));
            }
            System.out.print("Select the member you would like to bill: ");
            int member = scan.nextInt();

            PreparedStatement checkMem = conn.prepareStatement("SELECT * FROM Members WHERE mem_id = ?");
            checkMem.setInt(1, member);
            ResultSet mem = checkMem.executeQuery();
            if(!mem.isBeforeFirst()){
                System.out.println("invalid member id");
                return;
            }


            PreparedStatement sumTrainer = conn.prepareStatement("SELECT SUM(charged) AS Total_sum FROM Trainer, Trainer_member WHERE Trainer.train_id = Trainer_member.train_id AND Trainer_member.mem_id = ?;");
            sumTrainer.setInt(1, member);
            ResultSet trainer = sumTrainer.executeQuery();

            while(trainer.next()){
                trainerFee = trainer.getInt("Total_sum");
            }

            PreparedStatement sumClass = conn.prepareStatement("SELECT SUM(charged) AS Total_sum FROM Class, Class_member WHERE Class.class_id = Class_member.class_id AND Class_member.mem_id = ?;");
            sumClass.setInt(1, member);
            ResultSet classes = sumClass.executeQuery();

            while(classes.next()){
                classFee = classes.getInt("Total_sum");
            }

            PreparedStatement memFee = conn.prepareStatement("SELECT memb_fee FROM Members WHERE mem_id = ?");
            memFee.setInt(1, member);

            ResultSet membership = memFee.executeQuery();


            while(membership.next()){
                membershipFee = membership.getInt(1);
            }
            System.out.println();

            System.out.println("Fees outstanding ");
            System.out.println("Membership: " + membershipFee + "\nTotal class fees: "+ classFee + "\nTotal trainer fees: " + trainerFee);

            System.out.println();
            System.out.print("Would you like to bill this person? (1 yes 0 no): ");
            int bill = scan.nextInt();
            if(bill == 0){
                return;
            }

            System.out.println();

            if(membershipFee > 0){
                PreparedStatement memberFee = conn.prepareStatement("UPDATE Members SET memb_fee = 0 WHERE mem_id = ?");
                memberFee.setInt(1, member);
                memberFee.executeUpdate();
                System.out.println("- You have been charged for your membership fee");

            }
            else{
                System.out.println("- Membership fee was payed, no payment required");
            }
            if(trainerFee > 0){
                PreparedStatement trainFee = conn.prepareStatement("UPDATE Trainer_member SET charged = 0 WHERE mem_id = ?");
                trainFee.setInt(1, member);
                trainFee.executeUpdate();
                System.out.println("- You have been charged for your training sessions");

            }
            else{
                System.out.println("- Personal trainer payment not required");
            }
            if(classFee > 0){
                PreparedStatement groupclassFee = conn.prepareStatement("UPDATE Class_member SET charged = 0 WHERE mem_id = ?");
                groupclassFee.setInt(1, member);
                groupclassFee.executeUpdate();
                System.out.println("- You have been charged for your group classes");
            }
            else{
                System.out.println("- Group class payment not required");
            }




        } catch (Exception e) {
            System.out.println(e);
            return;
        }


    }

    //Staff can choose to maintain a chosen equipment which will automatically insert
    //the current date into the Staff_equipment table
    public void equipment(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            Statement get_equip = conn.createStatement();
            get_equip.executeQuery("SELECT * FROM Equipment");
            ResultSet allEquip = get_equip.getResultSet();

            if(!allEquip.isBeforeFirst()){
                System.out.println("There is no equipment, your gym sucks");
                return;
            }
            while (allEquip.next()) {
                System.out.println(allEquip.getInt(1)+ "| " + allEquip.getString(2));
            }
            System.out.println();
            System.out.println("Select which equipment you would like to maintain: ");
            int equipId = scan.nextInt();
            long millis=System.currentTimeMillis();
            Date d =new Date(millis);

            PreparedStatement setDays = conn.prepareStatement("INSERT INTO Staff_equipment VALUES(?, ?, ?)");
            setDays.setInt(1, this.id);
            setDays.setInt(2, equipId);
            setDays.setDate(3, d);
            setDays.executeUpdate();
            System.out.println("Schedule updated: ");

            Statement getSchedule = conn.createStatement();
            getSchedule.executeQuery("SELECT * FROM Staff_equipment");
            ResultSet sched = getSchedule.getResultSet();
            if(!sched.isBeforeFirst()){
                System.out.println("Something went wrong..");
                return;
            }
            while (sched.next()) {
                System.out.println("Staff ID: " + sched.getInt(1)+ "| Equipment ID: " + sched.getInt(2) + " ->Date Maintained: " + sched.getDate(3));
            }

        } catch (Exception e) {
            System.out.println(e);
            //return -1;
        }
    }


    public void display(){
        int choice = -1;
        while(choice != 0){
            System.out.println();
            System.out.println("Menu: ");
            System.out.println("1: Room booking management");
            System.out.println("2: Class Schedule update");
            System.out.println("3: Equipment Maintenance");
            System.out.println("4: Get a bill");

            System.out.println("0: exit");
            choice = scan.nextInt();
            System.out.println();

            switch (choice){
                case 1:
                    roomBooking();
                    break;

                case 2:
                    classSchedule();
                    break;

                case 3:
                    equipment();
                    break;
                case 4:
                    getBill();
                    break;
                case 0:
                    System.out.println("goodbye!");
                    break;
            }

        }



    }

    public int login(int id, String name) {

        this.id = id;
        this.name = name;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(Main.url, Main.user, Main.password);

            String get = "SELECT * FROM Staff WHERE staff_id = ? AND name = ?";
            PreparedStatement getStaff = conn.prepareStatement(get);
            getStaff.setInt(1, this.id);
            getStaff.setString(2, this.name);

            ResultSet result = getStaff.executeQuery();

            if(!result.isBeforeFirst()) {
                System.out.println("you entered your ID or name wrong");
                return -1;
            }
            while (result.next()) {
                System.out.println(name);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();

        return 1;

    }

    //functionality will only show if the login is correct
    public void profileDisplay(int id, String name){
        int login = login(id, name);
        if(login > 0) {
            display();
        }

    }
}
