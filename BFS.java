import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

class BFS<T> {

    ArrayDeque<CellWithValues>  queue = new ArrayDeque<>(100);
    List<CellWithValues>  allAccessibleCells = new ArrayList<>(900);
    Map<T, CellWithValues> mapCellCellWithValue = new HashMap<>(900);

    protected T source;

    public BFS(T source) {
        this.source = source;
        CellWithValues cellWithValue = new CellWithValues(source, true);
        mapCellCellWithValue.computeIfAbsent(cellWithValue.getCell(), c -> cellWithValue);
        mapCellCellWithValue.get(source).setVisited(true);
        queue.add(cellWithValue);
    }

    public List<CellWithValues> getAllAccessibleCells() {
        return allAccessibleCells;
    }

    public static <T> List<T> compute(T source,
                                      BiConsumer<T, List<T>> flagCell, Function<T, List<T>> neighbourgsFunction,
                                      boolean computeAcessible) {

        return new BFS(source).compute(flagCell, neighbourgsFunction, computeAcessible, Integer.MAX_VALUE, null);
    }

    public static <T> List<T> compute(T source,
                                      BiConsumer<T, List<T>> flagCell, Function<T, List<T>> neighbourgsFunction,
                                      boolean computeAcessible, int max) {

        return new BFS(source).compute(flagCell, neighbourgsFunction, computeAcessible, max, null);
    }

    public static <T> List<T> compute(T source,
                                      BiConsumer<T, List<T>> flagCell, Function<T, List<T>> neighbourgsFunction,
                                      boolean computeAcessible, int max, Function<T, Boolean> stopFunction) {

        return new BFS(source).compute(flagCell, neighbourgsFunction, computeAcessible, max, stopFunction);
    }

    public List<T> compute(BiConsumer<T, List<T>> flagCell, Function<T, List<T>> neighbourgsFunction,
                           boolean computeAcessible, int max, Function<T, Boolean> stopFunction) {
        while (!queue.isEmpty()) {
            CellWithValues firstInQueue = queue.pop();
            T cell = firstInQueue.getCell();
            List<T> neighbourgs = neighbourgsFunction.apply(cell);

            if (firstInQueue.pathToParent.size() > max || (stopFunction!=null && stopFunction.apply(firstInQueue.getCell()))) {
                neighbourgs.clear();
            }
            boolean atLeastOneNeighbourg = false;
            for (T neighbourg : neighbourgs) {
                if (mapCellCellWithValue.get(neighbourg) == null) {
                    mapCellCellWithValue.put(neighbourg, new CellWithValues(neighbourg, false));
                }
                CellWithValues cellWithValues = mapCellCellWithValue.get(neighbourg);
                if (!cellWithValues.isVisited()) {
                    cellWithValues.setParent(firstInQueue);
                    queue.addLast(cellWithValues);

                    if (computeAcessible) {
                        allAccessibleCells.add(cellWithValues);
                    }
                    if (flagCell != null) {
                        flagCell.accept(cellWithValues.getCell(),cellWithValues.getPath());
                    }
                    atLeastOneNeighbourg = true;
                    cellWithValues.setVisited(true);
                }
            }
            if (!atLeastOneNeighbourg) {
                firstInQueue.setLeaf(true);
            }
        }
        return allAccessibleCells.stream().map(CellWithValues::getCell).collect(Collectors.toList());
    }

    protected class CellWithValues {
        private T cell;
        private boolean visited = false;
        private CellWithValues parent = null;
        private List<T> pathToParent = new ArrayList<>(100);
        private boolean leaf;

        public CellWithValues(T cell, boolean visited) {
            this.cell = cell;
            this.visited = visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public T getCell() {
            return cell;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setParent(CellWithValues parent) {
            this.parent = parent;
            pathToParent = new ArrayList<>(parent.pathToParent);
            pathToParent.add(parent.getCell());
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }

        public List<T> getPath() {
            List<T> ret = new ArrayList<>();
            ret.addAll(new ArrayList<>(pathToParent));
            ret.add(getCell());
            return ret;

        }
    }

}
