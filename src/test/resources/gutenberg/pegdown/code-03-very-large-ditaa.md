
```ditaa
+---------------+        +---------------+        +-----------------+
| So you+re not |        | Do you set a  |        | On SUB sockets  |
| getting every |    +-->| subscription  +------->| you have to     |
| message? cRED |    |   | for messages? | No     | subscribe to    |
+--------+------+    |   +-------+-------+        | get messages {c}|
         | Yes       |           | Yes            +-----------------+
         v           |           v
+----------------+   |   +----------------+        +---------------+
| Are you losing |   |   | Do you start   |        | Start all SUB |
| messages in a  +---+   | the SUB socket +------->| sockets first |
| SUB socket? {d}| Yes   | after the PUB? | Yes    | then the PUB  |
+--------+-------+       +--------+-------+        +---------------+
         | No                     | No
         v                        v             +------------------+
/---------------+       +-----------------+     | Send and recv in |
| Are you using | Yes   | See explanation |     | a loop and check |
| REQ and REP?  +---+   | of slow joiners |  +->| return codes+    |
\--------+------+   |   | in the text {s} |  :  | With REP, recv   |
         | No       |   +-----------------+  |  | and send    cGRE |
         v          +------------------------+  +------------------+
+-----------------\      /----------------\                          
|  Are you using  +--\   | First PULL can |       +---------------+
|  PUSH sockets?  |  \-->| grab many msgs |       | Use the load  |
+--------+--------/ Yes  | while others   +--+    | balancing     |
         | No            | are still busy |  +--->| pattern and   |
         v               | connecting cBLK|       | ROUTER/DEALER |
+-----------------\      \----------------/       | sockets  {io} |
|  Do you check   | No                            +---------------+
| return codes on +---*   +----------------+
|  all methods?   |   |   | Check each 0MQ |   *--------*---------*
\--------+--------+   *-->| method call {o}|   | Use sockets only |
         | Yes            +----------------+   | in their owning  |
         v                                     | threads unless   |
/-----------------\      +---+         +------>* you know about   *
| Are you using a |      |   |      /--+       | memory barriers  |
| socket in more  +------+   |      |          *--------*---------*
| than 1 thread?  | Yes      +------/         
\--------+--------/                               +---------------+
         | No             /---=------------\      | To use inproc |
         v                | Do you call    |      | your sockets  |
+---------------+   +---->| zmq_ctx_new    +--=-->| must be in    |
| Are you using |   |     | twice or more? |Yes   | the same  {mo}|
| the inproc:// +---+     +--------+-------+      | 0MQ context   |
| transport?    | Yes              | No           +---------------+
+--------+------+                  v                              
         | No             +-----------------+      +------------+
         v                | Check that you  |      |            |
/-----------------\       | bind before you |--=---|   c1AB     |
|  Are you using  | Yes   | connect    cBLU |      | {tr}       |
| ROUTER sockets? +---+   +-----------------+      +------------+
\--------+-----=--/   |                      
         | No         |   *--------*---------*     +------------+
         v            :   | Check that the   |     | If you use |
+----------------+    |   | reply address is |     | identities |
| Make a minimal |    +-->* valid+ 0MQ drops *---->| set them   |
| test case, ask |        | messages it can't|     | before you |
| on IRC channel |        | route {mo}       |     | connect {d}|
+----------------+        *--------*---------*     +------------+
```