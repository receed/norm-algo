# Markov algorithm evaluation

```
java -jar prog-2020-norm-algo-receed.main.jar FILE [OPTION]...
```

or edit arguments in Gradle run configuration.

## Input file format

In the top of the input file substitution formulas should be 
listed one per line. Line corresponding to simple formula L->D should have
form "L D" (quotes for clarity) and line corresponding to final formula
L->.D should be "L D .". Lines after formulas are interpreted as input words
for the algorithm. If left or right word in the formula or input word is
empty (epsilon) it must be replaced by a special sequence ("\\" by default, 
see options for details)  

## Options

* -o FILE - output file when processing single input (default stdout)
* -e WORD - string to denote empty word (default \\)
* -mo NUM - maximum number of operations to perform (default 1000)
* -ml NUM - maximum length of resulting string (default 1000)
* -v - show applied formulas
* -b - batch processing. Input file is interpreted as list of files to
process, result for file "file" is written to "file.out".


