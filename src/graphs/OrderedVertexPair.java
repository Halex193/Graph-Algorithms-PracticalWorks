package graphs;

public class OrderedVertexPair extends VertexPair
{
    public OrderedVertexPair(Integer vertex1, Integer vertex2)
    {
        super(vertex1, vertex2);
        if (vertex2 > vertex1)
        {
            this.vertex1 = vertex2;
            this.vertex2 = vertex1;
        }
    }
}
