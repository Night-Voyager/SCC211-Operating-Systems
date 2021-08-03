import java.util.Scanner;

class Philosopher extends Thread {
    private final int id;
    private final Forks forks;

    public Philosopher(int id, Forks forks) {
        this.id = id;
        this.forks = forks;
    }

    @Override
    public void run() {
        while (true) {
            think();
            getForks();
            eat();
            putForks();
        }
    }

    public void getForks() {
        forks.getForks(id);
    }

    public void putForks() {
        forks.putForks(id);
    }

    public void think() {
        System.out.println("Philosopher " + getThreadName() + " is thinking.");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void eat() {
        System.out.println("Philosopher " + getThreadName() + " is eating.");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getThreadName() {
        return currentThread().getName();
    }
}

class Forks {
    private static boolean [] isUsed;
    private static int num_of_forks;

    public Forks(int n) {
        num_of_forks = n;
        isUsed = new boolean[num_of_forks];
        for (int i = 0; i < num_of_forks; i++) {
            isUsed[i] = false;
        }
    }

    public synchronized void getForks(int philosopherID) {
        while (isUsed[philosopherID] || isUsed[(philosopherID+1)%num_of_forks]) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isUsed[philosopherID] = true;
        isUsed[(philosopherID+1)%num_of_forks] = true;
        System.out.println("Philosopher " + Thread.currentThread().getName() + " gets the forks.");
    }

    public synchronized void putForks(int philosopherID) {
        isUsed[philosopherID] = false;
        isUsed[(philosopherID+1)%num_of_forks] = false;
        System.out.println("Philosopher " + Thread.currentThread().getName() + " puts down the forks.");
        notifyAll();
    }
}

public class CW2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Please input the number of philosophers: ");
        int n = in.nextInt();

        Forks forks = new Forks(n);

        for (int i = 0; i < n; i++) {
            new Philosopher(i, forks).start();
        }
    }
}
