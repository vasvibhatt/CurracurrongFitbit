Curracurrong
============

Curracurrong is a light-weight stream query processing engine designed for clusters
in the cloud. The system architecture and implementation is intended to provide
a good trade-off between productivity, flexibility, and energy-efficiency. 
Productivity is obtained with new query language for stream processing, where
computation is represented in the form of a stream graph. Nodes in the stream
graph represent computation elements called stream operators and edges represent
FIFO channels between operators.

The model hierarchically composes stream graphs built from individual operators
that may be filters, pipelines, and splitters-joiners, similar to other stream
programming languages such as StreamIt. The system supports data rate annotations
for stream operators that allows static computations of communication bandwidths.
An extensible stream operator library offers flexibility in designing complex
applications.

Curracurrong supports the following types of operators based on their primary
function type:
* Sense: Sense operators are inputs to a stream query and are responsible
  for collecting specific data about the environment and converting it to a
  format suitable for consumption by the downstream operators in a
  stream graph.
* Sink: Sink operators are outputs of the stream query that are
  responsible for writing out data resulting from the stream query into a format
  suitable for consumption by an external application or for human consumption
  through email or a suitable structured text record format like JSON or XML.
  These operators are usually deployed and executed on the base station by
  default unless other specified using the node annotation.
* Filter: Filter operators by default take the input record from the
  input channel and write it out to the output channel only if it satisfies the
  filtering condition specified. Transformation operators are implemented as
  filters which read the input record from the input channel and apply an empty
  filter predicate and write a transformed record to the output channel.
* Split: A split operator takes each input record from the input channel
  and write it out to one or more output channels.
* Join: A join operator performs the opposite task of a split operator by
  reading the input from one or more input channels and writing each record
  out to an output channel.

Operators are further classified based on their default scheduling modes into
time-triggered operators and event-triggered operators. Operators of the former
type are executed by the scheduling engine at regular time intervals specified
using an annotation. While operators of the latter category are executed when
there is an appropriate external trigger or there is data in its input channel.

All sense operators by default are time-triggered and execute at regular time
intervals to collect data samples from the environment, whereas, all the other
operators in the query are event-triggered, that execute when the data is
available in their input channels. To optimize energy usage, Curracurrong
facilitates in-network computation that lowers the communication overhead.
The system employs an algorithm that places stream operators optimally by
minimizing communication costs.

The following considers a stream graph for an example query that monitors a
system parameter (field number 5 in the data record) and sends an alert to
the sink if the value of the parameter reaches the threshold, that is 80. 
The query runs with three nodes and joins the incoming data to send it to
the sink at server.

The following is an example of a query defined as a stream graph represented in
the Curracurrong Query Language:

query alert = (Sense[node="1.1.1.1"]
               -> Select[field="4,5,6"]
               -> Threshold[check="$5 > 80"]
             | Sense[node="1.1.1.2"]
               -> Select[field="4,5,6"]
               -> Threshold[check="$5 > 80"]
             | Sense[node="1.1.1.3"]
               -> Select[field="4,5,6"]
               -> Threshold[check="$5 > 80"])

The structure of the Currarcurrong's query processing system consists of two 
subsystems: a server module and a runtime environment. The server module runs on
the base station. It compiles the query, maps it to a stream graph, computes
the placement of stream operators onto nodes and coordinates the deployment
of the stream graph across the network. An instance of runtime environment 
executes on each node in the network. The runtime environment has three main
components: the administrator, the communicator, and the scheduler.

The administrator receives administrative commands, such as the construction and
destruction of stream operators from the base station.

The communicator is implemented as a separate thread responsible for handling
communication between stream operators deployed across the sensor nodes via the
input and output channels. The communicator uses a routing table to route
records across the graph of stream operators deployed on different sensor nodes.

The scheduler manages and controls the execution of stream operators on the
sensor node based on the scheduling semantics, i.e., time-triggered or
event-triggered.

The Curracurrong runtime system is implemented in the Java programming language. 

