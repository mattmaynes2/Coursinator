import java.util.*;

public class  GridOrganizer<T>
{
	private HashMap<Integer, HashMap<Integer, T>> grid;
	
	public GridOrganizer()
	{
		grid = new HashMap<Integer, HashMap<Integer, T>>();
	}
	
	public void addToColumn(int col, int colspan, T item)
	{
		int nextFreeRow;
		
		for (int i=0; i<colspan; i++)
		{
			if (!grid.containsKey(col+i))
			{
				grid.put(col+i, new HashMap<Integer, T>());
			}
		}

		nextFreeRow = Collections.max(grid.get(col).keySet()) + 1;

		for (int i=0; i<colspan; i++)
		{
			while (getGridItem(col+i, nextFreeRow) != null)
			{
				nextFreeRow++;
			}
		}
		
		for (int i=0; i<colspan; i++)
		{
			grid.get(col+i).put(nextFreeRow, item);
		}
	}
	
	public T getGridItem(int col, int row)
	{
		T item;

		try
		{
			item = grid.get(col).get(row);
		}
		catch(Exception ex)
		{
			return null;
		}
		
		return item;
	}
	
	public void printRows()
	{
		for (Map.Entry col : grid.entrySet())
		{
			System.out.print("Column" + col.getKey() + ": ");
			System.out.print(col.getValue());
			System.out.println("");
		}
	}
}
