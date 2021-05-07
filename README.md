

# How to use

Example (Usage for https://www.codingame.com/contests/spring-challenge-2021 usage)

```java
gameContext.getAllTree().stream().forEach(tree ->
        {
            if (tree.getSize() > 0) {
                BFS.compute(tree.getCell(),
                        (Cell cell, List<Cell> path) -> cell.setDistToTree(path)
                        ,Cell::getNeighbourgs
                        ,true
                        ,tree.getSize() - 1);
            }
        });

```



```
class Cell {

    public void setDistToTree(List<Cell> path) {
        Cell accessibleCell = path.get(path.size() - 1);
        Tree tree = path.get(0).getTree();
        if (accessibleCell.getTree() == null && accessibleCell.getRichness() > 0 && !tree.isDormant()) {
            if (tree.isMine()) {
                accessibleCell.setCanSeed(true);
                accessibleCell.addSourceTree(tree);
            }
            else {
                accessibleCell.setCanSeedOpp(true);
                accessibleCell.addSourceTreeOpp(tree);
            }
        }
        distByTree.put(tree, path.size());
    }
}
```

