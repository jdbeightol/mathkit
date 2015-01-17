MathKit: A Mathematical Toolkit
=======

A toolkit for various mathematical problems, specifically: Linear maximization and minimization problems.

MathKit was inspired by the lectures of Dr. James Strayer at Lock Haven University.  While it currently represents only a small fraction of the subject matter he taught, the goal of this project is to incorporate a vast collection of mathematical tools to aid students and teachers alike in their educational adventures.

Current Features:

    -The Dantzig Simplex Algorithm for Canonical Tableaus with Anti-Cycling
     rules.

Planned Features:

    -Support for non-canonical tableaus and equation filing.
    -Support for Transportation Problems.
    -Support for Network flow optimization Problems.
    -Better graphical support for tableaus.
    -Mathematical tools relevant to disciplines beyond pure mathematics 
     (eg. Physics and Computer Science).

A Brief Explanation
-------------------

Console:

The console is the most important window of the application.  Here, all of the important information relevant to a tableau will be displayed.  

Tableaus:

Note that at any time, you may right click the table and choose "Check Tableau State" to output the current state of the tableau. This is useful for verifying variable order.

To create a new tableau:

    -Navigate to the "Math" menu on the main and choose "New Simplex 
     Tablaeu".  
     
    -From here, you can specify the number of variables and constraints 
     the problem has.  In addition, you can also specify a custom comma 
     separated list of max and min variables for the problem.  Note that
     a problem does not necessarily require variables; however, without 
     any variables, the anti-cycling rules will be ignored.
     
    -Click create to create a blank tableau.
    
    -Now, enter the values of the tableau into the cells of the table.
    
    -All tableau operations are performed by right-clicking a cell and 
     choosing an option from the menu.
     
    -Once an option is selected, the output of the operation will be 
     displayed in the Console, and the table will now display the updated 
     result of the tableau.
     
-----

IMPORTANT NOTICE:
I cannot guarantee this application is 100% free of error.  While I have tested and profiled the application extensively against various problems found in James Strayer's "Undergraduate Texts in Mathematics: Linear Programming  and Its Applications", obscure situations may exist which detriment the accuracy of the algorithm implementations.   As such, all results should be properly verified by hand and any contradictory results should be reported.
