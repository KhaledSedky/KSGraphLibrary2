import java.lang.*;
import java.util.*;

public class KSGraphVertexBackTrack
{
    private KSGraphVertexWeighted _targetGraph;
    private ArrayList<KSGraphVertex> _backTrackList;

    public  KSGraphVertexBackTrack(KSGraphVertexWeighted targetGraph)
    {
        this._targetGraph = targetGraph;
        this._backTrackList = new ArrayList<KSGraphVertex>();
    }

    public KSGraphVertexWeighted GetTargetVertex()
    {
        return this._targetGraph;
    }

    public ArrayList<KSGraphVertex> GetBackTrackList()
    {
        return this._backTrackList;
    }
}
