// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g 2011-01-27 16:54:33
package au.edu.sydney.Curracurrong.queryLanguage;
  import java.util.HashMap;
  import java.util.Map;
  import java.io.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "STRING", "INT", "DOUBLE", "WS", "';'", "'exec'", "'kill'", "'status'", "'print'", "'load'", "'visualize'", "'query'", "'='", "'sensorstreaming'", "'|'", "'->'", "'('", "')'", "'['", "','", "']'"
    };
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int INT=6;
    public static final int ID=4;
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int T__19=19;
    public static final int WS=8;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int DOUBLE=7;
    public static final int STRING=5;

    // delegates
    // delegators


        public TParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public TParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return TParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g"; }


       ControlCentre control = ControlCentre.getInstance();



    // $ANTLR start "program"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:13:1: program : ( commands ';' )* ;
    public final void program() throws RecognitionException {
        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:1: ( ( commands ';' )* )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:3: ( commands ';' )*
            {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:3: ( commands ';' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=10 && LA1_0<=16)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:5: commands ';'
            	    {
            	    System.out.println("CASCADE>");
            	    pushFollow(FOLLOW_commands_in_program25);
            	    commands();

            	    state._fsp--;

            	    match(input,9,FOLLOW_9_in_program27); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "program"


    // $ANTLR start "commands"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:17:1: commands : ( 'exec' ID STRING | 'kill' ID | 'status' ID | 'print' ID | 'load' str | 'visualize' ID | query_command );
    public final void commands() throws RecognitionException {
        Token ID1=null;
        Token STRING2=null;
        Token ID3=null;
        Token ID4=null;
        Token ID5=null;
        Token ID7=null;
        String str6 = null;


        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:18:1: ( 'exec' ID STRING | 'kill' ID | 'status' ID | 'print' ID | 'load' str | 'visualize' ID | query_command )
            int alt2=7;
            switch ( input.LA(1) ) {
            case 10:
                {
                alt2=1;
                }
                break;
            case 11:
                {
                alt2=2;
                }
                break;
            case 12:
                {
                alt2=3;
                }
                break;
            case 13:
                {
                alt2=4;
                }
                break;
            case 14:
                {
                alt2=5;
                }
                break;
            case 15:
                {
                alt2=6;
                }
                break;
            case 16:
                {
                alt2=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:18:3: 'exec' ID STRING
                    {
                    match(input,10,FOLLOW_10_in_commands39); 
                    ID1=(Token)match(input,ID,FOLLOW_ID_in_commands41); 
                    STRING2=(Token)match(input,STRING,FOLLOW_STRING_in_commands43); 
                     control.execute((ID1!=null?ID1.getText():null),(STRING2!=null?STRING2.getText():null)); 

                    }
                    break;
                case 2 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:20:3: 'kill' ID
                    {
                    match(input,11,FOLLOW_11_in_commands53); 
                    ID3=(Token)match(input,ID,FOLLOW_ID_in_commands55); 
                     control.kill((ID3!=null?ID3.getText():null)); 

                    }
                    break;
                case 3 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:22:3: 'status' ID
                    {
                    match(input,12,FOLLOW_12_in_commands64); 
                    ID4=(Token)match(input,ID,FOLLOW_ID_in_commands66); 
                     control.getStatus((ID4!=null?ID4.getText():null)); 

                    }
                    break;
                case 4 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:24:3: 'print' ID
                    {
                    match(input,13,FOLLOW_13_in_commands75); 
                    ID5=(Token)match(input,ID,FOLLOW_ID_in_commands77); 
                     control.print((ID5!=null?ID5.getText():null)); 

                    }
                    break;
                case 5 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:26:3: 'load' str
                    {
                    match(input,14,FOLLOW_14_in_commands86); 
                    pushFollow(FOLLOW_str_in_commands88);
                    str6=str();

                    state._fsp--;

                     control.load(str6); 

                    }
                    break;
                case 6 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:28:3: 'visualize' ID
                    {
                    match(input,15,FOLLOW_15_in_commands97); 
                    ID7=(Token)match(input,ID,FOLLOW_ID_in_commands99); 
                     control.visualize((ID7!=null?ID7.getText():null)); 

                    }
                    break;
                case 7 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:30:3: query_command
                    {
                    pushFollow(FOLLOW_query_command_in_commands110);
                    query_command();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "commands"

    protected static class query_command_scope {
        StreamGraph graph;
    }
    protected Stack query_command_stack = new Stack();


    // $ANTLR start "query_command"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:34:1: query_command : 'query' ID '=' expression[true] ( 'sensorstreaming' )? ;
    public final void query_command() throws RecognitionException {
        query_command_stack.push(new query_command_scope());
        Token ID9=null;
        TParser.expression_return expression8 = null;



           ((query_command_scope)query_command_stack.peek()).graph = new StreamGraph();
           boolean isSensorStreaming = true;

        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:42:1: ( 'query' ID '=' expression[true] ( 'sensorstreaming' )? )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:42:3: 'query' ID '=' expression[true] ( 'sensorstreaming' )?
            {
            match(input,16,FOLLOW_16_in_query_command132); 
            ID9=(Token)match(input,ID,FOLLOW_ID_in_query_command134); 
            match(input,17,FOLLOW_17_in_query_command136); 
            pushFollow(FOLLOW_expression_in_query_command138);
            expression8=expression(true);

            state._fsp--;

            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:42:36: ( 'sensorstreaming' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==18) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:42:37: 'sensorstreaming'
                    {
                    match(input,18,FOLLOW_18_in_query_command143); 
                     isSensorStreaming=false;

                    }
                    break;

            }


                 StreamNodeSink sink = new StreamNodeSink();
                 ((query_command_scope)query_command_stack.peek()).graph.addNode(sink); 
                 sink.setSensorNode("0.0.0.0");
                 ((query_command_scope)query_command_stack.peek()).graph.addEdge((expression8!=null?expression8.exit:null),sink); 
                 ((query_command_scope)query_command_stack.peek()).graph.setSink(sink);
                 control.defineQuery((ID9!=null?ID9.getText():null), ((query_command_scope)query_command_stack.peek()).graph,isSensorStreaming);               

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            query_command_stack.pop();
        }
        return ;
    }
    // $ANTLR end "query_command"

    public static class expression_return extends ParserRuleReturnScope {
        public StreamNode entry;
        public StreamNode exit;
    };

    // $ANTLR start "expression"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:53:1: expression[boolean first] returns [StreamNode entry, StreamNode exit] : property_list t1= term[$first] ( '|' t2= term[$first] ( '|' t3= term[$first] )* )? ;
    public final TParser.expression_return expression(boolean first) throws RecognitionException {
        TParser.expression_return retval = new TParser.expression_return();
        retval.start = input.LT(1);

        TParser.term_return t1 = null;

        TParser.term_return t2 = null;

        TParser.term_return t3 = null;

        Map<String,Object> property_list10 = null;


        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:55:1: ( property_list t1= term[$first] ( '|' t2= term[$first] ( '|' t3= term[$first] )* )? )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:55:3: property_list t1= term[$first] ( '|' t2= term[$first] ( '|' t3= term[$first] )* )?
            {
            pushFollow(FOLLOW_property_list_in_expression167);
            property_list10=property_list();

            state._fsp--;

            pushFollow(FOLLOW_term_in_expression171);
            t1=term(first);

            state._fsp--;

            retval.entry = (t1!=null?t1.entry:null); 
                  retval.exit =(t1!=null?t1.exit:null);
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:58:4: ( '|' t2= term[$first] ( '|' t3= term[$first] )* )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==19) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:58:5: '|' t2= term[$first] ( '|' t3= term[$first] )*
                    {
                    match(input,19,FOLLOW_19_in_expression186); 
                    pushFollow(FOLLOW_term_in_expression190);
                    t2=term(first);

                    state._fsp--;

                    StreamNode splitNode = null;

                             // create a split node only for operations that are 
                             // not sense operations. First operations have to be 
                             // sense operations.
                             if (! first) {
                                 splitNode = new StreamNodeSplit();
                                 ((query_command_scope)query_command_stack.peek()).graph.addNode(splitNode);
                                 splitNode.setPropertyList(property_list10);
                                 ((query_command_scope)query_command_stack.peek()).graph.addEdge(splitNode,(t1!=null?t1.entry:null));
                                 ((query_command_scope)query_command_stack.peek()).graph.addEdge(splitNode,(t2!=null?t2.entry:null));
                             }

                             // create join node
                             StreamNode joinNode = new StreamNodeJoin();
                             ((query_command_scope)query_command_stack.peek()).graph.addNode(joinNode);
                             joinNode.setPropertyList(property_list10);
                             ((query_command_scope)query_command_stack.peek()).graph.addEdge((t1!=null?t1.exit:null),joinNode);
                             ((query_command_scope)query_command_stack.peek()).graph.addEdge((t2!=null?t2.exit:null),joinNode);

                             retval.entry = splitNode;
                             retval.exit = joinNode;
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:81:7: ( '|' t3= term[$first] )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==19) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:81:8: '|' t3= term[$first]
                    	    {
                    	    match(input,19,FOLLOW_19_in_expression211); 
                    	    pushFollow(FOLLOW_term_in_expression215);
                    	    t3=term(first);

                    	    state._fsp--;

                    	    if (! first) {
                    	                  ((query_command_scope)query_command_stack.peek()).graph.addEdge(retval.entry,(t3!=null?t3.entry:null));
                    	               }
                    	               ((query_command_scope)query_command_stack.peek()).graph.addEdge((t3!=null?t3.exit:null),retval.exit);

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class term_return extends ParserRuleReturnScope {
        public StreamNode entry;
        public StreamNode exit;
    };

    // $ANTLR start "term"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:90:1: term[boolean first] returns [StreamNode entry, StreamNode exit] : f1= factor[$first] ( '->' f2= factor[false] )* ;
    public final TParser.term_return term(boolean first) throws RecognitionException {
        TParser.term_return retval = new TParser.term_return();
        retval.start = input.LT(1);

        TParser.factor_return f1 = null;

        TParser.factor_return f2 = null;


        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:92:1: (f1= factor[$first] ( '->' f2= factor[false] )* )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:92:3: f1= factor[$first] ( '->' f2= factor[false] )*
            {
            pushFollow(FOLLOW_factor_in_term263);
            f1=factor(first);

            state._fsp--;

            retval.entry = (f1!=null?f1.entry:null); 
                  retval.exit = (f1!=null?f1.exit:null);
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:95:5: ( '->' f2= factor[false] )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==20) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:95:6: '->' f2= factor[false]
            	    {
            	    match(input,20,FOLLOW_20_in_term281); 
            	    pushFollow(FOLLOW_factor_in_term285);
            	    f2=factor(false);

            	    state._fsp--;

            	    ((query_command_scope)query_command_stack.peek()).graph.addEdge(retval.exit,(f2!=null?f2.entry:null)); 
            	           retval.exit = (f2!=null?f2.exit:null);

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class factor_return extends ParserRuleReturnScope {
        public StreamNode entry;
        public StreamNode exit;
    };

    // $ANTLR start "factor"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:101:1: factor[boolean first] returns [StreamNode entry, StreamNode exit] : ( '(' expression[$first] ')' | ID property_list );
    public final TParser.factor_return factor(boolean first) throws RecognitionException {
        TParser.factor_return retval = new TParser.factor_return();
        retval.start = input.LT(1);

        Token ID12=null;
        TParser.expression_return expression11 = null;

        Map<String,Object> property_list13 = null;


        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:103:1: ( '(' expression[$first] ')' | ID property_list )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==21) ) {
                alt7=1;
            }
            else if ( (LA7_0==ID) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:103:3: '(' expression[$first] ')'
                    {
                    match(input,21,FOLLOW_21_in_factor316); 
                    pushFollow(FOLLOW_expression_in_factor318);
                    expression11=expression(first);

                    state._fsp--;

                    match(input,22,FOLLOW_22_in_factor322); 
                    retval.entry = (expression11!=null?expression11.entry:null); 
                         retval.exit = (expression11!=null?expression11.exit:null);

                    }
                    break;
                case 2 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:106:3: ID property_list
                    {
                    ID12=(Token)match(input,ID,FOLLOW_ID_in_factor334); 
                    pushFollow(FOLLOW_property_list_in_factor336);
                    property_list13=property_list();

                    state._fsp--;

                    StreamNode node=null;
                         try {
                                String className = "au.edu.sydney.Curracurrong.queryLanguage.StreamNode" + (ID12!=null?ID12.getText():null);
                                Class nodeClass = Class.forName(className);
                                node = (StreamNode) nodeClass.newInstance();
                                retval.entry = retval.exit = node;
                                ((query_command_scope)query_command_stack.peek()).graph.addNode(node);
                                node.setPropertyList(property_list13);
                                if (first) {
                                    ((query_command_scope)query_command_stack.peek()).graph.addSource(node);
                                    if(!node.isSenseOp()) {
                                        System.out.println("CASCADE: "+(ID12!=null?ID12.getText():null)+" is not a sense operation.");
                                    }
                                }
                         }                         
                         catch (Exception e) {
                                System.out.println("CASCADE: no actor class for "+(ID12!=null?ID12.getText():null)+" found.."+e);
                                retval.entry = retval.exit = null;
                                node = new StreamNodeFilter();
                                ((StreamNodeFilter)node).setFilterName(ID12!=null?ID12.getText():null);
                                retval.entry = retval.exit = node;
                                ((query_command_scope)query_command_stack.peek()).graph.addNode(node);
                                node.setPropertyList(property_list13);
                         }            

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "factor"


    // $ANTLR start "property_list"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:128:1: property_list returns [Map<String,Object> plist] : ( '[' (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )? ']' )? ;
    public final Map<String,Object> property_list() throws RecognitionException {
        Map<String,Object> plist = null;

        Token id1=null;
        Token id2=null;
        Object v1 = null;

        Object v2 = null;



           plist = new HashMap<String,Object>();

        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:1: ( ( '[' (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )? ']' )? )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:3: ( '[' (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )? ']' )?
            {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:3: ( '[' (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )? ']' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==23) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:5: '[' (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )? ']'
                    {
                    match(input,23,FOLLOW_23_in_property_list363); 
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:9: (id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )* )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==ID) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:133:11: id1= ID '=' v1= value ( ',' id2= ID '=' v2= value )*
                            {
                            id1=(Token)match(input,ID,FOLLOW_ID_in_property_list369); 
                            match(input,17,FOLLOW_17_in_property_list371); 
                            pushFollow(FOLLOW_value_in_property_list375);
                            v1=value();

                            state._fsp--;

                            plist.put((id1!=null?id1.getText():null),v1);
                            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:134:11: ( ',' id2= ID '=' v2= value )*
                            loop8:
                            do {
                                int alt8=2;
                                int LA8_0 = input.LA(1);

                                if ( (LA8_0==24) ) {
                                    alt8=1;
                                }


                                switch (alt8) {
                            	case 1 :
                            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:134:13: ',' id2= ID '=' v2= value
                            	    {
                            	    match(input,24,FOLLOW_24_in_property_list391); 
                            	    id2=(Token)match(input,ID,FOLLOW_ID_in_property_list395); 
                            	    match(input,17,FOLLOW_17_in_property_list397); 
                            	    pushFollow(FOLLOW_value_in_property_list401);
                            	    v2=value();

                            	    state._fsp--;

                            	    plist.put((id2!=null?id2.getText():null),v2);

                            	    }
                            	    break;

                            	default :
                            	    break loop8;
                                }
                            } while (true);


                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_property_list415); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return plist;
    }
    // $ANTLR end "property_list"


    // $ANTLR start "value"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:138:1: value returns [Object val] : ( ID | str | INT | DOUBLE );
    public final Object value() throws RecognitionException {
        Object val = null;

        Token ID14=null;
        Token INT16=null;
        Token DOUBLE17=null;
        String str15 = null;


        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:140:1: ( ID | str | INT | DOUBLE )
            int alt11=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt11=1;
                }
                break;
            case STRING:
                {
                alt11=2;
                }
                break;
            case INT:
                {
                alt11=3;
                }
                break;
            case DOUBLE:
                {
                alt11=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:140:3: ID
                    {
                    ID14=(Token)match(input,ID,FOLLOW_ID_in_value430); 
                    val = new String((ID14!=null?ID14.getText():null)); 

                    }
                    break;
                case 2 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:141:3: str
                    {
                    pushFollow(FOLLOW_str_in_value440);
                    str15=str();

                    state._fsp--;

                    val = str15;

                    }
                    break;
                case 3 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:142:3: INT
                    {
                    INT16=(Token)match(input,INT,FOLLOW_INT_in_value449); 
                    val = new Integer((INT16!=null?INT16.getText():null)); 

                    }
                    break;
                case 4 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:143:3: DOUBLE
                    {
                    DOUBLE17=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_value458); 
                    val = new Double((DOUBLE17!=null?DOUBLE17.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "value"


    // $ANTLR start "str"
    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:146:1: str returns [String val] : STRING ;
    public final String str() throws RecognitionException {
        String val = null;

        Token STRING18=null;

        try {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:148:1: ( STRING )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:149:1: STRING
            {
            STRING18=(Token)match(input,STRING,FOLLOW_STRING_in_str475); 
             String str = new String((STRING18!=null?STRING18.getText():null));
                      val = str.substring(1,str.length()-1); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "str"

    // Delegated rules


 

    public static final BitSet FOLLOW_commands_in_program25 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_9_in_program27 = new BitSet(new long[]{0x000000000001FC02L});
    public static final BitSet FOLLOW_10_in_commands39 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_commands41 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_STRING_in_commands43 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_commands53 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_commands55 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_commands64 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_commands66 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_commands75 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_commands77 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_commands86 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_str_in_commands88 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_commands97 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_commands99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_command_in_commands110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_query_command132 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_query_command134 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_query_command136 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_expression_in_query_command138 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_query_command143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_property_list_in_expression167 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_term_in_expression171 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_expression186 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_term_in_expression190 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_expression211 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_term_in_expression215 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_factor_in_term263 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_20_in_term281 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_factor_in_term285 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_21_in_factor316 = new BitSet(new long[]{0x0000000000A00010L});
    public static final BitSet FOLLOW_expression_in_factor318 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_factor322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_factor334 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_property_list_in_factor336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_property_list363 = new BitSet(new long[]{0x0000000002000010L});
    public static final BitSet FOLLOW_ID_in_property_list369 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_property_list371 = new BitSet(new long[]{0x00000000000000F0L});
    public static final BitSet FOLLOW_value_in_property_list375 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24_in_property_list391 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_property_list395 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_property_list397 = new BitSet(new long[]{0x00000000000000F0L});
    public static final BitSet FOLLOW_value_in_property_list401 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_25_in_property_list415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_value430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_str_in_value440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_value449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_value458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_str475 = new BitSet(new long[]{0x0000000000000002L});

}