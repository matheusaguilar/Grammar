# Grammar
  This is a library developed in Java that receives a grammar(String) and will give to you the terminals, non terminals, productions of the grammar, first and follow set.

# Getting started
  To work with this, u just need create an object of type Grammar and put a String in his constructor that follow this template:
  <br>"a non terminal -> symbols"
  
  <br><b>Example:</b>
  <br>
  ```java
String text_grammar = 
            "S -> ( A ) | &\n" + 
            "A -> T E\n" +
            "E -> x T E | &\n" + 
            "T -> ( A ) | a | bb | ccc";
        
        try{
            Grammar grammar = new Grammar(text_grammar);
            grammar.calculate();
            
            //Generated content:
            grammar.printNonTerminals();
            grammar.printTerminals();
            grammar.printProductions();
            grammar.printFirst();
            grammar.printFollow();
            
        } catch (InputMismatchException ex){
            System.out.println(ex.getMessage());
        }
  ```
  
  <br> <b>Note:</b>
  <br>
  * Spaces " " are very important, because the program try know every symbol, using them
  * "\n" are used to separate productions
  * By default empty symbol are represented with "&" and production with "->", both can be changed with some methods, see the 
  <a href="https://github.com/matheusaguilar/Grammar/wiki"> Wiki</a>
  
  <br><br> <b>First and Follow Set:</b>
  <br>
  
  <br> <b>Getting First Set:</b>
  ```java
   LinkedList nonterminals;
   nonterminals = grammar.getNonterminals();
   for(NonTerminal nterm : nonterminals){
    System.out.print(nterm.getSymbol() + " =");
    for(Terminal term: nterm.getFirst()){
      System.out.print(" " + term.getSymbol());
    }
    System.out.println("");
   }
   ```
  
  <br> <b>Getting Follow Set:</b>
  ```java
   LinkedList nonterminals;
   nonterminals = grammar.getNonterminals();
   for(NonTerminal nterm : nonterminals){
    System.out.print(nterm.getSymbol() + " =");
    for(Terminal term: nterm.getFollow()){
      if (!term.getSymbol().equals(Grammar.CHAR_REPRESENTATION_EOF)){
        System.out.print(" " + term.getSymbol());
      } else{
        System.out.print(" $");
      }
    }
    System.out.println("");
   }
   ```
  
# Docs
<a href="https://github.com/matheusaguilar/Grammar/wiki"><h3>Documentation</h3></a>.
  
# Future development
* Optimize the code

# Contact
Feel free to communicate any questions or concerns. matcatarino@yahoo.com.br
