package mcagrammar;

/**
 * If some bug was found, let me know it, comment on GitHub project.
 * https://github.com/matheusaguilar/Grammar
 * 
 * @author Matheus Catarino de Aguilar
 */

import java.util.InputMismatchException;
import java.util.LinkedList;

public class Grammar {

    private final LinkedList<String> lines;
    private final LinkedList<Production> productions;
    private final LinkedList<NonTerminal> nonterminals;
    private final LinkedList<Terminal> terminals;
    private int RECURSIVE_COUNT = 0;
    public static char CHAR_REPRESENTATION_EMPTY = '&';
    public static String CHAR_REPRESENTATION_PRODUCTION = "->"; 
    public static final String CHAR_REPRESENTATION_EOF = "#$EOF$#";
    
    public Grammar(String grammar) throws InputMismatchException{
       this.lines = new LinkedList<>();
       this.productions = new LinkedList<>();
       this.nonterminals = new LinkedList<>();
       this.terminals = new LinkedList<>();
       splitLine(this.lines, grammar, '\n');
       verifyGrammar();
       updateProductions();
       leftRecursion();
       this.lines.clear();
    }

    public LinkedList<Production> getProductions() {
        return productions;
    }

    public LinkedList<NonTerminal> getNonterminals() {
        return nonterminals;
    }

    public LinkedList<Terminal> getTerminals() {
        return terminals;
    }

    public static void setCHAR_REPRESENTATION_EMPTY(char CHAR_REPRESENTATION_EMPTY) {
        Grammar.CHAR_REPRESENTATION_EMPTY = CHAR_REPRESENTATION_EMPTY;
    }

    public static void setCHAR_REPRESENTATION_PRODUCTION(String CHAR_REPRESENTATION_PRODUCTION) {
        Grammar.CHAR_REPRESENTATION_PRODUCTION = CHAR_REPRESENTATION_PRODUCTION;
    }
    
    public NonTerminal getNonTerminalBySymbol(Symbol symb){
        NonTerminal result = null;
        for(NonTerminal nterm : nonterminals){
            if (nterm.getSymbol().equals(symb.getSymbol())){
                result = nterm;
            }
        }
        return result;
    }
    
    public static void splitLine(LinkedList<String> list, String grammar, char c){
        int last_line = 0;
        for(int i=0; i<grammar.length(); i++){
            if (grammar.charAt(i) == c){
                list.add(removeSpecialFromString(grammar.substring(last_line, i)));
                last_line = i + 1;
            }
        }
        if (last_line < grammar.length()){
           if (!(grammar.charAt(last_line) == '\r' || grammar.charAt(last_line) == '\n')){
               list.add(removeSpecialFromString(grammar.substring(last_line)));
           }
        }
    }
    
    public static String removeSpecialFromString(String text){
        for(int i=0; i<text.length(); i++){
            if (text.charAt(i) == '\r' || text.charAt(i) == '\n'){
                if (i + 1 == text.length()){
                    text = text.substring(0, i);
                } else{
                    text = text.substring(0, i) + text.substring(i + 1, text.length());
                }
            }
        }
        return text;
    }
    
    public void calculate() throws InputMismatchException{
        calculateFirst();
        calculateFollow();
    }
    
    /*  
    --- Prints, debbug  
    */
    
    public void printProductions(){
        System.out.println(" [Productions]");
        for(Production prod : productions){
            System.out.print(prod.getNonterminal().getSymbol() + " " + CHAR_REPRESENTATION_PRODUCTION);
            for(Symbol symb: prod.getSymbols()){
                System.out.print(" " + symb.getSymbol());
            }
            System.out.println("");
        }
    }
    
    public void printNonTerminals(){
        System.out.println(" [Não Terminais]");
        for(NonTerminal nterm : nonterminals){
            System.out.println("'" + nterm.getSymbol() + "'");
        }
    }
    
    public void printTerminals(){
        System.out.println(" [Terminais]");
        for(Terminal term : terminals){
            System.out.println("'" +term.getSymbol() + "'");
        }
    }
    
    public void printFirst(){
        System.out.println(" [First]");
        for(NonTerminal nterm : nonterminals){
            System.out.print(nterm.getSymbol() + " =");
                for(Terminal term: nterm.getFirst()){
                    System.out.print(" " + term.getSymbol());
                }
            System.out.println("");
        }
    }
    
