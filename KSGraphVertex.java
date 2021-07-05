import java.lang.*;
import java.security.InvalidParameterException;
import java.util.*;

public class KSGraphVertex
{
    private String __vertexName;
    private ArrayList<KSGraphEdge> __outgoingEdges;
    private ArrayList<KSGraphEdge> __incomingEdges;

    public KSGraphVertex()
    {
        this.__vertexName = "Undefined";
        this.__outgoingEdges = new ArrayList<KSGraphEdge>();
        this.__incomingEdges = new ArrayList<KSGraphEdge>();
    }

    public KSGraphVertex(String vertexName)
    {
        this.__vertexName = vertexName;
        this.__outgoingEdges = new ArrayList<KSGraphEdge>();
        this.__incomingEdges = new ArrayList<KSGraphEdge>();
    }

    public String GetVertexName()
    {
        return this.__vertexName;
    }

    public ArrayList<KSGraphEdge> GetIncomingEdges()
    {
        return this.__incomingEdges;
    }

    public ArrayList<KSGraphEdge> GetOutgoingEdges()
    {
        return this.__outgoingEdges;
    }

    public void AddEdge(KSGraphEdge newEdge) throws InvalidParameterException
    {
        if (newEdge!=null)
        {
            this.__outgoingEdges.add(newEdge);
        }
        else
        {
            throw new InvalidParameterException("The newEdge parameters was not properly est");
            // we should throw an exception
        }
    }

    public void Isolate()
    {
        for(KSGraphEdge currentEdge : this.__outgoingEdges)
        {
            currentEdge.GetTargetVertex().GetIncomingEdges().remove(currentEdge);
        }
        this.__outgoingEdges.clear();;

        for(KSGraphEdge currentEdge : this.__incomingEdges)
        {
            currentEdge.GetSourceVertex().GetOutgoingEdges().remove(currentEdge);
        }
        this.__incomingEdges.clear();;
    }

    public void ConnectToTarget(KSGraphVertex targetVertex)
    {
        if (targetVertex!=null)
        {
            KSGraphEdge newEdge = new KSGraphEdge(this, targetVertex);
            this.__outgoingEdges.add(newEdge);
            targetVertex.ConnectFromSource(newEdge);
        }
        else
        {
            // we should throw an exception
        }
    }

    public void ConnectFromSource(KSGraphEdge newEdgeFromSource)
    {
        if(newEdgeFromSource!=null)
        {
            this.__incomingEdges.add(newEdgeFromSource);
        }
        else
        {
            // we should throw an exception

        }
    }

    public ArrayList<KSGraphVertex> ShortestPathUnWeighted(KSGraphVertex targetVertex)
    {
        Queue<KSGraphVertexWeighted> vertexQueue = new LinkedList<KSGraphVertexWeighted>();
        HashMap<String, KSGraphVertexBackTrack> queuedVertexes = new HashMap<String, KSGraphVertexBackTrack>();
        //ArrayList<ArrayList<KSGraphVertex>> paths = new ArrayList<ArrayList<KSGraphVertex>>();
        //ArrayList<KSGraphVertex> path = new ArrayList<KSGraphVertex>();
        KSGraphVertexWeighted clonedGraphVertexWeighted = new KSGraphVertexWeighted(this);
        queuedVertexes.put(clonedGraphVertexWeighted.GetVertexName(), new KSGraphVertexBackTrack(clonedGraphVertexWeighted));

        vertexQueue.add(clonedGraphVertexWeighted);

        while(!vertexQueue.isEmpty())
        {
            KSGraphVertexWeighted currentVertex = vertexQueue.remove();
            KSGraphVertexWeighted newGraphVertex = null;
            KSGraphVertexBackTrack newGraphVertexBackTrack = null;

            for(KSGraphEdge edge : currentVertex.GetOutgoingEdges())
            {
                if(queuedVertexes.containsKey(edge.GetTargetVertex().GetVertexName()))
                {
                    newGraphVertexBackTrack = queuedVertexes.get(edge.GetTargetVertex().GetVertexName());
                    newGraphVertex = newGraphVertexBackTrack.GetTargetVertex();
                    if(newGraphVertex.GetVertexWeight()>currentVertex.GetVertexWeight()+1)
                    {
                        newGraphVertex.SetVertexWeight(currentVertex.GetVertexWeight()+1);
                        newGraphVertexBackTrack.GetBackTrackList().clear();
                        newGraphVertexBackTrack.GetBackTrackList().addAll(queuedVertexes.get(currentVertex.GetVertexName()).GetBackTrackList());
                        newGraphVertexBackTrack.GetBackTrackList().add(currentVertex);
                    }
                }
                else
                {
                    newGraphVertex = (KSGraphVertexWeighted) edge.GetTargetVertex();
                    KSGraphVertexBackTrack backTrack = new KSGraphVertexBackTrack(newGraphVertex);
                    backTrack.GetBackTrackList().addAll(queuedVertexes.get(currentVertex.GetVertexName()).GetBackTrackList());
                    backTrack.GetBackTrackList().add(currentVertex);
                    queuedVertexes.put(newGraphVertex.GetVertexName(), backTrack);
                    newGraphVertex.SetVertexWeight(currentVertex.GetVertexWeight()+1);
                }

                vertexQueue.add((KSGraphVertexWeighted) edge.GetTargetVertex());
            }
        }

        //
        // CodeGuru should have ensured that we are not de-referencing a null pointer
        //
        return queuedVertexes.get(targetVertex.GetVertexName()).GetBackTrackList();
        /*ArrayList<KSGraphVertex> path = null;
        KSGraphVertexBackTrack queryTarget = queuedVertexes.get(targetVertex.GetVertexName());
        if(queryTarget!=null)
        {
            path = queryTarget.GetBackTrackList();
        }
        return path;*/
    }

