/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sse2project;

/**
 *
 * @author Kalle
 */

import sse2project.AbstractSyntaxTrees.BotsProgram;
import sse2project.AbstractSyntaxTrees.CollaborationList;
import sse2project.AbstractSyntaxTrees.Collaboration;
import sse2project.AbstractSyntaxTrees.SequentialCollaboration;
import sse2project.AbstractSyntaxTrees.BotList;
import sse2project.AbstractSyntaxTrees.Bot;
import sse2project.AbstractSyntaxTrees.SequentialBot;
import sse2project.AbstractSyntaxTrees.OperationList;
import sse2project.AbstractSyntaxTrees.Operations;
import sse2project.AbstractSyntaxTrees.SequentialOperation;
import sse2project.AbstractSyntaxTrees.Identifier;
import sse2project.AbstractSyntaxTrees.Identifier2;
import sse2project.AbstractSyntaxTrees.IntegerLiteral;

///////////////////////////////////////////////////////////////////////////////
//
// UNUSED TRIANGLE IMPORTS!!!!!!!!
//
///////////////////////////////////////////////////////////////////////////////
//import Triangle.ErrorReporter;
//import Triangle.AbstractSyntaxTrees.ActualParameter;
//import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
//import Triangle.AbstractSyntaxTrees.ArrayAggregate;
//import Triangle.AbstractSyntaxTrees.ArrayExpression;
//import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
//import Triangle.AbstractSyntaxTrees.AssignCommand;
//import Triangle.AbstractSyntaxTrees.BinaryExpression;
//import Triangle.AbstractSyntaxTrees.CallCommand;
//import Triangle.AbstractSyntaxTrees.CallExpression;
//import Triangle.AbstractSyntaxTrees.CharacterExpression;
//import Triangle.AbstractSyntaxTrees.CharacterLiteral;
//import Triangle.AbstractSyntaxTrees.Command;
//import Triangle.AbstractSyntaxTrees.ConstActualParameter;
//import Triangle.AbstractSyntaxTrees.ConstDeclaration;
//import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
//import Triangle.AbstractSyntaxTrees.Declaration;
//import Triangle.AbstractSyntaxTrees.DotVname;
//import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
//import Triangle.AbstractSyntaxTrees.EmptyCommand;
//import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
//import Triangle.AbstractSyntaxTrees.Expression;
//import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
//import Triangle.AbstractSyntaxTrees.FormalParameter;
//import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
//import Triangle.AbstractSyntaxTrees.FuncActualParameter;
//import Triangle.AbstractSyntaxTrees.FuncDeclaration;
//import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
//import Triangle.AbstractSyntaxTrees.IfCommand;
//import Triangle.AbstractSyntaxTrees.IfExpression;
//import Triangle.AbstractSyntaxTrees.IntegerExpression;
//import Triangle.AbstractSyntaxTrees.LetCommand;
//import Triangle.AbstractSyntaxTrees.LetExpression;
//import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
//import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
//import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
//import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
//import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
//import Triangle.AbstractSyntaxTrees.Operator;
//import Triangle.AbstractSyntaxTrees.ProcActualParameter;
//import Triangle.AbstractSyntaxTrees.ProcDeclaration;
//import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
//import Triangle.AbstractSyntaxTrees.RecordAggregate;
//import Triangle.AbstractSyntaxTrees.RecordExpression;
//import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
//import Triangle.AbstractSyntaxTrees.SequentialCommand;
//import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
//import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
//import Triangle.AbstractSyntaxTrees.SimpleVname;
//import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
//import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
//import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
//import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
//import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
//import Triangle.AbstractSyntaxTrees.SubscriptVname;
//import Triangle.AbstractSyntaxTrees.TypeDeclaration;
//import Triangle.AbstractSyntaxTrees.TypeDenoter;
//import Triangle.AbstractSyntaxTrees.UnaryExpression;
//import Triangle.AbstractSyntaxTrees.VarActualParameter;
//import Triangle.AbstractSyntaxTrees.VarDeclaration;
//import Triangle.AbstractSyntaxTrees.VarFormalParameter;
//import Triangle.AbstractSyntaxTrees.Vname;
//import Triangle.AbstractSyntaxTrees.VnameExpression;
//import Triangle.AbstractSyntaxTrees.WhileCommand;