    public void printFollow(){
        System.out.println(" [Follow]");
        for(NonTerminal nterm : nonterminals){
            System.out.print(nterm.getSymbol() + " =");
                for(Terminal term: nterm.getFollow()){
                    if (!term.getSymbol().equals(CHAR_REPRESENTATION_EOF)){
                        System.out.print(" " + term.getSymbol());
                    } else{
                        System.out.print(" $");
                    }
                }
            System.out.println("");
        }
    }
    
    /*  
    --- Private Functions  
    */
    
    private void verifyGrammar() throws InputMismatchException{
        if (!lines.isEmpty()){
            for(String production : lines){
                productions.add(new Production(production));
            }
            lines.clear();
        } else{
            throw new InputMismatchException("Error: Productions empty");
        }
    }
   
    private void updateProductions(){
        int divide = -1, size = 0;
        //Identificar Nao terminais:
        for(Production prod : productions){
            if (!alreadyAdded(nonterminals, prod.getNonterminal().getSymbol())){
                nonterminals.add(prod.getNonterminal());
            }
        }
        //Identificar Terminais:
        for(Production prod : productions){
            for(Symbol symb : prod.getSymbols()){
                if (!symb.getSymbol().equals("|")){
                    if (!isNonTerminal(symb.getSymbol())){
                        if (!alreadyAdded(terminals, symb.getSymbol())){
                            terminals.add(new Terminal(symb.getSymbol()));
                        }
                    }
                }
            }
        }
        //Dividir multiplas producoes:
        size = productions.size();
        for (int i=0; i<size; i++){
            divide = haveMultipleProduction(productions.get(i).getSymbols());
            if (divide != 0){
                productions.add(new Production(productions.get(i).getNonterminal(), productions.get(i).getSymbols(), divide));
                size++;
                while(productions.get(i).getSymbols().size() > divide){
                    productions.get(i).getSymbols().remove(divide);
                }
            }
        }
        //Atualizar valores de producoes
        for(Production prod : productions){
            symbolUpdate(prod.getSymbols());
        }     
    }
    
