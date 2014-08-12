```ditaa

 +--------+       +---------------------+ *
 | Client |------>| Component   cEEE    |<-----------------------------+
 +--------+       +---------------------+                              |
                  | Operation()         |                              |
                  | Add(Component)      |                              |
                  | Remove(Component)   |                              |
                  | GetChild(int)       |                              |
                  +---------------------+                              |
                            #                                          |
                            |                                          |
                     +----------+--------------+                       |
                     |                         |                       |
                  +--+----------+   +----------+----------+  children  |
                  | Leaf  cEEE  |   | Composite     cEEE  |O-----------+
                  +-------------+   +---------------------+
                  | Operation() |   | Operation()         |
                  +-------------+   | Add(Component)      |
                                    | Remove(Component)   |
                                    | GetChild(int)       |
                                    +---------------------+

```
