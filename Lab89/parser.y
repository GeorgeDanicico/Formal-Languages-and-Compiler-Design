%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token BEGINN
%token END
%token PRGRM
%token VAR
%token INT
%token CHAR
%token WHILE
%token IF
%token ELSE
%token READ
%token WRITE
%token THEN
%token DO

%token plus
%token minus
%token mul
%token division
%token eq
%token equal
%token different
%token less
%token more
%token lessOrEqual
%token moreOrEqual

%token leftRoundBracket
%token rightRoundBracket
%token semicolon
%token colon
%token dot
%token comma
%token leftSquaredBracket
%token rightSquaredBracket

%token IDENTIFIER
%token NUMBER_CONST
%token STRING_CONST
%token CHAR_CONST

%start program

%%

program : PRGRM IDENTIFIER semicolon VAR decllist BEGINN stmtlist END dot
decllist : declaration semicolon decllist | /*Empty*/
declaration : IDENTIFIER colon type identifierlist
identifierlist : comma declaration | /*Empty*/
simpletype : CHAR | INT
type : simpletype 
stmtlist : stmt semicolon stmtlist | /*Empty*/
stmt : simplestmt | structstmt
simplestmt : assignstmt | iostmt
assignstmt : IDENTIFIER eq expression
sign : plus | minus | mul | division
expression : term sign_and_expression
sign_and_expression : sign expression | /*Empty*/
term : IDENTIFIER | NUMBER_CONST
iostmt : readstmt | writestmt
readstmt : READ IDENTIFIER
writestmt : WRITE writeinput
writeinput : term | STRING_CONST
structstmt : ifstmt | whilestmt
expression1 : expression
expression2 : expression
condition : expression1 relation expression2
ifstmt : IF condition THEN stmt
whilestmt : WHILE condition DO stmt
relation : less | lessOrEqual | equal | different | moreOrEqual | more

%%

yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tProgram is syntactically correct.\n");
}