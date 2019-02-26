package graphs;

import java.util.Objects;

public class VertexPair
{
    private final Integer vertex1;
    private final Integer vertex2;

    public VertexPair(Integer vertex1, Integer vertex2)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public Integer getVertex1()
    {
        return vertex1;
    }

    public Integer getVertex2()
    {
        return vertex2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexPair pair = (VertexPair) o;
        return Objects.equals(vertex1, pair.vertex1) &&
                Objects.equals(vertex2, pair.vertex2);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(vertex1, vertex2);
    }
}
