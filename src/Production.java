package mcagrammar;

/**
 * If some bug was found, let me know it, comment on GitHub project.
 * https://github.com/matheusaguilar/Grammar
 * 
 * @author Matheus Catarino de Aguilar
 */

import java.util.InputMismatchException;
import java.util.LinkedList;
import static mcagrammar.Grammar.CHAR_REPRESENTATION_EMPTY;
import static mcagrammar.Grammar.CHAR_REPRESENTATION_PRODUCTION;

public class Production{
    
    private NonTerminal nonterminal;
    private final LinkedList<Symbol> symbols;
    private LinkedList<String> elements;
        
    public Production(String line) throws InputMismatchException{
        this.symbols = new LinkedList<>();
        this.elements = new LinkedList<>();
        Grammar.splitLine(this.elements, line, ' ');
        if (this.elements.size() >= 3){
            if (CHAR_REPRESENTATION_PRODUCTION.equals(this.elements.get(1))){
                this.nonterminal = new NonTerminal(this.elements.get(0));
                for(int i=2;i<this.elements.size(); i++){
                    if (this.elements.get(i).charAt(0) == CHAR_REPRESENTATION_EMPTY){
                        this.nonterminal.setCanbenull(true);
                    }
                    this.symbols.add(new Symbol(this.elements.get(i)));
                }
            } else{
                throw new InputMismatchException("Error: expected" + CHAR_REPRESENTATION_PRODUCTION + "on symbol two, but " + this.elements.get(1));
            }
        } else{
            throw new InputMismatchException("Error: need at least three symbols per line in the grammar (NonTerm " + CHAR_REPRESENTATION_PRODUCTION + " Symbols)");
        }
        this.elements.clear();
    }
        
    public Production(NonTerminal nterm, LinkedList<Symbol> symbols, int location) throws InputMismatchException{
        this.symbols = new LinkedList<>();
        this.nonterminal = nterm;
        if (symbols.size() - 1 -  location <= 0){
            throw new InputMismatchException("Error: no production after |, expected any symbol");
        } else{
            for (int i=location + 1; i<symbols.size(); i++){
                this.symbols.add(symbols.get(i));
            }
        }
    }
        
    public NonTerminal getNonterminal(){
        return nonterminal;
    }
        
    public LinkedList<Symbol> getSymbols(){
        return this.symbols;
    } 
        
    public void setNonTerminal(NonTerminal nterm){
        this.nonterminal = nterm;
    }
        
}
