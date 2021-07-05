import java.lang.*;
import java.util.*;

public class KSGraphVertexWeighted extends KSGraphVertex
{
    private int __vertexWeight;

    public KSGraphVertexWeighted(String vertexName)
    {
        super(vertexName);
        this.__vertexWeight = 0;
    }

    public KSGraphVertexWeighted(KSGraphVertex copyGraphVertex)
    {
        super(copyGraphVertex.GetVertexName());
        HashMap<String, KSGraphVertexWeighted> clonedGraph = new HashMap<String, KSGraphVertexWeighted>();
        Queue<ArrayList<KSGraphVertex>> graphQueue = new LinkedList<ArrayList<KSGraphVertex>>();
        ArrayList<KSGraphVertex> pairGraphVertex = new ArrayList<KSGraphVertex>();

        clonedGraph.put(this.GetVertexName(), this);
        pairGraphVertex.add(copyGraphVertex);
        pairGraphVertex.add(this);
        graphQueue.add(pairGraphVertex);

        while(!graphQueue.isEmpty())
        {
            pairGraphVertex = graphQueue.remove();
            KSGraphVertex originalVertex = pairGraphVertex.get(0);
            KSGraphVertexWeighted clonedVertex = (KSGraphVertexWeighted)pairGraphVertex.get(1);
            KSGraphVertexWeighted newGraphVertexWeighted = null;
            for(KSGraphEdge edge : originalVertex.GetOutgoingEdges())
            {
                if(clonedGraph.containsKey(edge.GetTargetVertex().GetVertexName()))
                {
                    newGraphVertexWeighted = clonedGraph.get(edge.GetTargetVertex().GetVertexName());
                }
                else
                {
                    newGraphVertexWeighted = new KSGraphVertexWeighted(edge.GetTargetVertex().GetVertexName());
                    pairGraphVertex = new ArrayList<KSGraphVertex>();
                    pairGraphVertex.add(edge.GetTargetVertex());
                    pairGraphVertex.add(newGraphVertexWeighted);
                    graphQueue.add(pairGraphVertex);
                    clonedGraph.put(newGraphVertexWeighted.GetVertexName(), newGraphVertexWeighted);
                }
                clonedVertex.ConnectToTarget(newGraphVertexWeighted);
            }
        }
    }

    public int GetVertexWeight()
    {
        return this.__vertexWeight;
    }

    public void SetVertexWeight(int vertexWeight)
    {
        this.__vertexWeight = vertexWeight;
    }

    public int IncVertexWeight()
    {
        return ++this.__vertexWeight;
    }
}
