package Rescue.Disaster.Management.dto;
public class DashboardSummary {
    private long totalEmergencies;
    private long reportedEmergencies;
    private long verifiedEmergencies;
    private long assignedEmergencies;
    private long completedEmergencies;
    private long totalRescueTeams;
    private long availableRescueTeams;
    private long totalCitizens;
    private long totalTeamMembers;
    public DashboardSummary(long totalEmergencies, long reportedEmergencies, long verifiedEmergencies,
            long assignedEmergencies, long completedEmergencies, long totalRescueTeams,
            long availableRescueTeams, long totalCitizens, long totalTeamMembers) {
        this.totalEmergencies = totalEmergencies; this.reportedEmergencies = reportedEmergencies;
        this.verifiedEmergencies = verifiedEmergencies; this.assignedEmergencies = assignedEmergencies;
        this.completedEmergencies = completedEmergencies; this.totalRescueTeams = totalRescueTeams;
        this.availableRescueTeams = availableRescueTeams; this.totalCitizens = totalCitizens;
        this.totalTeamMembers = totalTeamMembers;
    }
    public long getTotalEmergencies() { return totalEmergencies; }
    public long getReportedEmergencies() { return reportedEmergencies; }
    public long getVerifiedEmergencies() { return verifiedEmergencies; }
    public long getAssignedEmergencies() { return assignedEmergencies; }
    public long getCompletedEmergencies() { return completedEmergencies; }
    public long getTotalRescueTeams() { return totalRescueTeams; }
    public long getAvailableRescueTeams() { return availableRescueTeams; }
    public long getTotalCitizens() { return totalCitizens; }
    public long getTotalTeamMembers() { return totalTeamMembers; }
}
