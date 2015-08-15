// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g 2011-01-27 16:54:33
package au.edu.sydney.Curracurrong.queryLanguage;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TLexer extends Lexer {
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

    public TLexer() {;} 
    public TLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g"; }

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:3:6: ( ';' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:3:8: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:4:7: ( 'exec' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:4:9: 'exec'
            {
            match("exec"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:5:7: ( 'kill' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:5:9: 'kill'
            {
            match("kill"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:6:7: ( 'status' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:6:9: 'status'
            {
            match("status"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:7:7: ( 'print' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:7:9: 'print'
            {
            match("print"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:8:7: ( 'load' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:8:9: 'load'
            {
            match("load"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:9:7: ( 'visualize' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:9:9: 'visualize'
            {
            match("visualize"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:10:7: ( 'query' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:10:9: 'query'
            {
            match("query"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:11:7: ( '=' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:11:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:12:7: ( 'sensorstreaming' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:12:9: 'sensorstreaming'
            {
            match("sensorstreaming"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:13:7: ( '|' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:13:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:7: ( '->' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:14:9: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:15:7: ( '(' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:15:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:16:7: ( ')' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:16:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:17:7: ( '[' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:17:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:18:7: ( ',' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:18:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:19:7: ( ']' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:19:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:153:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:153:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:153:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:155:7: ( '\"' ( . )* '\"' )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:155:9: '\"' ( . )* '\"'
            {
            match('\"'); 
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:155:12: ( . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\"') ) {
                    alt2=2;
                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:155:12: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:157:5: ( ( '0' .. '9' )+ )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:157:9: ( '0' .. '9' )+
            {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:157:9: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:157:9: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:8: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ ( ( 'E' | 'e' ) ( '+' | '-' ) ( '0' .. '9' )+ )? )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:12: ( '0' .. '9' )* '.' ( '0' .. '9' )+ ( ( 'E' | 'e' ) ( '+' | '-' ) ( '0' .. '9' )+ )?
            {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:12: ( '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('.'); 
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:29: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:30: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:41: ( ( 'E' | 'e' ) ( '+' | '-' ) ( '0' .. '9' )+ )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='E'||LA7_0=='e') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:42: ( 'E' | 'e' ) ( '+' | '-' ) ( '0' .. '9' )+
                    {
                    if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:63: ( '0' .. '9' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:159:64: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:161:5: ( ( ' ' | '\\t' | '\\n' )+ )
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:161:9: ( ' ' | '\\t' | '\\n' )+
            {
            // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:161:9: ( ' ' | '\\t' | '\\n' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\t' && LA8_0<='\n')||LA8_0==' ') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:8: ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | ID | STRING | INT | DOUBLE | WS )
        int alt9=22;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:15: T__10
                {
                mT__10(); 

                }
                break;
            case 3 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:21: T__11
                {
                mT__11(); 

                }
                break;
            case 4 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:27: T__12
                {
                mT__12(); 

                }
                break;
            case 5 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:33: T__13
                {
                mT__13(); 

                }
                break;
            case 6 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:39: T__14
                {
                mT__14(); 

                }
                break;
            case 7 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:45: T__15
                {
                mT__15(); 

                }
                break;
            case 8 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:51: T__16
                {
                mT__16(); 

                }
                break;
            case 9 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:57: T__17
                {
                mT__17(); 

                }
                break;
            case 10 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:63: T__18
                {
                mT__18(); 

                }
                break;
            case 11 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:69: T__19
                {
                mT__19(); 

                }
                break;
            case 12 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:75: T__20
                {
                mT__20(); 

                }
                break;
            case 13 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:81: T__21
                {
                mT__21(); 

                }
                break;
            case 14 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:87: T__22
                {
                mT__22(); 

                }
                break;
            case 15 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:93: T__23
                {
                mT__23(); 

                }
                break;
            case 16 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:99: T__24
                {
                mT__24(); 

                }
                break;
            case 17 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:105: T__25
                {
                mT__25(); 

                }
                break;
            case 18 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:111: ID
                {
                mID(); 

                }
                break;
            case 19 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:114: STRING
                {
                mSTRING(); 

                }
                break;
            case 20 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:121: INT
                {
                mINT(); 

                }
                break;
            case 21 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:125: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 22 :
                // C:\\Vasvi_Doc\\Paper draft\\Cascade\\src\\Cascade-Server\\src\\queryLanguage\\T.g:1:132: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\2\uffff\7\21\12\uffff\1\36\2\uffff\10\21\1\uffff\10\21\1\57\1"+
        "\60\3\21\1\64\2\21\2\uffff\2\21\1\71\1\uffff\1\21\1\73\1\74\1\21"+
        "\1\uffff\1\21\2\uffff\5\21\1\104\1\21\1\uffff\4\21\1\112\1\uffff";
    static final String DFA9_eofS =
        "\113\uffff";
    static final String DFA9_minS =
        "\1\11\1\uffff\1\170\1\151\1\145\1\162\1\157\1\151\1\165\12\uffff"+
        "\1\56\2\uffff\1\145\1\154\1\141\1\156\1\151\1\141\1\163\1\145\1"+
        "\uffff\1\143\1\154\1\164\1\163\1\156\1\144\1\165\1\162\2\60\1\165"+
        "\1\157\1\164\1\60\1\141\1\171\2\uffff\1\163\1\162\1\60\1\uffff\1"+
        "\154\2\60\1\163\1\uffff\1\151\2\uffff\1\164\1\172\1\162\2\145\1"+
        "\60\1\141\1\uffff\1\155\1\151\1\156\1\147\1\60\1\uffff";
    static final String DFA9_maxS =
        "\1\174\1\uffff\1\170\1\151\1\164\1\162\1\157\1\151\1\165\12\uffff"+
        "\1\71\2\uffff\1\145\1\154\1\141\1\156\1\151\1\141\1\163\1\145\1"+
        "\uffff\1\143\1\154\1\164\1\163\1\156\1\144\1\165\1\162\2\172\1\165"+
        "\1\157\1\164\1\172\1\141\1\171\2\uffff\1\163\1\162\1\172\1\uffff"+
        "\1\154\2\172\1\163\1\uffff\1\151\2\uffff\1\164\1\172\1\162\2\145"+
        "\1\172\1\141\1\uffff\1\155\1\151\1\156\1\147\1\172\1\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\7\uffff\1\11\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1"+
        "\22\1\23\1\uffff\1\25\1\26\10\uffff\1\24\20\uffff\1\2\1\3\3\uffff"+
        "\1\6\4\uffff\1\5\1\uffff\1\10\1\4\7\uffff\1\7\5\uffff\1\12";
    static final String DFA9_specialS =
        "\113\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\25\25\uffff\1\25\1\uffff\1\22\5\uffff\1\14\1\15\2\uffff"+
            "\1\17\1\13\1\24\1\uffff\12\23\1\uffff\1\1\1\uffff\1\11\3\uffff"+
            "\32\21\1\16\1\uffff\1\20\1\uffff\1\21\1\uffff\4\21\1\2\5\21"+
            "\1\3\1\6\3\21\1\5\1\10\1\21\1\4\2\21\1\7\4\21\1\uffff\1\12",
            "",
            "\1\26",
            "\1\27",
            "\1\31\16\uffff\1\30",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\24\1\uffff\12\23",
            "",
            "",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\61",
            "\1\62",
            "\1\63",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\65",
            "\1\66",
            "",
            "",
            "\1\67",
            "\1\70",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            "\1\72",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\75",
            "",
            "\1\76",
            "",
            "",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\105",
            "",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | ID | STRING | INT | DOUBLE | WS );";
        }
    }
 

}