/**
 * CREATING A THREAD BY EXTENDING THE Thread CLASS
 * -----------------------------------------------
 * A RescueTeamThread IS a Thread (inheritance). The work the thread
 * does goes inside run(). We never call run() ourselves; we call
 * start(), and Java creates a new thread that then calls run() for us.
 */
public class RescueTeamThread extends Thread {

    private RescueTeam team;
    private String emergencyId;

    public RescueTeamThread(RescueTeam team, String emergencyId) {
        super(team.getTeamName());      // give the thread a name (shows in the output)
        this.team = team;
        this.emergencyId = emergencyId;
    }

    // run() = the code that executes in the NEW thread.
    @Override
    public void run() {
        try {
            team.updateStatus("En Route");
            log("is En Route to " + emergencyId);
            Thread.sleep(300);          // sleep(): pause THIS thread for 300 ms

            team.updateStatus("In Action");
            log("is In Action at " + emergencyId);
            Thread.sleep(300);

            team.updateStatus("Completed");
            log("has Completed " + emergencyId);
        } catch (InterruptedException e) {
            // sleep() throws this checked exception if another thread interrupts us
            log("was interrupted");
        }
    }

    // Prints which thread is actually running this code.
    private void log(String message) {
        System.out.println("   [" + Thread.currentThread().getName() + "] " + message);
    }
}