    public  HashMap<String, KSGraphVertex> CloneGraph()
    {
        HashMap<String, KSGraphVertex> clonedGraph = new HashMap<String, KSGraphVertex>();
        Queue<ArrayList<KSGraphVertex>> graphQueue = new LinkedList<ArrayList<KSGraphVertex>>();
        ArrayList<KSGraphVertex> pairGraphVertex = new ArrayList<KSGraphVertex>();
        KSGraphVertex newGraphVertex = new KSGraphVertex(this.GetVertexName());

        clonedGraph.put(newGraphVertex.GetVertexName(), newGraphVertex);
        pairGraphVertex.add(this);
        pairGraphVertex.add(newGraphVertex);
        graphQueue.add(pairGraphVertex);

        while(!graphQueue.isEmpty())
        {
            pairGraphVertex = graphQueue.remove();
            KSGraphVertex originalVertex = pairGraphVertex.get(0);
            KSGraphVertex clonedVertex = pairGraphVertex.get(1);
            for(KSGraphEdge edge : originalVertex.GetOutgoingEdges())
            {
                if(clonedGraph.containsKey(edge.GetTargetVertex().GetVertexName()))
                {
                    newGraphVertex = clonedGraph.get(edge.GetTargetVertex().GetVertexName());
                }
                else
                {
                    newGraphVertex = new KSGraphVertex(edge.GetTargetVertex().GetVertexName());
                    pairGraphVertex = new ArrayList<KSGraphVertex>();
                    pairGraphVertex.add(edge.GetTargetVertex());
                    pairGraphVertex.add(newGraphVertex);
                    graphQueue.add(pairGraphVertex);
                    clonedGraph.put(newGraphVertex.GetVertexName(), newGraphVertex);
                }

                clonedVertex.ConnectToTarget(newGraphVertex);
            }
        }

        return clonedGraph;
    }



    public static HashMap<String, KSGraphVertex> CloneGraph(KSGraphVertex graphRoot)
    {
        HashMap<String, KSGraphVertex> clonedGraph = new HashMap<String, KSGraphVertex>();
        Queue<ArrayList<KSGraphVertex>> graphQueue = new LinkedList<ArrayList<KSGraphVertex>>();
        ArrayList<KSGraphVertex> pairGraphVertex = new ArrayList<KSGraphVertex>();
        KSGraphVertex newGraphVertex = new KSGraphVertex(graphRoot.GetVertexName());

        clonedGraph.put(newGraphVertex.GetVertexName(), newGraphVertex);
        pairGraphVertex.add(graphRoot);
        pairGraphVertex.add(newGraphVertex);
        graphQueue.add(pairGraphVertex);

        while(!graphQueue.isEmpty())
        {
            pairGraphVertex = graphQueue.remove();
            KSGraphVertex originalVertex = pairGraphVertex.get(0);
            KSGraphVertex clonedVertex = pairGraphVertex.get(1);
            for(KSGraphEdge edge : originalVertex.GetOutgoingEdges())
            {
                if(clonedGraph.containsKey(edge.GetTargetVertex().GetVertexName()))
                {
                    newGraphVertex = clonedGraph.get(edge.GetTargetVertex().GetVertexName());
                }
                else
                {
                    newGraphVertex = new KSGraphVertex(edge.GetTargetVertex().GetVertexName());
                    pairGraphVertex = new ArrayList<KSGraphVertex>();
                    pairGraphVertex.add(edge.GetTargetVertex());
                    pairGraphVertex.add(newGraphVertex);
                    graphQueue.add(pairGraphVertex);
                    clonedGraph.put(newGraphVertex.GetVertexName(), newGraphVertex);
                }

                clonedVertex.ConnectToTarget(newGraphVertex);
            }
        }

        return clonedGraph;
    }

    public static ArrayList<KSGraphVertex> TopologicalSort(HashMap<String, KSGraphVertex> clonedGraph, KSGraphVertex graphRoot)
    {
        Queue<KSGraphVertex> vertexQueue = new LinkedList<KSGraphVertex>();
        HashMap<String, KSGraphVertex> queuedVertexes = new HashMap<String, KSGraphVertex>();
        ArrayList<KSGraphVertex> topologicalOrderList = new ArrayList<KSGraphVertex>();

        vertexQueue.add(graphRoot);
        queuedVertexes.put(graphRoot.GetVertexName(), graphRoot);
        KSGraphVertex currentVertex = null;

        while(!vertexQueue.isEmpty())
        {
            currentVertex = vertexQueue.remove();

            if (currentVertex.GetIncomingEdges().isEmpty())
            {
                for (KSGraphEdge edge : currentVertex.GetOutgoingEdges())
                {
                    if (!queuedVertexes.containsKey(edge.GetTargetVertex().GetVertexName()))
                    {
                        vertexQueue.add(edge.GetTargetVertex());
                        queuedVertexes.put(edge.GetTargetVertex().GetVertexName(), edge.GetTargetVertex());
                    }
                }
                currentVertex.Isolate();
                topologicalOrderList.add(currentVertex);
            }
            else
            {
                vertexQueue.add(currentVertex);
            }
        }
        return topologicalOrderList;
    }
}
