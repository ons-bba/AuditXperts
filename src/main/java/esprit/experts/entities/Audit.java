package esprit.experts.entities;


public class Audit {

    private String Title;
    private String Startdate;
    private String Returndate;
    private String Deficiency;
    private String Report;
    private String Duration;
    private String Status;
    private String Approach;

    public Audit(String title, String startdate, String returndate, String deficiency, String report, String duration, String status, String approach) {
        Title = title;
        Startdate = startdate;
        Returndate = returndate;
        Deficiency = deficiency;
        Report = report;
        Duration = duration;
        Status = status;
        Approach = approach;
    }

    public Audit(int id, String title, String startdate, String returndate, String deficiency, String report, String duration, String status, String approach) {
        this.id = id;
        Title = title;
        Startdate = startdate;
        Returndate = returndate;
        Deficiency = deficiency;
        Report = report;
        Duration = duration;
        Status = status;
        Approach = approach;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String startdate) {
        Startdate = startdate;
    }

    public String getReturndate() {
        return Returndate;
    }

    public void setReturndate(String returndate) {
        Returndate = returndate;
    }

    public String getDeficiency() {
        return Deficiency;
    }

    public void setDeficiency(String deficiency) {
        Deficiency = deficiency;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getApproach() {
        return Approach;
    }

    public void setApproach(String approach) {
        Approach = approach;
    }



}
