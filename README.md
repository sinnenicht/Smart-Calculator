# Smart Calculator
 
A program that can perform basic operations on integers. Supports the use of variables.

Prerequisites
-------------

This program requires Java to run and compile.

Installation
------------

1. Download this repository and unzip the .zip file in your desired location.
2. Using the command line, navigate to \Smart-Calculator-master\src\calculator.
3. Compile the program using the command `javac Main.java`.

Usage
-----

Once the program has been compiled, it can be run from the command line by navigating to \Smart-Calculator-master\src and using the command `java calculator.Main`.

While Smart Calculator is running, the user may enter a command, single value, variable assignment, or expression.

Smart Calculator has two commands: `/help` and `/exit`. `/help` will print some instructions on how to use the program, while `/exit` will stop the program.

Entering a single value, whether it is a number or a variable, will result in the same value being printed. In the case of a variable name being entered, its numerical value is printed. Numbers must be integers, i.e. no fractions or decimals.

Variables are assigned using the variable name followed by the `=` character, followed by the variable assignmnet. Variable names may only contain Latin alphabet characters and are case sensitive. Variables can be assigned numbers, other variables, and expressions.

Expressions may contain parentheses and any of the following operations: `+`, `-`, `*`, `/`, `^`. `+` and `-` may appear consecutively (e.g. `+++` or `---`). In the case of `-`, an odd number of characters will result in subtraction, while an even number will result in addition.

Credits
-------

**Author:** Kate Jordan - [sinnenicht](https://github.com/sinnenicht/)

This program is based on the Smart Calculator project on [Jet Brains Academy](https://hyperskill.org/projects/42?goal=7).

License
-------

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](https://github.com/sinnenicht/Smart-Calculator/blob/master/LICENSE) for details.
