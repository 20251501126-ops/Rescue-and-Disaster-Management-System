/**
 * SHARED RESOURCE: a limited number of rescue boats.
 * Several rescue teams (threads) ask for a boat at the same time.
 * The check "is a boat free?" and the update "take it" must happen
 * together as ONE step, otherwise two teams can both see "1 boat
 * left" and both take it (the pool goes negative).
 */
public class BoatPool {

    private int availableBoats;

    public BoatPool(int availableBoats) {
        this.availableBoats = availableBoats;
    }

    // UNSAFE version: check and take are separate, so threads interleave.
    public boolean allocateUnsafe(String teamName) {
        if (availableBoats > 0) {
            pause();                    // widens the gap so the problem shows reliably
            availableBoats--;
            return true;
        }
        return false;
    }

    // SAFE version: synchronized makes check + take one indivisible step.
    public synchronized boolean allocate(String teamName) {
        if (availableBoats > 0) {
            pause();
            availableBoats--;
            return true;
        }
        return false;
    }

    public synchronized int getAvailableBoats() {
        return availableBoats;
    }

    private void pause() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // ignored in this demo
        }
    }
}