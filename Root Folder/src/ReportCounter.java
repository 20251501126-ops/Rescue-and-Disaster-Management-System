/**
 * SHARED DATA FOR THE SYNCHRONIZATION DEMO
 * ----------------------------------------
 * Imagine many citizens reporting at the same moment, and every
 * report adds 1 to one shared total.
 *
 * "total++" looks like one step but is really THREE: read the value,
 * add 1, write it back. If two threads do this at the same time they
 * can read the same old value and one update is LOST. That problem is
 * called a RACE CONDITION.
 */
public class ReportCounter {

    private int total = 0;

    // NOT thread-safe: threads can overlap and lose updates.
    public void unsafeIncrement() {
        total++;
    }

    // THREAD-SAFE: "synchronized" lets only ONE thread at a time run
    // this method on this object (the object's lock). Others wait.
    public synchronized void safeIncrement() {
        total++;
    }

    public int getTotal() {
        return total;
    }
}