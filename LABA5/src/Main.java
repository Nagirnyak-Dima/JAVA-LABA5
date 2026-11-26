import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- МЕНЮ (5 ЛАБА) ---");
            System.out.println("1. Завдання 1 (State, ArrayList, Comparable, Comparator, файл)");
            System.out.println("2. Завдання 2 (Різні англійські слова через HashSet)");
            System.out.println("0. Вихід");
            System.out.print("Вибір: ");

            String choice = sc.nextLine().trim();
            if (choice.equals("1")) {
                task1();
            } else if (choice.equals("2")) {
                task2();
            } else if (choice.equals("0")) {
                break;
            } else {
                System.out.println("Невірний вибір.");
            }
        }
    }

    static void task1() {
        ArrayList<Republic> republics = readRepublics("republics.txt");
        ArrayList<Monarchy> monarchies = readMonarchies("monarchies.txt");
        ArrayList<Kingdom> kingdoms = readKingdoms("kingdoms.txt");

        System.out.println("\n--- Прочитані дані ---");
        printAll(republics);
        printAll(monarchies);
        printAll(kingdoms);

        Collections.sort(republics);
        Collections.sort(monarchies);
        Collections.sort(kingdoms);

        System.out.println("\n--- Відсортовані (Comparable - за населенням) ---");
        printAll(republics);
        printAll(monarchies);
        printAll(kingdoms);

        System.out.println("\n--- Додавання записів з клавіатури ---");
        System.out.print("Республіка (назва населення президент): ");
        republics.add(new Republic(sc.next(), sc.nextLong(), sc.next()));
        sc.nextLine();

        System.out.print("Монархія (назва населення титул): ");
        monarchies.add(new Monarchy(sc.next(), sc.nextLong(), sc.next()));
        sc.nextLine();

        System.out.print("Королівство (назва населення титул династія): ");
        kingdoms.add(new Kingdom(sc.next(), sc.nextLong(), sc.next(), sc.next()));
        sc.nextLine();

        Collections.sort(republics);
        Collections.sort(monarchies);
        Collections.sort(kingdoms);

        System.out.println("\n--- Після додавання і сортування ---");
        printAll(republics);
        printAll(monarchies);
        printAll(kingdoms);

        ArrayList<State> all = new ArrayList<>();
        all.addAll(republics);
        all.addAll(monarchies);
        all.addAll(kingdoms);

        Collections.sort(all, new StateComparator());

        System.out.println("\n--- Загальний список (Comparator - за назвою) ---");
        printAll(all);

        System.out.print("Назва вихідного файлу: ");
        String outFile = sc.nextLine().trim();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            for (State s : all) {
                bw.write(s.toString());
                bw.newLine();
            }
            System.out.println("Записано у " + outFile);
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }

    static void task2() {
        System.out.print("Введіть шлях до англійського файлу: ");
        String fileName = sc.nextLine().trim();

        HashSet<String> words = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.toLowerCase().split("[^a-z']+");
                for (String w : parts) {
                    if (!w.isEmpty()) {
                        words.add(w);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
            return;
        }

        System.out.println("Різних слів: " + words.size());
        for (String w : words) {
            System.out.println(w);
        }
    }

    static ArrayList<Republic> readRepublics(String file) {
        ArrayList<Republic> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 3) {
                    list.add(new Republic(p[0].trim(), Long.parseLong(p[1].trim()), p[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка " + file + ": " + e.getMessage());
        }
        return list;
    }

    static ArrayList<Monarchy> readMonarchies(String file) {
        ArrayList<Monarchy> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 3) {
                    list.add(new Monarchy(p[0].trim(), Long.parseLong(p[1].trim()), p[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка " + file + ": " + e.getMessage());
        }
        return list;
    }

    static ArrayList<Kingdom> readKingdoms(String file) {
        ArrayList<Kingdom> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 4) {
                    list.add(new Kingdom(p[0].trim(), Long.parseLong(p[1].trim()), p[2].trim(), p[3].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка " + file + ": " + e.getMessage());
        }
        return list;
    }

    static <T> void printAll(ArrayList<T> list) {
        for (T item : list) {
            System.out.println("  " + item);
        }
    }
}

abstract class State {
    String name;
    long population;

    State(String name, long population) {
        this.name = name;
        this.population = population;
    }
}

class Republic extends State implements Comparable<Republic> {
    String president;

    Republic(String name, long population, String president) {
        super(name, population);
        this.president = president;
    }

    @Override
    public int compareTo(Republic o) {
        return Long.compare(this.population, o.population);
    }

    @Override
    public String toString() {
        return "Republic: " + name + ", " + population + ", " + president;
    }
}

class Monarchy extends State implements Comparable<Monarchy> {
    String title;

    Monarchy(String name, long population, String title) {
        super(name, population);
        this.title = title;
    }

    @Override
    public int compareTo(Monarchy o) {
        return Long.compare(this.population, o.population);
    }

    @Override
    public String toString() {
        return "Monarchy: " + name + ", " + population + ", " + title;
    }
}

class Kingdom extends State implements Comparable<Kingdom> {
    String title;
    String house;

    Kingdom(String name, long population, String title, String house) {
        super(name, population);
        this.title = title;
        this.house = house;
    }

    @Override
    public int compareTo(Kingdom o) {
        return Long.compare(this.population, o.population);
    }

    @Override
    public String toString() {
        return "Kingdom: " + name + ", " + population + ", " + title + ", " + house;
    }
}

class StateComparator implements Comparator<State> {
    @Override
    public int compare(State a, State b) {
        return a.name.compareToIgnoreCase(b.name);
    }
}