    private void leftRecursion() throws InputMismatchException{
        LinkedList<NonTerminal> recursive;
        recursive = new LinkedList<>();
        for (NonTerminal nterm : nonterminals){
            //Para todos os nao terminais, adicionar na lista as producoes que comecam com outros nao terminais
            for (Production prod : productions){
                if (prod.getSymbols().get(0) instanceof NonTerminal){
                    if (nterm.getSymbol().equals(prod.getNonterminal().getSymbol())){
                        recursive.add((NonTerminal)prod.getSymbols().get(0));
                    }
                }
            }
            //Verificar para todos os nao terminais, aqueles que estao na lista "recursive" as producoes deles possuem o nao terminal nterm
            for (NonTerminal nterm_aux : nonterminals){
                for(NonTerminal recursive1 : recursive){
                    if (recursive1.getSymbol().equals(nterm_aux.getSymbol())){
                        for (Production prod : productions){
                            if (prod.getNonterminal().getSymbol().equals(recursive1.getSymbol())){
                                if (prod.getSymbols().get(0) instanceof NonTerminal){
                                    if (nterm.getSymbol().equals(prod.getSymbols().get(0).getSymbol())){
                                        throw new InputMismatchException("Error: left recursion on grammar");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            recursive.clear();
        }
    }
    
    private int haveMultipleProduction(LinkedList<Symbol> list){
        int location = 0;
        for(int i=0; i<list.size(); i++){
            if (list.get(i).getSymbol().equals("|")){
                if (location == 0){
                    location = i;
                }
            }
        }
        return location;
    }
    
    private boolean alreadyAdded(LinkedList list, String symbol){
        boolean added = false;
        for(int i=0; i<list.size(); i++){
            if (((Symbol)list.get(i)).getSymbol().equals(symbol)){
                added = true;
            }
        }
        return added;
    }
    
    private void symbolUpdate(LinkedList<Symbol> list){
        boolean found;
        //Atualiza valores de nao terminais:
        for (Production prod: productions){
            for(NonTerminal nterm : nonterminals){
                if (prod.getNonterminal().getSymbol().equals(nterm.getSymbol())){
                    if (prod.getNonterminal().isCanbenull()){
                        nterm.setCanbenull(true);
                    }
                    prod.setNonTerminal(nterm);
                }
            }
        }
        //Atualiza valores de simbolos:
        for(int i=0;i<list.size(); i++){
           found = false;
           for(int j=0; j<nonterminals.size(); j++){
                if (!found){
                    if (list.get(i).getSymbol().equals(nonterminals.get(j).getSymbol())){
                        found = true;
                        list.remove(i);
                        list.add(i, nonterminals.get(j));
                    }
                }
            }
            if (!found){
                for(int j=0; j<terminals.size(); j++){
                    if (!found){
                        if (list.get(i).getSymbol().equals(terminals.get(j).getSymbol())){
                            found = true;
                            list.remove(i);
                            list.add(i, terminals.get(j));
                        }
                    }
                }
            }
        }
    }
    
    private boolean isNonTerminal(String symbol){
        boolean is = false;
        for(NonTerminal nterm: nonterminals){
            if (symbol.equals(nterm.getSymbol())){
                is = true;
            }
        }
        return is;
    }
    
    private void calculateFirst() throws InputMismatchException{
        LinkedList<Production> remaining;
        NonTerminal aux;
        //Calcular os primeiros first de terminais:
        for(Production prod: productions){
            if (prod.getSymbols().get(0) instanceof Terminal){
                prod.getNonterminal().addFirst((Terminal)prod.getSymbols().get(0));
            }
        }
        //Calcular first de nao terminais:
        remaining = new LinkedList<>();
        for(Production prod: productions){
            if (prod.getSymbols().get(0) instanceof NonTerminal){
                remaining.add(prod);
            }
        }
        while(!remaining.isEmpty()){
            if (RECURSIVE_COUNT < 100){
                for(int i=0; i<remaining.size(); i++){
                    aux = (NonTerminal) remaining.get(i).getSymbols().get(0);
                    if (!aux.getFirst().isEmpty()){
                        remaining.get(i).getNonterminal().addFirst(aux.getFirst());
                        remaining.remove(i);
                    }
                }
            } else{
                throw new InputMismatchException("Error: recursive count limit");
            }
            RECURSIVE_COUNT++;
        }
        RECURSIVE_COUNT = 0;
    } 
    
    private boolean isNonTerminalOnlyNull(NonTerminal nterm){
        int count = 0, aux = 0;
        for(Production prod: productions){
            if (prod.getNonterminal().getSymbol().equals(nterm.getSymbol())){
                count++;
                if (prod.getSymbols().size() == 1){
                    if (prod.getSymbols().get(0) instanceof Terminal){
                        if (prod.getSymbols().get(0).getSymbol().charAt(0) == CHAR_REPRESENTATION_EMPTY){
                            aux++;
                        }
                    }
                }
            }
        }
        return count == aux;
    }
    
    private void calculateFollow(){
        NonTerminal aux;
        int pos;
        boolean keep;
        //S -> $, EOF depois da primeira producao
        productions.get(0).getNonterminal().addFollow(new Terminal(CHAR_REPRESENTATION_EOF));
        
        //B -> aAb, Follow(A) = First(B)     
        for (Production prod: productions){
            for (int i=prod.getSymbols().size() - 1; i>=0; i--){
                if (i - 1 >= 0){
                    if (prod.getSymbols().get(i -1) instanceof NonTerminal){
                        if (prod.getSymbols().get(i) instanceof NonTerminal){
                            aux = getNonTerminalBySymbol(prod.getSymbols().get(i));
                            if (aux.isCanbenull()){
                                if (!isNonTerminalOnlyNull(aux)){
                                    getNonTerminalBySymbol(prod.getSymbols().get(i - 1)).addFollow(aux.getFirst());
                                }
                            } else{
                                getNonTerminalBySymbol(prod.getSymbols().get(i - 1)).addFollow(aux.getFirst());
                            }
                        } else{
                            getNonTerminalBySymbol(prod.getSymbols().get(i - 1)).addFollow((Terminal)prod.getSymbols().get(i));
                        }
                    }
                }
            }
        }
        
        ////B -> aAb | B -> aA, Follow(A) = Follow(B)   
        for (Production prod: productions){
            pos = prod.getSymbols().size() - 1;
            keep = true;
            while(pos >= 0 && prod.getSymbols().get(pos) instanceof NonTerminal && keep){
                aux = (NonTerminal)prod.getSymbols().get(pos);
                if (aux.isCanbenull()){
                    aux.addFollow(prod.getNonterminal().getFollow());
                } else{
                    keep = false;
                    aux.addFollow(prod.getNonterminal().getFollow());
                }
                pos--;
            }
        }
        
    }
    
}
