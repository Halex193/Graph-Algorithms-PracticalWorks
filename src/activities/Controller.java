package activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import graphs.DirectedGraph;

class Controller
{

    private DirectedGraph directedGraph;
    private Map<Integer, Activity> activities;

    public Controller(String activitiesFileName)
    {
        try
        {
            readDAG(activitiesFileName);
        }
        catch (FileNotFoundException e)
        {
            this.directedGraph = new DirectedGraph();
            activities = new HashMap<>();
        }
    }

    public DTOProject createProject()
    {
        List<Integer> order = topologicalSort();
        if (order == null)
            return null;
        List<Activity> activityList = order.stream()
                                           .map(integer -> activities.get(integer))
                                           .collect(Collectors.toList());
        generateTimes(activityList);
        activityList.remove(0);
        Activity last = activityList.remove(activityList.size() - 1);
        return new DTOProject(activityList, last.minEndTime);
    }

    private void generateTimes(List<Activity> activityList)
    {
        for (int i = 1; i < activityList.size(); i++)
        {
            Activity activity = activityList.get(i);
            activity.minStartTime = 0;
            for (int dependency : directedGraph.parseInboundEdges(activity.vertex))
            {
                int dependencyMinEndTime = activities.get(dependency).minEndTime;
                if (dependencyMinEndTime > activity.minStartTime)
                {
                    activity.minStartTime = dependencyMinEndTime;
                }
            }
            activity.minEndTime = activity.minStartTime + activity.duration;
        }

        Activity last = activityList.get(activityList.size() - 1);
        last.maxEndTime = last.minEndTime;
        last.maxStartTime = last.minStartTime;

        for (int i = activityList.size() - 2; i >= 0; i--)
        {
            Activity activity = activityList.get(i);
            activity.maxEndTime = last.maxStartTime;
            for (int dependency : directedGraph.parseOutboundEdges(activity.vertex))
            {
                int dependencyMaxStartTime = activities.get(dependency).maxStartTime;
                if (dependencyMaxStartTime < activity.maxEndTime)
                {
                    activity.maxEndTime = dependencyMaxStartTime;
                }
            }
            activity.maxStartTime = activity.maxEndTime - activity.duration;

            if (activity.maxStartTime == activity.minStartTime)
                activity.critical = true;
        }
    }

    private List<Integer> topologicalSort()
    {
        int vertexNumber = directedGraph.getNumberOfVertices();
        List<Integer> sorted = new ArrayList<>(vertexNumber);
        Set<Integer> fullyProcessed = new HashSet<>(vertexNumber);
        Set<Integer> inProcess = new HashSet<>(vertexNumber);
        for (Integer vertex : directedGraph.parseVertices())
        {
            if (!fullyProcessed.contains(vertex))
            {
                boolean ok = topologicalSortDFS(vertex, sorted, fullyProcessed, inProcess);
                if (!ok)
                    return null;
            }
        }
        return sorted;
    }

    private boolean topologicalSortDFS(
            Integer sourceVertex,
            List<Integer> sorted,
            Set<Integer> fullyProcessed,
            Set<Integer> inProcess
    )
    {
        inProcess.add(sourceVertex);
        for (Integer dependency : directedGraph.parseInboundEdges(sourceVertex))
        {
            if (inProcess.contains(dependency))
                return false;
            else if (!fullyProcessed.contains(dependency))
            {
                boolean ok = topologicalSortDFS(dependency, sorted, fullyProcessed, inProcess);
                if (!ok)
                    return false;
            }
        }
        inProcess.remove(sourceVertex);
        sorted.add(sourceVertex);
        fullyProcessed.add(sourceVertex);
        return true;
    }

    private void readDAG(String activitiesFileName) throws FileNotFoundException
    {
        File file = new File(activitiesFileName);
        if (!file.exists())
        {
            throw new FileNotFoundException();
        }
        Scanner scanner = new Scanner(file);
        int vertexNumber = scanner.nextInt();
        scanner.nextLine();
        directedGraph = new DirectedGraph(vertexNumber, 0);
        activities = new HashMap<>(vertexNumber);
        for (int i = 0; i < vertexNumber; i++)
        {
            String[] activityValues = scanner.nextLine().split(" ");
            int vertex = Integer.valueOf(activityValues[0]);
            int duration = Integer.valueOf(activityValues[1]);
            activities.put(vertex, new Activity(vertex, duration));
            for (int j = 2; j < activityValues.length; j++)
            {
                int dependency = Integer.valueOf(activityValues[j]);
                directedGraph.addEdge(dependency, vertex, 0);
            }
        }
        scanner.close();

        directedGraph.addVertex(-1);
        directedGraph.addVertex(-2);
        activities.put(-1, new Activity(-1, 0));
        activities.put(-2, new Activity(-2, 0));
        for (int vertex : directedGraph.parseVertices())
        {
            if (vertex != -1 && vertex != -2)
            {
                if (directedGraph.inDegree(vertex) == 0)
                {
                    directedGraph.addEdge(-1, vertex, 0);
                }
                if (directedGraph.outDegree(vertex) == 0)
                {
                    directedGraph.addEdge(vertex, -2, 0);
                }
            }
        }
    }

    public class DTOProject
    {
        public List<Activity> activityList;
        public int minEndTime;

        public DTOProject(List<Activity> activityList, int minEndTime)
        {
            this.activityList = activityList;
            this.minEndTime = minEndTime;
        }
    }
}
