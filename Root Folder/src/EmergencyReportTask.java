/**
 * CREATING A THREAD BY IMPLEMENTING THE Runnable INTERFACE
 * --------------------------------------------------------
 * Runnable has just one method: run(). A class that implements it
 * only describes the TASK. To execute it, we hand it to a Thread:
 *
 *     new Thread(new EmergencyReportTask(...)).start();
 *
 * Why use Runnable instead of extending Thread? Java allows only ONE
 * parent class, so a class that already extends something else (or
 * that you want to keep free to extend later) can still be a task by
 * implementing the interface.
 */
public class EmergencyReportTask implements Runnable {

    private String citizenName;
    private String type;
    private String location;

    public EmergencyReportTask(String citizenName, String type, String location) {
        this.citizenName = citizenName;
        this.type = type;
        this.location = location;
    }

    @Override
    public void run() {
        Emergency emergency = new Emergency("E-" + citizenName, type, location);
        System.out.println("   [" + Thread.currentThread().getName() + "] "
                + citizenName + " reported -> " + emergency.getSummary());
    }
}