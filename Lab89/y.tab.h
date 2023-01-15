/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     BEGINN = 258,
     END = 259,
     PRGRM = 260,
     VAR = 261,
     INT = 262,
     CHAR = 263,
     WHILE = 264,
     IF = 265,
     ELSE = 266,
     READ = 267,
     WRITE = 268,
     THEN = 269,
     DO = 270,
     plus = 271,
     minus = 272,
     mul = 273,
     division = 274,
     eq = 275,
     equal = 276,
     different = 277,
     less = 278,
     more = 279,
     lessOrEqual = 280,
     moreOrEqual = 281,
     leftRoundBracket = 282,
     rightRoundBracket = 283,
     semicolon = 284,
     colon = 285,
     dot = 286,
     comma = 287,
     leftSquaredBracket = 288,
     rightSquaredBracket = 289,
     IDENTIFIER = 290,
     NUMBER_CONST = 291,
     STRING_CONST = 292,
     CHAR_CONST = 293
   };
#endif
/* Tokens.  */
#define BEGINN 258
#define END 259
#define PRGRM 260
#define VAR 261
#define INT 262
#define CHAR 263
#define WHILE 264
#define IF 265
#define ELSE 266
#define READ 267
#define WRITE 268
#define THEN 269
#define DO 270
#define plus 271
#define minus 272
#define mul 273
#define division 274
#define eq 275
#define equal 276
#define different 277
#define less 278
#define more 279
#define lessOrEqual 280
#define moreOrEqual 281
#define leftRoundBracket 282
#define rightRoundBracket 283
#define semicolon 284
#define colon 285
#define dot 286
#define comma 287
#define leftSquaredBracket 288
#define rightSquaredBracket 289
#define IDENTIFIER 290
#define NUMBER_CONST 291
#define STRING_CONST 292
#define CHAR_CONST 293




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;

