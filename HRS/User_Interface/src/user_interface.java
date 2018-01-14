import java.util.*;
import java.io.*;

public class user_interface{
    public static void main(String args[]) throws IOException{
        //Load the room list
        String path = "/Users/FH/java/HRS/Rooms.csv";
        File file = new File(path);
        if(!file.exists()) throw new RuntimeException("Error:File not found");
        BufferedReader br = new BufferedReader(new FileReader(file));

        Room[] room = new Room[10];
        String[] res_num = new String[10];
        int available_room = 0;
        String line;
        int i = 0;
        br.readLine();

        while((line = br.readLine())!=null){
            String[] st = line.split(",");
            room[i] = new Room(Integer.parseInt(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]));
            res_num[i] = st[3];
            if(room[i].available == 1) available_room++;
            i++;
        }
        br.close();
        //Choose to reserve or cancel order(be done in Ex.6 maybe)
        //User choose to make a reservation
        reserve(room, available_room, res_num);
        //update the room information
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write("roomNum,price,available,reservationNum\n");
        for(i = 0;i < 10;i++){
            bw.write(room[i].room_num+","+room[i].price+","+room[i].available+","+res_num[i]+"\n");
        }
        bw.close();
    }

    public static void reserve(Room[] room, int available_room, String[] res_num) throws  IOException{
        //Create a new user
        User user = new User();
        //Input the user information
        Scanner scan = new Scanner(System.in);
        System.out.print("How many rooms do you want?(1~"+available_room+"):");
        int num_of_room = scan.nextInt();
        //If the number of the room is not enough, print error message and exit
        if(num_of_room > available_room){
            System.out.print("Error: Sorry we don't have so many rooms available. Please try later.");
            System.exit(0);
        }
        System.out.print("Input your last name:");
        user.last_name = scan.next();
        System.out.print("Input your first name:");
        user.first_name = scan.next();
        System.out.print("Input your phone number:");
        user.phone_num = scan.next();
        //Save the information to the order
        Order order = new Order(user.first_name, user.last_name, user.phone_num, num_of_room);
        //Input date
        System.out.print("Input check in date(eg.20180101):");
        order.check_in = scan.nextInt();
        System.out.print("Input check out date(eg.20180102):");
        order.check_out = scan.nextInt();
        //Print and check order
        order.print_order();
        System.out.println("If the information is all correct,please enter 1; ");
        System.out.println("If some of the information is wrong,please enter 0: ");
        int flag = scan.nextInt();
        if(flag == 1){
            //Save the room number into the order
            int counter = 0;
            for(int i = 0;i < 10;i++){
                if(room[i].available == 1){
                    order.room[counter] = room[i];
                    order.total_price += order.room[counter].price;
                    room[i].available = 0;
                    res_num[i] = order.reservation_num;
                    counter++;
                }
                if(counter == num_of_room) break;
            }
            order.save_order();
            System.out.println("Your reservation is finished. Thank you!");
        }
        else{
            System.out.print("Sorry please run the program from the beginning again.");
            System.exit(0);
        }
    }
}
