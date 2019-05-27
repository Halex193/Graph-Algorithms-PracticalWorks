package activities;

public class Activity
{
    public int vertex;
    public int duration;
    public int minStartTime = 0;
    public int minEndTime = 0;
    public int maxStartTime = 0;
    public int maxEndTime = 0;
    public boolean critical = false;

    public Activity(int vertex, int duration)
    {
        this.vertex = vertex;
        this.duration = duration;
    }
}
