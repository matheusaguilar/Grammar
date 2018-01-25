# Grammar
  This is an API developed in Java that receives a grammar(String) and will give to you the terminals, non terminals, productions of the grammar, first and follow set.

# Getting started
  To work with this API, u just create an object of type Grammar and put a String in his constructor that follow this template:
  <br>"a non terminal -> symbols"
  
  <br><b>Example:</b>
  <br>
 <br>String text_grammar = 
 <br>           "S -> ( A ) | &\n" + 
 <br>           "A -> T E\n" +
 <br>           "E -> x T E | &\n" + 
 <br>           "T -> ( A ) | a | bb | ccc";
  
  <br>Grammar grammar = new Grammar(text_grammar);
  <br>grammar.calculate();
  
  <br> <b>Note:</b>
  <br>
  <br>Spaces are very important, because the program try know every symbol, using them.
  <br> By default empty symbol are represented with "&" and production with "->", both can be changed with some methods, see the 
  <a href="https://github.com/matheusaguilar/Grammar/wiki"> Wiki</a>.
  
# Docs
<a href="https://github.com/matheusaguilar/Grammar/wiki"> <h3>Documentation</h3></a>.
  
# Future development
* Optimize the code

# Contact
Feel free to communicate any questions or concerns. matcatarino@yahoo.com.br
