package mcagrammar;

/**
 * If some bug was found, let me know it, comment on GitHub project.
 * https://github.com/matheusaguilar/Grammar
 * 
 * @author Matheus Catarino de Aguilar
 */

import java.util.LinkedList;

public class NonTerminal extends Symbol{
    
    private final LinkedList<Terminal> first, follow;
    private boolean canbenull;
    
    public NonTerminal(String symbol) {
        super(symbol);
        this.first = new LinkedList<>();
        this.follow = new LinkedList<>();
        canbenull = false;
    }

    public LinkedList<Terminal> getFirst() {
        return first;
    }

    public LinkedList<Terminal> getFollow() {
        return follow;
    }

    public boolean isCanbenull() {
        return canbenull;
    }

    public void setCanbenull(boolean canbenull) {
        this.canbenull = canbenull;
    }
    
    public void addFirst(Terminal term){
        if (!isAlreadAdded(first, term)){
            first.add(term);
        }
    }
   
    public void addFirst(LinkedList<Terminal> firsts){
        for(Terminal first1 : firsts){        
            if (!isAlreadAdded(first, first1)){
                first.add(first1);
            }
        }
    }
    
    public void addFollow(Terminal term){
        if (!isAlreadAdded(follow, term)){
            if (term.getSymbol().charAt(0) != Grammar.CHAR_REPRESENTATION_EMPTY){
                follow.add(term);
            }
        }
    }
   
    public void addFollow(LinkedList<Terminal> follows){
        for(Terminal follows1 : follows){        
            if (!isAlreadAdded(follow, follows1)){
                if (follows1.getSymbol().charAt(0) != Grammar.CHAR_REPRESENTATION_EMPTY){
                    follow.add(follows1);
                }
            }
        }
    }
    
    private boolean isAlreadAdded(LinkedList<Terminal> list, Terminal term){
        boolean added = false;
        for(Terminal list1 : list){
            if (list1.getSymbol().equals(term.getSymbol())){
                added = true;
            }
        }
        return added;
    }
     
}
