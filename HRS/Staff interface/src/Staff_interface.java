import java.util.Scanner;
import java.io.*;

public class Staff_interface {
    public static void main(String[] args) throws IOException{
        //Staff Log in
        Scanner scan = new Scanner(System.in);
        System.out.print("Input your staff id:");
        String input_id = scan.next();
        System.out.print("Input your password:");
        String input_pass = scan.next();

        //Load the staff list
        String path = "/Users/FH/java/HRS/Staff.csv";
        File file = new File(path);
        if(!file.exists()) throw new RuntimeException("Error:File not found");
        BufferedReader br = new BufferedReader(new FileReader(file));

        Staff[] staff = new Staff[3];
        int i = 0;
        String line;
        br.readLine();
        while((line = br.readLine())!= null){
            String[] st = line.split(",");
            staff[i] = new Staff(st[0],st[1]);
            i++;
        }
        br.close();

        //Check the staff id and password
        int success = 0;
        for(i = 0;i < 3;i++){
            if(staff[i].staff_id.equals(input_id) && staff[i].password.equals(input_pass)){
                success = 1;
                break;
            }
        }
        if(success == 0){
            System.out.println("Log in failed. Please check your id and password again.");
            System.exit(0);
        }
        else System.out.println("Log in completed!");

        //Choose the function
        System.out.println("Input the number of the function you want to use");
        System.out.println("1.Check in");
        System.out.println("2.Check out");
        int choice = scan.nextInt();
        switch (choice){
            case 1: check_in();
            break;
            case 2: check_out();
            break;
        }
    }

    public static void check_in() throws IOException{
        Order order = new Order();
        System.out.println("Please input the reservation number:");
        Scanner scan = new Scanner(System.in);
        String res_num = scan.next();
        System.out.println("The order information is as following, please guide the customer:");
        order.read_order(res_num);
    }

    public static void check_out() throws IOException{
        //Input amount of room
        System.out.println("How many rooms?");
        Scanner scan = new Scanner(System.in);
        int number = scan.nextInt();
        //Input room number
        System.out.println("Input the room number:");
        int[] input_room = new int[10];
        int i;
        for(i = 0;i < number;i++){
            input_room[i] = scan.nextInt();
        }
        //search for reservation number
        String path = "/Users/FH/java/HRS/Rooms.csv";
        File file = new File(path);
        if(!file.exists()) throw new RuntimeException("Error:File not found");
        BufferedReader br = new BufferedReader(new FileReader(file));

        Room[] room = new Room[10];
        String[] res_num = new String[10];
        String line;
        i = 0;
        int j = 0;
        br.readLine();

        while((line = br.readLine())!=null){
            String[] st = line.split(",");
            room[i] = new Room(Integer.parseInt(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]));
            i++;
            if(Integer.parseInt(st[0]) == input_room[j]){
                res_num[j] = st[3];
                if(j > 0){
                    if(res_num[j].equals(res_num[j-1])) res_num[j] = null;
                }
                j++;
            }
        }
        br.close();
        //search for the order and calculate the total payment
        Order order = new Order();
        i = 0;
        int payment = 0;
        while(true) {
            if (res_num[i] == null) break;
            else {
                payment = payment + order.payment(res_num[i]);
                i++;
            }
        }
        //Conduct payment process
        System.out.println("The total payment is:"+payment);
        //Finish payment process
        while(true){
            System.out.println("Enter 1 AFTER finishing the payment:");
            int flag = scan.nextInt();
            if(flag == 1){
                //Delete the reservation file
                for(i = 0;i < 10;i++){
                    if(res_num[i] == null) break;
                    path = "/Users/FH/java/HRS/"+res_num[i]+".txt";
                    file = new File(path);
                    if(file.exists()){
                        if(file.delete()) System.out.println("Reservation file deleted.");
                        else System.out.println("Delete Failed.");
                    }
                    else throw new RuntimeException("Error:File not found");
                }
                //update the room list
                for(i = 0;i < 10;i++){
                    if(room[i].room_num == input_room[i]){
                        room[i].available = 1;
                        res_num[i] = null;
                    }
                }
                path = "/Users/FH/java/HRS/Rooms.csv";
                BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                bw.write("roomNum,price,available,reservationNum\n");
                for(i = 0;i < 10;i++){
                    bw.write(room[i].room_num+","+room[i].price+","+room[i].available+","+res_num[i]+"\n");
                }
                bw.close();
                System.out.println("Check out finished");
                break;
            }
        }
    }
}
