grammar T;

@header {
  import java.util.HashMap;
  import java.util.Map;
  import java.io.*;
}

@members {
   ControlCentre control = ControlCentre.getInstance();
}

program
: ( {System.out.println("CASCADE>");}commands ';') *
;

commands
: 'exec' ID STRING
   { control.execute($ID.text,$STRING.text); } //second argument not used in execute...
| 'kill' ID
   { control.kill($ID.text); }
| 'status' ID
   { control.getStatus($ID.text); }
| 'print' ID
   { control.print($ID.text); }
| 'load' str
   { control.load($str.val); }
| 'visualize' ID 
   { control.visualize($ID.text); } 
| query_command
	;	


query_command
scope {
   StreamGraph graph;
} 
@init {
   $query_command::graph = new StreamGraph();
   boolean isSensorStreaming = false;
}
: 'query' ID '=' expression [true] ('sensorstreaming' { isSensorStreaming=true; } )?
   {
     StreamNodeSink sink = new StreamNodeSink();
     $query_command::graph.addNode(sink); 
     sink.setSensorNode("0.0.0.0");
     $query_command::graph.addEdge($expression.exit,sink); 
     $query_command::graph.setSink(sink);
     control.defineQuery($ID.text, $query_command::graph,isSensorStreaming);
   }
;

expression[boolean first]
returns [StreamNode entry, StreamNode exit]
: property_list t1=term[$first]
     {$entry = $t1.entry; 
      $exit =$t1.exit;} 
   ('|' t2=term[$first]
        {StreamNode splitNode = null;

         // create a split node only for operations that are 
         // not sense operations. First operations have to be 
         // sense operations.
         if (! $first) {
             splitNode = new StreamNodeSplit();
             $query_command::graph.addNode(splitNode);
             splitNode.setPropertyList($property_list.plist);
             $query_command::graph.addEdge(splitNode,$t1.entry);
             $query_command::graph.addEdge(splitNode,$t2.entry);
         }

         // create join node
         StreamNode joinNode = new StreamNodeJoin();
         $query_command::graph.addNode(joinNode);
         joinNode.setPropertyList($property_list.plist);
         $query_command::graph.addEdge($t1.exit,joinNode);
         $query_command::graph.addEdge($t2.exit,joinNode);

         $entry = splitNode;
         $exit = joinNode;} 
      ('|' t3=term  [$first]
          {if (! $first) {
              $query_command::graph.addEdge($entry,$t3.entry);
           }
           $query_command::graph.addEdge($t3.exit,$exit);}
      )* 
   )?
; 

term[boolean first]
returns [StreamNode entry, StreamNode exit]
: f1=factor [$first]
     {$entry = $f1.entry; 
      $exit = $f1.exit;}  
    ('->' f2=factor [false]
      {$query_command::graph.addEdge($exit,$f2.entry); 
       $exit = $f2.exit;}
    )*
;

factor[boolean first]
returns [StreamNode entry, StreamNode exit]
: '(' expression [$first] ')'  
    {$entry = $expression.entry; 
     $exit = $expression.exit;}
| ID property_list
    {StreamNode node=null;
     try {
            String className = "queryLanguage.StreamNode" + $ID.text;
            Class nodeClass = Class.forName(className);
            node = (StreamNode) nodeClass.newInstance();
            $entry = $exit = node;
            $query_command::graph.addNode(node);
            node.setPropertyList($property_list.plist);
            if ($first) {
                $query_command::graph.addSource(node);
                if(!node.isSenseOp()) {
                    System.out.println("CASCADE: "+$ID.text+" is not a sense operation.");
                }
            }
     } catch (Exception e) {
            System.out.println("CASCADE: no actor class for "+$ID.text+" found");
            $entry = $exit = null;
            node = new StreamNodeFilter();
            ((StreamNodeFilter)node).setFilterName($ID.text);
            $entry = $exit = node;
            $query_command::graph.addNode(node);
            node.setPropertyList($property_list.plist);
     }
    }
;  

property_list
returns [Map<String,Object> plist]
@init{
   $plist = new HashMap<String,Object>();
}
: ( '[' ( id1=ID '=' v1=value {$plist.put($id1.text,$v1.val);}
          ( ',' id2=ID '=' v2=value {$plist.put($id2.text,$v2.val);} )* )?
    ']')?
;

value
returns [Object val]
: ID     {$val = new String($ID.text); }
| str    {$val = $str.val;}
| INT    {$val = new Integer($INT.text); }
| DOUBLE  {$val = new Double($DOUBLE.text); }
;

str
returns [String val]
: 
STRING  { String str = new String($STRING.text);
          $val = str.substring(1,str.length()-1); }
;

ID  :   ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

STRING: '"'.*'"';

INT :   '0'..'9'+ ;

DOUBLE :   ('0'..'9' )* '.' ('0'..'9')+ (('E'|'e')  ('+'|'-') ('0'..'9')+ )?;

WS  :   (' '|'\t'|'\n')+ {$channel=HIDDEN;}
    ;
