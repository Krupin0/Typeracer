package typeracer.typeracer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Server {
    public static ArrayList<PrintWriter> writers = new ArrayList<>();
    public  static TreeMap pocetOdeslanychSlov = new TreeMap();
    public static int pocetPripojeni = 0;
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Server jede - čekám na klienty");
        ServerSocket serverSocket = new ServerSocket(55555);
        while (true){
            Socket clientSocket = serverSocket.accept();
            if (pocetPripojeni<2){
                System.out.println("Nový klient!");
                writers.add(new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                new ClientConnHandle(clientSocket).start();
                pocetPripojeni++;
                if(pocetPripojeni == 2){
                    Thread.sleep(1000);
                    sendToAll("start3");
                    Thread.sleep(1000);
                    sendToAll("start2");
                    Thread.sleep(1000);
                    sendToAll("start1");
                    Thread.sleep(1000);
                    sendToAll("text:" + Server.randomText() + " ");
                }
            }
        }

    }

    private static String randomText() {
        String s1 = "Jak se pamatuji, Adame, bylo to tak: - odkázal mi poslední vůlí jenom chatrných tisíc korun a jak pravíš, uložil mému bratru pod ztrátou požehnání, aby mne dobře vychoval; a tím začínají moje strasti. Mého bratra Jaquesa dal do škol, a pověst mluví skvěle o jeho zdatnosti;";
        String s2 = "Jdi do kláštera! Proč bys chtěla rodit hříšníky? Nejsem zrovna ten nejhorší, a přece toho na sebe vím, žeby bylo lépe, kdyby mě matka nebyla povila. Jsem velmi pyšný, mstivý, ctižádostivý.";
        String s3 = "Vidím, že jsi ochoten; a tupější bys byl než tučné bejlí, jež líně uhnívá na břehu Lethe, to kdyby k činu nepohnulo tě.";
        String s4 = "Semť tvého otce duch a odsouzen a jistou dobu v noci obcházet za dne, vězněn, lačnět v plamenech, až podlé viny mojí časnosti se očistí a shoří. - Ó, že jest mi zakázáno taje vyprávět své věznice!";
        String s5 = "Za víc to neměj; neboť příroda když rozvíjí se, nezmohutňuje se pouze ve svalech a v objemu, leč jak se zveličuje tento chrám, i vnitřní služba myšlení a srdce s ním roste v šíř.";

        Random rn = new Random();
        int cisloPredlohy = rn.nextInt(5);
        String[] predlohy = {s1, s2, s3, s4, s5};
        return predlohy[cisloPredlohy];
    }

    //Odesílání zpráv
    public static void sendToAll(String text){
        if(text.startsWith("start") || text.startsWith("text")){ //Řídící zprávy - start za kolik, generování textu
            for (PrintWriter writer:writers) {
                writer.println(text);
                writer.flush();
            }
        }
        else{   //Informace o skóre
            String[] separated = text.split(":");
            String celek = "";
            pocetOdeslanychSlov.put(separated[0], separated[1]);
            for (PrintWriter writer:writers) {
                pocetOdeslanychSlov.forEach((key, value) -> {
                    writer.print(key + ":" + value);
                    writer.print("#");
                });
                writer.println("");
                writer.flush();
            }
        }
    }
}