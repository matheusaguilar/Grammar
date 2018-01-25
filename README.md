# Grammar
  This is an API developed in Java that receives a grammar(String) and will give to you the terminals, non terminals, productions of the grammar, first and follow set.

# Getting started
  To work with this API, u just create an object of type Grammar and put a String in his constructor that follow this template:
  <br>"a non terminal -> symbols"
  
  <br><b>Example:</b>
  <br>
<br>String text_grammar = 
<br>            "S -> ( A ) | &\n" + 
<br>            "A -> T E\n" +
<br>            "E -> x T E | &\n" + 
<br>            "T -> ( A ) | a | bb | ccc";
        
<br>        try{
<br>            Grammar grammar = new Grammar(text_grammar);
<br>            grammar.calculate();
            
 <br>           //Generated content:
 <br>           grammar.printNonTerminals();
 <br>           grammar.printTerminals();
 <br>           grammar.printProductions();
 <br>           grammar.printFirst();
 <br>           grammar.printFollow();
            
 <br>       } catch (InputMismatchException ex){
 <br>           System.out.println(ex.getMessage());
 <br>       }
  
  <br> <b>Note:</b>
  <br>
  * Spaces " " are very important, because the program try know every symbol, using them
  * "\n" are used to separate productions
  * By default empty symbol are represented with "&" and production with "->", both can be changed with some methods, see the 
  <a href="https://github.com/matheusaguilar/Grammar/wiki"> Wiki</a>
  
  <br> <b>Getting First Set:</b>
  <br> LinkedList<NonTerminal> nonterminals;
  <br> nonterminals = grammar.getNonterminals();
  <br> for(NonTerminal nterm : nonterminals){
  <br> System.out.print(nterm.getSymbol() + " =");
  <br> for(Terminal term: nterm.getFirst()){
  <br> System.out.print(" " + term.getSymbol());
  <br> }
  <br> System.out.println("");
  <br> }
  
  <br> <b>Getting Follow Set:</b>
  <br> LinkedList<NonTerminal> nonterminals;
  <br> nonterminals = grammar.getNonterminals();
  <br> for(NonTerminal nterm : nonterminals){
  <br> System.out.print(nterm.getSymbol() + " =");
  <br> for(Terminal term: nterm.getFollow()){
  <br> if (!term.getSymbol().equals(CHAR_REPRESENTATION_EOF)){
  <br> System.out.print(" " + term.getSymbol());
  <br> } else{
  <br> System.out.print(" $");
  <br> }
  <br> }
  <br> System.out.println("");
  <br> }
  
# Docs
<a href="https://github.com/matheusaguilar/Grammar/wiki"><h3>Documentation</h3></a>.
  
# Future development
* Optimize the code

# Contact
Feel free to communicate any questions or concerns. matcatarino@yahoo.com.br
