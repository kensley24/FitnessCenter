import java.sql.*;
import java.util.Scanner;


public class Main{
    public static String url;
    public static String user;
    public static String password;

    public Main(){
        this.url = url;
        this.user = user;
        this.password = password;

        //user = "postgres";
        //password = "passwrd";
    }
    public static void main(String[] args){

        url = "jdbc:postgresql://localhost:5432/FitnessClub";

        Scanner scan = new Scanner(System.in);

        System.out.println("postgresql username: ");
        user = scan.next();
        System.out.println("postgresql password: ");
        password = scan.next();
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully");
        }
        catch(Exception e){
                System.out.println(e);
                System.out.println("not connected successfully");
                return;
        }

        int choice = -1;

        System.out.println("------------WELCOME------------");
        while(choice != 3){
            System.out.print("Would you like to: \n1) login \n2) signup \n3) exit \nselection: ");
            choice = scan.nextInt();

            if(choice == 1){
                System.out.println();
                System.out.print("Select \n1 to login as a member\n2 to login as a trainer\n3 to login as a staff \nselection: ");
                int select = scan.nextInt();
                System.out.println();
                System.out.print("id: ");
                int id = scan.nextInt();
                System.out.println("name: ");
                String name = scan.next();


                switch(select){
                    case 1:
                        Member one = new Member();
                        one.profileDisplay(id, name);
                        break;

                    case 2:
                        Trainer t = new Trainer();
                        t.profileDisplay(id, name);
                        break;

                    case 3:
                        Staff s = new Staff();
                        s.profileDisplay(id, name);
                        break;
                }
            }
            if(choice == 2){
                System.out.println();
                System.out.print("Select \n1 to sign-up as a member \n2 to sign-up as a trainer \n3 to sign-up as a staff \nSelection: ");
                int sel = scan.nextInt();

                switch(sel){
                    case 1:
                        Member one = new Member();
                        one.signup();
                        break;

                    case 2:
                        Trainer t = new Trainer();
                        t.signup();
                        break;

                    case 3:
                        Staff s = new Staff();
                        s.signup();
                        break;
                }
            }
        }


    }
}