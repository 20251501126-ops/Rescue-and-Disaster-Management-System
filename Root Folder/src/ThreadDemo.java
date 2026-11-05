/**
 * MULTITHREADING DEMO  (run with:  java ThreadDemo)
 * Uses the same project classes: RescueTeam, Emergency.
 * Output lines from different threads can appear in a different order
 * each run; that is normal because threads run at the same time.
 */
public class ThreadDemo {

    // ---------- small Runnable tasks used by the demo ----------

    // yield() demo task
    static class YieldTask implements Runnable {
        private String label;

        YieldTask(String label) {
            this.label = label;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println("   [" + label + "] step " + i);
                Thread.yield();         // hint: "I can pause, let another thread run"
            }
        }
    }

    // Holds a lock for some time (used to show the BLOCKED state)
    static class LockTask implements Runnable {
        private final Object lock;
        private final long holdMillis;

        LockTask(Object lock, long holdMillis) {
            this.lock = lock;
            this.holdMillis = holdMillis;
        }

        @Override
        public void run() {
            synchronized (lock) {       // only one thread can be inside at a time
                try {
                    Thread.sleep(holdMillis);
                } catch (InterruptedException e) {
                    // ignored in this demo
                }
            }
        }
    }

    // Adds 1 to the shared counter many times
    static class CounterTask implements Runnable {
        private ReportCounter counter;
        private boolean safe;
        private int times;

        CounterTask(ReportCounter counter, boolean safe, int times) {
            this.counter = counter;
            this.safe = safe;
            this.times = times;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                if (safe) {
                    counter.safeIncrement();
                } else {
                    counter.unsafeIncrement();
                }
            }
        }
    }

    // A team asking for a boat
    static class BoatRequestTask implements Runnable {
        private BoatPool pool;
        private boolean safe;

        BoatRequestTask(BoatPool pool, boolean safe) {
            this.pool = pool;
            this.safe = safe;
        }

        @Override
        public void run() {
            String team = Thread.currentThread().getName();
            boolean got = safe ? pool.allocate(team) : pool.allocateUnsafe(team);
            System.out.println("   [" + team + "] " + (got ? "got a boat" : "no boat left"));
        }
    }

    private static void heading(String text) {
        System.out.println("\n=== " + text + " ===");
    }

    private static void runBoatDemo(boolean safe) throws InterruptedException {
        BoatPool pool = new BoatPool(3);                  // only 3 boats...
        Thread[] teams = new Thread[5];                   // ...but 5 teams ask
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Thread(new BoatRequestTask(pool, safe), "Team-" + (i + 1));
        }
        for (Thread t : teams) {
            t.start();
        }
        for (Thread t : teams) {
            t.join();
        }
        System.out.println("   Boats left in pool: " + pool.getAvailableBoats()
                + (pool.getAvailableBoats() < 0 ? "   <-- impossible! (race condition)" : ""));
    }

    private static void runCounterDemo(boolean safe) throws InterruptedException {
        ReportCounter counter = new ReportCounter();
        Thread[] threads = new Thread[4];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new CounterTask(counter, safe, 100000));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("   Expected total = 400000, actual total = " + counter.getTotal());
    }

    public static void main(String[] args) throws InterruptedException {

        // ---------------------------------------------------------------
        heading("1) Extending Thread + life cycle + start() / sleep() / join()");
        RescueTeam alpha = new RescueTeam("RT01", "Alpha Rescue Team");
        RescueTeamThread teamThread = new RescueTeamThread(alpha, "E001");

        System.out.println("   State before start():   " + teamThread.getState());   // NEW
        teamThread.start();                       // creates the thread; run() now executes in it
        Thread.sleep(100);                        // main thread sleeps; team thread is working
        System.out.println("   State during its sleep: " + teamThread.getState());   // TIMED_WAITING
        teamThread.join();                        // main WAITS until the team thread finishes
        System.out.println("   State after it ends:    " + teamThread.getState());   // TERMINATED
        System.out.println("   Team status now: " + alpha.getTeamStatus());

        // ---------------------------------------------------------------
        heading("2) start() versus calling run() directly");
        RescueTeam bravo = new RescueTeam("RT02", "Bravo Rescue Team");
        System.out.println("   Calling run() directly -> runs on the MAIN thread, no new thread:");
        new RescueTeamThread(bravo, "E002").run();

        RescueTeam charlie = new RescueTeam("RT03", "Charlie Rescue Team");
        System.out.println("   Calling start() -> runs on its OWN thread:");
        RescueTeamThread charlieThread = new RescueTeamThread(charlie, "E003");
        charlieThread.start();
        charlieThread.join();

        // ---------------------------------------------------------------
        heading("3) Implementing Runnable (two citizens report at the same time)");
        Thread citizen1 = new Thread(new EmergencyReportTask("Rahul", "Flood", "Vadodara"), "Citizen-1");
        Thread citizen2 = new Thread(new EmergencyReportTask("Meera", "Fire", "Surat"), "Citizen-2");
        citizen1.start();
        citizen2.start();
        citizen1.join();
        citizen2.join();

        // ---------------------------------------------------------------
        heading("4) yield()  (only a hint; the order of lines can change between runs)");
        Thread dispatcherA = new Thread(new YieldTask("Dispatcher-A"));
        Thread dispatcherB = new Thread(new YieldTask("Dispatcher-B"));
        dispatcherA.start();
        dispatcherB.start();
        dispatcherA.join();
        dispatcherB.join();

        // ---------------------------------------------------------------
        heading("5) Life cycle: the BLOCKED state");
        Object lock = new Object();
        Thread holder = new Thread(new LockTask(lock, 500));
        Thread waiter = new Thread(new LockTask(lock, 0));
        holder.start();
        Thread.sleep(100);                        // let holder grab the lock first
        waiter.start();
        Thread.sleep(100);
        System.out.println("   holder state: " + holder.getState());   // TIMED_WAITING (sleeping inside the lock)
        System.out.println("   waiter state: " + waiter.getState());   // BLOCKED (waiting for the lock)
        holder.join();
        waiter.join();
        System.out.println("   both finished: " + holder.getState() + " / " + waiter.getState());

        // ---------------------------------------------------------------
        heading("6) Synchronization: shared report counter");
        System.out.println("   WITHOUT synchronized (4 threads x 100000 reports):");
        runCounterDemo(false);
        System.out.println("   WITH synchronized:");
        runCounterDemo(true);

        // ---------------------------------------------------------------
        heading("7) Synchronization: 5 teams, only 3 boats");
        System.out.println("   WITHOUT synchronized:");
        runBoatDemo(false);
        System.out.println("   WITH synchronized:");
        runBoatDemo(true);
    }
}