public class Parser {

  private Scanner lexicalAnalyser;
  //private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer) { //, ErrorReporter reporter
    lexicalAnalyser = lexer;
    //errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    //errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public BotsProgram parseBotsProgram() {

    BotsProgram programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();

    try {
      CollaborationList cAST = parseCollaborationList();
      programAST = new BotsProgram(cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { return null; }
    return programAST;
  }
///////////////////////////////////////////////////////////////////////////////
//
// COLLABORATIONLISTS
//
///////////////////////////////////////////////////////////////////////////////
    CollaborationList parseCollaborationList() throws SyntaxError {

        CollaborationList c1AST = parseCollaboration();
        while (currentToken.kind == Token.COLLABORATION){
            //accept(Token.COLLABORATION);
            CollaborationList c2AST = parseCollaboration();
            c1AST = new SequentialCollaboration(c1AST, c2AST);//, currentToken.position);
            }
        return c1AST;
    }



///////////////////////////////////////////////////////////////////////////////
//
// COLLABORATIONS
//
///////////////////////////////////////////////////////////////////////////////
    Collaboration parseCollaboration() throws SyntaxError{
        Collaboration cAST;// = parseCollaboration();
        accept(Token.COLLABORATION);    
        Identifier ID = parseIdentifier();       
        BotList b1 = parseBotList();
        accept(Token.LBRACKET);
        OperationList o1 = parseOperationList();
        accept(Token.RBRACKET);
        cAST = new Collaboration(ID, b1, o1);
        return cAST;
    }

///////////////////////////////////////////////////////////////////////////////
//
// BOTLISTS
//
///////////////////////////////////////////////////////////////////////////////
    BotList parseBotList() throws SyntaxError{
        BotList b1AST;
        accept(Token.BETWEEN);
        b1AST = parseBot();

        while (currentToken.kind == Token.COMMA){
            accept(Token.COMMA);
            BotList b2AST = parseBot();
            b1AST = new SequentialBot(b1AST, b2AST);//, currentToken.position);
        }        
        return b1AST;
    }
///////////////////////////////////////////////////////////////////////////////
//
// BOTS
//
///////////////////////////////////////////////////////////////////////////////
    Bot parseBot() throws SyntaxError {
        Bot B;// = parseBot();
        IntegerLiteral IL = parseIntegerLiteral();
        accept(Token.INTLITERAL);
        B = new Bot(IL);
        return B;
    }
///////////////////////////////////////////////////////////////////////////////
//
// OPERATIONLISTS
//
///////////////////////////////////////////////////////////////////////////////
    OperationList parseOperationList() throws SyntaxError {
        OperationList o1AST =  parseOperation();
        
        while(currentToken.kind == Token.SEMICOLON){
             accept(Token.SEMICOLON);
             if(currentToken.kind == Token.RBRACKET)
                 break;
            OperationList o2AST = parseOperation();
            o1AST = new SequentialOperation(o1AST, o2AST, currentToken.position);
        }
     return o1AST;
    }

///////////////////////////////////////////////////////////////////////////////
//
// OPERATIONS
//
///////////////////////////////////////////////////////////////////////////////
    Operations parseOperation()throws SyntaxError {
        Operations OAST=null;// = parseOperation();
        Identifier2 ID2 = new Identifier2(currentToken.spelling, previousTokenPosition);
        accept(Token.IDENTIFIER);        
        accept(Token.BY);
        IntegerLiteral IL =   parseIntegerLiteral();
        accept(Token.INTLITERAL);
        OAST  = new Operations(ID2, IL, previousTokenPosition);
        return OAST;
    }

///////////////////////////////////////////////////////////////////////////////
//
// IDENTIFIER
//
///////////////////////////////////////////////////////////////////////////////
    Identifier parseIdentifier() throws SyntaxError {
        Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
    }
///////////////////////////////////////////////////////////////////////////////
//
// INTEGER-LITERALS
//
///////////////////////////////////////////////////////////////////////////////
    public IntegerLiteral parseIntegerLiteral() throws SyntaxError {
        // parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  //IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      ///currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }
    

///////////////////////////////////////////////////////////////////////////////
//
// IDENTIFIER2
//
///////////////////////////////////////////////////////////////////////////////
    Identifier2 parseIdentifier2()throws SyntaxError {
        Identifier2 ID2AST = parseIdentifier2();
        if(currentToken.kind == Token.IDENTIFIER){//Token.WORK
            acceptIt();
        }
        else if (currentToken.kind == Token.IDENTIFIER){//Token.MOVE
            acceptIt();
        }
     return ID2AST;
    }






///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////



// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

//  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
//    CharacterLiteral CL = null;
//
//    if (currentToken.kind == Token.CHARLITERAL) {
//      previousTokenPosition = currentToken.position;
//      String spelling = currentToken.spelling;
//      CL = new CharacterLiteral(spelling, previousTokenPosition);
//      currentToken = lexicalAnalyser.scan();
//    } else {
//      CL = null;
//      syntacticError("character literal expected here", "");
//    }
//    return CL;
//  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

//  Identifier parseIdentifier() throws SyntaxError {
//    Identifier I = null;
//
//    if (currentToken.kind == Token.IDENTIFIER) {
//      previousTokenPosition = currentToken.position;
//      String spelling = currentToken.spelling;
//      I = new Identifier(spelling, previousTokenPosition);
//      currentToken = lexicalAnalyser.scan();
//    } else {
//      I = null;
//      syntacticError("identifier expected here", "");
//    }
//    return I;
//  }

// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

//  Operator parseOperator() throws SyntaxError {
//    Operator O = null;
//
//    if (currentToken.kind == Token.OPERATOR) {
//      previousTokenPosition = currentToken.position;
//      String spelling = currentToken.spelling;
//      O = new Operator(spelling, previousTokenPosition);
//      currentToken = lexicalAnalyser.scan();
//    } else {
//      O = null;
//      syntacticError("operator expected here", "");
//    }
//    return O;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.
//parseCollaboration parses the collaboration, and constructs an AST
// to represent its phrase structure.

//  Collaboration parseCollaboration() throws SyntaxError {
//    Collaboration collaborationAST = null; // in case there's a syntactic error
//
//    SourcePosition collaborationPos = new SourcePosition();
//
//    start(collaborationPos);
//    collaborationAST = parseSingleCollaboration();
//    while (currentToken.kind == Token.SEMICOLON) {
//      acceptIt();
//      Collaboration c2AST = parseSingleCollaboration();
//      finish(collaborationPos);
//      collaborationAST = new SequentialCollaboration(collaborationAST, c2AST, collaborationPos);
//    }
//    return collaborationAST;
//  }

//  Collaboration parseSingleCollaboration() throws SyntaxError {
//    Collaboration commandAST = null; // in case there's a syntactic error
//
//    SourcePosition collaborationPos = new SourcePosition();
//    start(collaborationPos);
//
//    switch (currentToken.kind) {
//
//    case Token.COLLABORATION;
//    {
//        Collaboration cAST = parseCollaboration();
//        if (currentToken.kind == Token.IDENTIFIER){
//            acceptIt();
//
//    }
//    }
//      case Token.IDENTIFIER:
//      {
//        Identifier iAST = parseIdentifier();
//        if (currentToken.kind == Token.LPAREN) {
//          acceptIt();
//          ActualParameterSequence apsAST = parseActualParameterSequence();
//          accept(Token.RPAREN);
//          finish(commandPos);
//          commandAST = new CallCommand(iAST, apsAST, commandPos);
//
//        } else {
//
//          Vname vAST = parseRestOfVname(iAST);
//          accept(Token.BECOMES);
//          Expression eAST = parseExpression();
//          finish(commandPos);
//          commandAST = new AssignCommand(vAST, eAST, commandPos);
//        }
//      }
//      break;
//
//    case Token.BEGIN:
//      acceptIt();
//      commandAST = parseCommand();
//      accept(Token.END);
//      break;
//
//    case Token.LET:
//      {
//        acceptIt();
//        Declaration dAST = parseDeclaration();
//        accept(Token.IN);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new LetCommand(dAST, cAST, commandPos);
//      }
//      break;
//
//    case Token.IF:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.THEN);
//        Command c1AST = parseSingleCommand();
//        accept(Token.ELSE);
//        Command c2AST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
//      }
//      break;
//
//    case Token.WHILE:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.DO);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new WhileCommand(eAST, cAST, commandPos);
//      }
//      break;
//
//    case Token.SEMICOLON:
//    case Token.BY:
//    case Token.BETWEEN:
//    case Token.END:
//    case Token.ELSE:
//    case Token.IN:
//    case Token.EOT:
//
//      finish(commandPos);
//      commandAST = new EmptyCommand(commandPos);
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start a command",
//        currentToken.spelling);
//      break;
//
//    }

//    return commandAST;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

//  Expression parseExpression() throws SyntaxError {
//    Expression expressionAST = null; // in case there's a syntactic error
//
//    SourcePosition expressionPos = new SourcePosition();
//
//    start (expressionPos);
//
//    switch (currentToken.kind) {
//
//    case Token.LET:
//      {
//        acceptIt();
//        Declaration dAST = parseDeclaration();
//        accept(Token.IN);
//        Expression eAST = parseExpression();
//        finish(expressionPos);
//        expressionAST = new LetExpression(dAST, eAST, expressionPos);
//      }
//      break;
//
//    case Token.IF:
//      {
//        acceptIt();
//        Expression e1AST = parseExpression();
//        accept(Token.THEN);
//        Expression e2AST = parseExpression();
//        accept(Token.ELSE);
//        Expression e3AST = parseExpression();
//        finish(expressionPos);
//        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
//      }
//      break;
//
//    default:
//      expressionAST = parseSecondaryExpression();
//      break;
//    }
//    return expressionAST;
//  }
//
//  Expression parseSecondaryExpression() throws SyntaxError {
//    Expression expressionAST = null; // in case there's a syntactic error
//
//    SourcePosition expressionPos = new SourcePosition();
//    start(expressionPos);
//
//    expressionAST = parsePrimaryExpression();
//    while (currentToken.kind == Token.OPERATOR) {
//      Operator opAST = parseOperator();
//      Expression e2AST = parsePrimaryExpression();
//      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
//        expressionPos);
//    }
//    return expressionAST;
//  }
//
//  Expression parsePrimaryExpression() throws SyntaxError {
//    Expression expressionAST = null; // in case there's a syntactic error
//
//    SourcePosition expressionPos = new SourcePosition();
//    start(expressionPos);
//
//    switch (currentToken.kind) {
//
//    case Token.INTLITERAL:
//      {
//        IntegerLiteral ilAST = parseIntegerLiteral();
//        finish(expressionPos);
//        expressionAST = new IntegerExpression(ilAST, expressionPos);
//      }
//      break;
//
//    case Token.CHARLITERAL:
//      {
//        CharacterLiteral clAST= parseCharacterLiteral();
//        finish(expressionPos);
//        expressionAST = new CharacterExpression(clAST, expressionPos);
//      }
//      break;
//
//    case Token.LBRACKET:
//      {
//        acceptIt();
//        ArrayAggregate aaAST = parseArrayAggregate();
//        accept(Token.RBRACKET);
//        finish(expressionPos);
//        expressionAST = new ArrayExpression(aaAST, expressionPos);
//      }
//      break;
//
//    case Token.LCURLY:
//      {
//        acceptIt();
//        RecordAggregate raAST = parseRecordAggregate();
//        accept(Token.RCURLY);
//        finish(expressionPos);
//        expressionAST = new RecordExpression(raAST, expressionPos);
//      }
//      break;
//
//    case Token.IDENTIFIER:
//      {
//        Identifier iAST= parseIdentifier();
//        if (currentToken.kind == Token.LPAREN) {
//          acceptIt();
//          ActualParameterSequence apsAST = parseActualParameterSequence();
//          accept(Token.RPAREN);
//          finish(expressionPos);
//          expressionAST = new CallExpression(iAST, apsAST, expressionPos);
//
//        } else {
//          Vname vAST = parseRestOfVname(iAST);
//          finish(expressionPos);
//          expressionAST = new VnameExpression(vAST, expressionPos);
//        }
//      }
//      break;
//
//    case Token.OPERATOR:
//      {
//        Operator opAST = parseOperator();
//        Expression eAST = parsePrimaryExpression();
//        finish(expressionPos);
//        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
//      }
//      break;
//
//    case Token.LPAREN:
//      acceptIt();
//      expressionAST = parseExpression();
//      accept(Token.RPAREN);
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start an expression",
//        currentToken.spelling);
//      break;
//
//    }
//    return expressionAST;
//  }
//
//  RecordAggregate parseRecordAggregate() throws SyntaxError {
//    RecordAggregate aggregateAST = null; // in case there's a syntactic error
//
//    SourcePosition aggregatePos = new SourcePosition();
//    start(aggregatePos);
//
//    Identifier iAST = parseIdentifier();
//    accept(Token.IS);
//    Expression eAST = parseExpression();
//
//    if (currentToken.kind == Token.COMMA) {
//      acceptIt();
//      RecordAggregate aAST = parseRecordAggregate();
//      finish(aggregatePos);
//      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
//    } else {
//      finish(aggregatePos);
//      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
//    }
//    return aggregateAST;
//  }
//
//  ArrayAggregate parseArrayAggregate() throws SyntaxError {
//    ArrayAggregate aggregateAST = null; // in case there's a syntactic error
//
//    SourcePosition aggregatePos = new SourcePosition();
//    start(aggregatePos);
//
//    Expression eAST = parseExpression();
//    if (currentToken.kind == Token.COMMA) {
//      acceptIt();
//      ArrayAggregate aAST = parseArrayAggregate();
//      finish(aggregatePos);
//      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
//    } else {
//      finish(aggregatePos);
//      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
//    }
//    return aggregateAST;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

//  Vname parseVname () throws SyntaxError {
//    Vname vnameAST = null; // in case there's a syntactic error
//    Identifier iAST = parseIdentifier();
//    vnameAST = parseRestOfVname(iAST);
//    return vnameAST;
//  }
//
//  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
//    SourcePosition vnamePos = new SourcePosition();
//    vnamePos = identifierAST.position;
//    Vname vAST = new SimpleVname(identifierAST, vnamePos);
//
//    while (currentToken.kind == Token.DOT ||
//           currentToken.kind == Token.LBRACKET) {
//
//      if (currentToken.kind == Token.DOT) {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        vAST = new DotVname(vAST, iAST, vnamePos);
//      } else {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.RBRACKET);
//        finish(vnamePos);
//        vAST = new SubscriptVname(vAST, eAST, vnamePos);
//      }
//    }
//    return vAST;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

//  Declaration parseDeclaration() throws SyntaxError {
//    Declaration declarationAST = null; // in case there's a syntactic error
//
//    SourcePosition declarationPos = new SourcePosition();
//    start(declarationPos);
//    declarationAST = parseSingleDeclaration();
//    while (currentToken.kind == Token.SEMICOLON) {
//      acceptIt();
//      Declaration d2AST = parseSingleDeclaration();
//      finish(declarationPos);
//      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
//        declarationPos);
//    }
//    return declarationAST;
//  }
//
//  Declaration parseSingleDeclaration() throws SyntaxError {
//    Declaration declarationAST = null; // in case there's a syntactic error
//
//    SourcePosition declarationPos = new SourcePosition();
//    start(declarationPos);
//
//    switch (currentToken.kind) {
//
//    case Token.CONST:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.IS);
//        Expression eAST = parseExpression();
//        finish(declarationPos);
//        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
//      }
//      break;
//
//    case Token.VAR:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.COLON);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(declarationPos);
//        declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
//      }
//      break;
//
//    case Token.PROC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.LPAREN);
//        FormalParameterSequence fpsAST = parseFormalParameterSequence();
//        accept(Token.RPAREN);
//        accept(Token.IS);
//        Command cAST = parseSingleCommand();
//        finish(declarationPos);
//        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
//      }
//      break;
//
//    case Token.FUNC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.LPAREN);
//        FormalParameterSequence fpsAST = parseFormalParameterSequence();
//        accept(Token.RPAREN);
//        accept(Token.COLON);
//        TypeDenoter tAST = parseTypeDenoter();
//        accept(Token.IS);
//        Expression eAST = parseExpression();
//        finish(declarationPos);
//        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
//          declarationPos);
//      }
//      break;
//
//    case Token.TYPE:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.IS);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(declarationPos);
//        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
//      }
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start a declaration",
//        currentToken.spelling);
//      break;
//
//    }
//    return declarationAST;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

//  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
//    FormalParameterSequence formalsAST;
//
//    SourcePosition formalsPos = new SourcePosition();
//
//    start(formalsPos);
//    if (currentToken.kind == Token.RPAREN) {
//      finish(formalsPos);
//      formalsAST = new EmptyFormalParameterSequence(formalsPos);
//
//    } else {
//      formalsAST = parseProperFormalParameterSequence();
//    }
//    return formalsAST;
//  }
//
//  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
//    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;
//
//    SourcePosition formalsPos = new SourcePosition();
//    start(formalsPos);
//    FormalParameter fpAST = parseFormalParameter();
//    if (currentToken.kind == Token.COMMA) {
//      acceptIt();
//      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
//      finish(formalsPos);
//      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
//        formalsPos);
//
//    } else {
//      finish(formalsPos);
//      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
//    }
//    return formalsAST;
//  }

//  FormalParameter parseFormalParameter() throws SyntaxError {
//    FormalParameter formalAST = null; // in case there's a syntactic error;
//
//    SourcePosition formalPos = new SourcePosition();
//    start(formalPos);
//
//    switch (currentToken.kind) {

//    case Token.IDENTIFIER:
//      {
//        Identifier iAST = parseIdentifier();
//        accept(Token.COLON);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(formalPos);
//        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
//      }
//      break;

//    case Token.VAR:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.COLON);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(formalPos);
//        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
//      }
//      break;

//    case Token.PROC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.LPAREN);
//        FormalParameterSequence fpsAST = parseFormalParameterSequence();
//        accept(Token.RPAREN);
//        finish(formalPos);
//        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
//      }
//      break;

//    case Token.FUNC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        accept(Token.LPAREN);
//        FormalParameterSequence fpsAST = parseFormalParameterSequence();
//        accept(Token.RPAREN);
//        accept(Token.COLON);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(formalPos);
//        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
//      }
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start a formal parameter",
//        currentToken.spelling);
//      break;
//
//    }
//    return formalAST;
//  }


//  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
//    ActualParameterSequence actualsAST;
//
//    SourcePosition actualsPos = new SourcePosition();
//
//    start(actualsPos);
//    if (currentToken.kind == Token.RPAREN) {
//      finish(actualsPos);
//      actualsAST = new EmptyActualParameterSequence(actualsPos);
//
//    } else {
//      actualsAST = parseProperActualParameterSequence();
//    }
//    return actualsAST;
//  }
//
//  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
//    ActualParameterSequence actualsAST = null; // in case there's a syntactic error
//
//    SourcePosition actualsPos = new SourcePosition();
//
//    start(actualsPos);
//    ActualParameter apAST = parseActualParameter();
//    if (currentToken.kind == Token.COMMA) {
//      acceptIt();
//      ActualParameterSequence apsAST = parseProperActualParameterSequence();
//      finish(actualsPos);
//      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
//        actualsPos);
//    } else {
//      finish(actualsPos);
//      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
//    }
//    return actualsAST;
//  }
//
//  ActualParameter parseActualParameter() throws SyntaxError {
//    ActualParameter actualAST = null; // in case there's a syntactic error
//
//    SourcePosition actualPos = new SourcePosition();
//
//    start(actualPos);
//
//    switch (currentToken.kind) {
//
//    case Token.IDENTIFIER:
//    case Token.INTLITERAL:
//    case Token.CHARLITERAL:
//    case Token.OPERATOR:
//    case Token.LET:
//    case Token.IF:
//    case Token.LPAREN:
//    case Token.LBRACKET:
//    case Token.LCURLY:
//      {
//        Expression eAST = parseExpression();
//        finish(actualPos);
//        actualAST = new ConstActualParameter(eAST, actualPos);
//      }
//      break;
//
//    case Token.VAR:
//      {
//        acceptIt();
//        Vname vAST = parseVname();
//        finish(actualPos);
//        actualAST = new VarActualParameter(vAST, actualPos);
//      }
//      break;
//
//    case Token.PROC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        finish(actualPos);
//        actualAST = new ProcActualParameter(iAST, actualPos);
//      }
//      break;
//
//    case Token.FUNC:
//      {
//        acceptIt();
//        Identifier iAST = parseIdentifier();
//        finish(actualPos);
//        actualAST = new FuncActualParameter(iAST, actualPos);
//      }
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start an actual parameter",
//        currentToken.spelling);
//      break;
//
//    }
//    return actualAST;
//  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

//  TypeDenoter parseTypeDenoter() throws SyntaxError {
//    TypeDenoter typeAST = null; // in case there's a syntactic error
//    SourcePosition typePos = new SourcePosition();
//
//    start(typePos);
//
//    switch (currentToken.kind) {
//
//    case Token.IDENTIFIER:
//      {
//        Identifier iAST = parseIdentifier();
//        finish(typePos);
//        typeAST = new SimpleTypeDenoter(iAST, typePos);
//      }
//      break;
//
//    case Token.ARRAY:
//      {
//        acceptIt();
//        IntegerLiteral ilAST = parseIntegerLiteral();
//        accept(Token.OF);
//        TypeDenoter tAST = parseTypeDenoter();
//        finish(typePos);
//        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
//      }
//      break;
//
//    case Token.RECORD:
//      {
//        acceptIt();
//        FieldTypeDenoter fAST = parseFieldTypeDenoter();
//        accept(Token.END);
//        finish(typePos);
//        typeAST = new RecordTypeDenoter(fAST, typePos);
//      }
//      break;
//
//    default:
//      syntacticError("\"%\" cannot start a type denoter",
//        currentToken.spelling);
//      break;
//
//    }
//    return typeAST;
//  }
//
//  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
//    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error
//
//    SourcePosition fieldPos = new SourcePosition();
//
//    start(fieldPos);
//    Identifier iAST = parseIdentifier();
//    accept(Token.COLON);
//    TypeDenoter tAST = parseTypeDenoter();
//    if (currentToken.kind == Token.COMMA) {
//      acceptIt();
//      FieldTypeDenoter fAST = parseFieldTypeDenoter();
//      finish(fieldPos);
//      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
//    } else {
//      finish(fieldPos);
//      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
//    }
//    return fieldAST;
//  }
}
