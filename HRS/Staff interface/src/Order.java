import java.io.*;
import java.util.Random;

public class Order {
    String reservation_num;
    String reservation_name;
    String reservation_phone_num;
    int num_of_room;
    int check_in;
    int check_out;
    Room[] room;
    int total_price;
    public Order(){
        reservation_name = null;
        reservation_phone_num = null;
        reservation_num = null;
        num_of_room = 0;
        check_in = 0;
        check_out = 0;
        room = null;
        total_price = 0;
    }
    public Order(String f_name, String l_name, String phoneNo, int amount){
        Random random = new Random();
        reservation_name = l_name + " " + f_name;
        reservation_phone_num = phoneNo;
        reservation_num = phoneNo + l_name.charAt(0) + f_name.charAt(0)+String.valueOf(random.nextInt(99));
        num_of_room = amount;
        check_in = 0;
        check_out = 0;
        room = new Room[num_of_room];
        total_price = 0;
    }
    public void print_order(){
        System.out.println("Please confirm the order information.");
        System.out.println("Reservation number: " + reservation_num);
        System.out.println("Name: " + reservation_name);
        System.out.println("Phone number: " + reservation_phone_num);
        System.out.println("Number of room: " + num_of_room);
        System.out.println("Check in date: " + check_in);
        System.out.println("Check out date: " + check_out);
    }

    public void save_order() throws IOException{
        String path = "/Users/FH/java/HRS/"+reservation_num+".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write("Name:"+reservation_name+"\n");
        bw.write("Phone number:"+reservation_phone_num+"\n");
        bw.write("Number of room:"+num_of_room+"\n");
        bw.write("Check in date:"+check_in+"\n");
        bw.write("Check out date:"+check_out+"\n");
        bw.write("Room number(s):");
        for(int i = 0;i < num_of_room;i++){
            bw.write(room[i].room_num + " ");
        }
        bw.write("\nTotal payment:"+total_price);
        bw.close();
    }

    public void read_order(String res_num) throws IOException{
        String path = "/Users/FH/java/HRS/" + res_num + ".txt";
        File file = new File(path);
        if(!file.exists()) throw new RuntimeException("Error:File not found");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while((line = br.readLine()) != null){
            System.out.println(line);
        }
        br.close();
    }

    public int payment(String res_num) throws IOException{
        int pay = 0;
        String path = "/Users/FH/java/HRS/" + res_num + ".txt";
        File file = new File(path);
        if(!file.exists()) throw new RuntimeException("Error:File not found");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while((line = br.readLine()) != null){
            String[] st = line.split(":");
            if(st[0].equals("Total payment")) pay = Integer.parseInt(st[1]);
        }
        br.close();
        return pay;
    }
}

