JAVAC=javac
JAVAC_FLAGS=-sourcepath .
JAVA=java
JAVA_FLAGS=
CUP=java_cup.Main
CUPFLAGS=-nopositions -expect 100
JLEX=JLex.Main
all: test

test: absyn Latte/Yylex.class Latte/PrettyPrinter.class Latte/Test.class Latte/ComposVisitor.class Latte/AbstractVisitor.class Latte/FoldVisitor.class Latte/AllVisitor.class Latte/parser.class Latte/sym.class Latte/Test.class

.PHONY: absyn

%.class: %.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn: Latte/Absyn/Program.java Latte/Absyn/Prog.java Latte/Absyn/TopDef.java Latte/Absyn/FnDef.java Latte/Absyn/ClassDef.java Latte/Absyn/ClassDefE.java Latte/Absyn/ListTopDef.java Latte/Absyn/Arg.java Latte/Absyn/Ar.java Latte/Absyn/ListArg.java Latte/Absyn/FuncDef.java Latte/Absyn/FunDef.java Latte/Absyn/ClassBody.java Latte/Absyn/ClassBlock.java Latte/Absyn/ClassElem.java Latte/Absyn/ClassMeth.java Latte/Absyn/ClassAtr.java Latte/Absyn/ListClassElem.java Latte/Absyn/Block.java Latte/Absyn/Blk.java Latte/Absyn/ListStmt.java Latte/Absyn/Stmt.java Latte/Absyn/Empty.java Latte/Absyn/BStmt.java Latte/Absyn/Decl.java Latte/Absyn/Ass.java Latte/Absyn/Incr.java Latte/Absyn/Decr.java Latte/Absyn/Ret.java Latte/Absyn/VRet.java Latte/Absyn/Cond.java Latte/Absyn/CondElse.java Latte/Absyn/While.java Latte/Absyn/For.java Latte/Absyn/SExp.java Latte/Absyn/Item.java Latte/Absyn/NoInit.java Latte/Absyn/Init.java Latte/Absyn/ListItem.java Latte/Absyn/Type.java Latte/Absyn/Arr.java Latte/Absyn/Int.java Latte/Absyn/Str.java Latte/Absyn/Bool.java Latte/Absyn/Void.java Latte/Absyn/Obj.java Latte/Absyn/Expr.java Latte/Absyn/ECastNull.java Latte/Absyn/EAttrAccess.java Latte/Absyn/EMthCall.java Latte/Absyn/EArrAccess.java Latte/Absyn/EVar.java Latte/Absyn/ENewArr.java Latte/Absyn/ENew.java Latte/Absyn/ELitInt.java Latte/Absyn/ELitTrue.java Latte/Absyn/ELitFalse.java Latte/Absyn/EApp.java Latte/Absyn/EString.java Latte/Absyn/Neg.java Latte/Absyn/Not.java Latte/Absyn/EMul.java Latte/Absyn/EAdd.java Latte/Absyn/ERel.java Latte/Absyn/EAnd.java Latte/Absyn/EOr.java Latte/Absyn/ListExpr.java Latte/Absyn/FunctionCall.java Latte/Absyn/FCall.java Latte/Absyn/ArrAccess.java Latte/Absyn/ArrAcc.java Latte/Absyn/AttrAccess.java Latte/Absyn/AttrAcc.java Latte/Absyn/MethodCall.java Latte/Absyn/MCall.java Latte/Absyn/LValue.java Latte/Absyn/LVFCall.java Latte/Absyn/LVMethodCall.java Latte/Absyn/LVSimpleIdent.java Latte/Absyn/LVEArrAccess.java Latte/Absyn/LVAttrAcc.java Latte/Absyn/AddOp.java Latte/Absyn/Plus.java Latte/Absyn/Minus.java Latte/Absyn/MulOp.java Latte/Absyn/Times.java Latte/Absyn/Div.java Latte/Absyn/Mod.java Latte/Absyn/RelOp.java Latte/Absyn/LTH.java Latte/Absyn/LE.java Latte/Absyn/GTH.java Latte/Absyn/GE.java Latte/Absyn/EQU.java Latte/Absyn/NE.java
	${JAVAC} ${JAVAC_FLAGS} $^

Latte/Yylex.java: Latte/Yylex
	${JAVA} ${JAVA_FLAGS} ${JLEX} Latte/Yylex

Latte/sym.java Latte/parser.java: Latte/Latte.cup
	${JAVA} ${JAVA_FLAGS} ${CUP} ${CUPFLAGS} Latte/Latte.cup
	mv sym.java parser.java Latte/

Latte/Yylex.class: Latte/Yylex.java Latte/sym.java

Latte/sym.class: Latte/sym.java

Latte/parser.class: Latte/parser.java Latte/sym.java

Latte/PrettyPrinter.class: Latte/PrettyPrinter.java

clean:
	rm -f Latte/Absyn/*.class Latte/*.class

distclean: vclean

vclean:
	 rm -f Latte/Absyn/Program.java Latte/Absyn/Prog.java Latte/Absyn/TopDef.java Latte/Absyn/FnDef.java Latte/Absyn/ClassDef.java Latte/Absyn/ClassDefE.java Latte/Absyn/ListTopDef.java Latte/Absyn/Arg.java Latte/Absyn/Ar.java Latte/Absyn/ListArg.java Latte/Absyn/FuncDef.java Latte/Absyn/FunDef.java Latte/Absyn/ClassBody.java Latte/Absyn/ClassBlock.java Latte/Absyn/ClassElem.java Latte/Absyn/ClassMeth.java Latte/Absyn/ClassAtr.java Latte/Absyn/ListClassElem.java Latte/Absyn/Block.java Latte/Absyn/Blk.java Latte/Absyn/ListStmt.java Latte/Absyn/Stmt.java Latte/Absyn/Empty.java Latte/Absyn/BStmt.java Latte/Absyn/Decl.java Latte/Absyn/Ass.java Latte/Absyn/Incr.java Latte/Absyn/Decr.java Latte/Absyn/Ret.java Latte/Absyn/VRet.java Latte/Absyn/Cond.java Latte/Absyn/CondElse.java Latte/Absyn/While.java Latte/Absyn/For.java Latte/Absyn/SExp.java Latte/Absyn/Item.java Latte/Absyn/NoInit.java Latte/Absyn/Init.java Latte/Absyn/ListItem.java Latte/Absyn/Type.java Latte/Absyn/Arr.java Latte/Absyn/Int.java Latte/Absyn/Str.java Latte/Absyn/Bool.java Latte/Absyn/Void.java Latte/Absyn/Obj.java Latte/Absyn/Expr.java Latte/Absyn/ECastNull.java Latte/Absyn/EAttrAccess.java Latte/Absyn/EMthCall.java Latte/Absyn/EArrAccess.java Latte/Absyn/EVar.java Latte/Absyn/ENewArr.java Latte/Absyn/ENew.java Latte/Absyn/ELitInt.java Latte/Absyn/ELitTrue.java Latte/Absyn/ELitFalse.java Latte/Absyn/EApp.java Latte/Absyn/EString.java Latte/Absyn/Neg.java Latte/Absyn/Not.java Latte/Absyn/EMul.java Latte/Absyn/EAdd.java Latte/Absyn/ERel.java Latte/Absyn/EAnd.java Latte/Absyn/EOr.java Latte/Absyn/ListExpr.java Latte/Absyn/FunctionCall.java Latte/Absyn/FCall.java Latte/Absyn/ArrAccess.java Latte/Absyn/ArrAcc.java Latte/Absyn/AttrAccess.java Latte/Absyn/AttrAcc.java Latte/Absyn/MethodCall.java Latte/Absyn/MCall.java Latte/Absyn/LValue.java Latte/Absyn/LVFCall.java Latte/Absyn/LVMethodCall.java Latte/Absyn/LVSimpleIdent.java Latte/Absyn/LVEArrAccess.java Latte/Absyn/LVAttrAcc.java Latte/Absyn/AddOp.java Latte/Absyn/Plus.java Latte/Absyn/Minus.java Latte/Absyn/MulOp.java Latte/Absyn/Times.java Latte/Absyn/Div.java Latte/Absyn/Mod.java Latte/Absyn/RelOp.java Latte/Absyn/LTH.java Latte/Absyn/LE.java Latte/Absyn/GTH.java Latte/Absyn/GE.java Latte/Absyn/EQU.java Latte/Absyn/NE.java Latte/Absyn/Program.class Latte/Absyn/Prog.class Latte/Absyn/TopDef.class Latte/Absyn/FnDef.class Latte/Absyn/ClassDef.class Latte/Absyn/ClassDefE.class Latte/Absyn/ListTopDef.class Latte/Absyn/Arg.class Latte/Absyn/Ar.class Latte/Absyn/ListArg.class Latte/Absyn/FuncDef.class Latte/Absyn/FunDef.class Latte/Absyn/ClassBody.class Latte/Absyn/ClassBlock.class Latte/Absyn/ClassElem.class Latte/Absyn/ClassMeth.class Latte/Absyn/ClassAtr.class Latte/Absyn/ListClassElem.class Latte/Absyn/Block.class Latte/Absyn/Blk.class Latte/Absyn/ListStmt.class Latte/Absyn/Stmt.class Latte/Absyn/Empty.class Latte/Absyn/BStmt.class Latte/Absyn/Decl.class Latte/Absyn/Ass.class Latte/Absyn/Incr.class Latte/Absyn/Decr.class Latte/Absyn/Ret.class Latte/Absyn/VRet.class Latte/Absyn/Cond.class Latte/Absyn/CondElse.class Latte/Absyn/While.class Latte/Absyn/For.class Latte/Absyn/SExp.class Latte/Absyn/Item.class Latte/Absyn/NoInit.class Latte/Absyn/Init.class Latte/Absyn/ListItem.class Latte/Absyn/Type.class Latte/Absyn/Arr.class Latte/Absyn/Int.class Latte/Absyn/Str.class Latte/Absyn/Bool.class Latte/Absyn/Void.class Latte/Absyn/Obj.class Latte/Absyn/Expr.class Latte/Absyn/ECastNull.class Latte/Absyn/EAttrAccess.class Latte/Absyn/EMthCall.class Latte/Absyn/EArrAccess.class Latte/Absyn/EVar.class Latte/Absyn/ENewArr.class Latte/Absyn/ENew.class Latte/Absyn/ELitInt.class Latte/Absyn/ELitTrue.class Latte/Absyn/ELitFalse.class Latte/Absyn/EApp.class Latte/Absyn/EString.class Latte/Absyn/Neg.class Latte/Absyn/Not.class Latte/Absyn/EMul.class Latte/Absyn/EAdd.class Latte/Absyn/ERel.class Latte/Absyn/EAnd.class Latte/Absyn/EOr.class Latte/Absyn/ListExpr.class Latte/Absyn/FunctionCall.class Latte/Absyn/FCall.class Latte/Absyn/ArrAccess.class Latte/Absyn/ArrAcc.class Latte/Absyn/AttrAccess.class Latte/Absyn/AttrAcc.class Latte/Absyn/MethodCall.class Latte/Absyn/MCall.class Latte/Absyn/LValue.class Latte/Absyn/LVFCall.class Latte/Absyn/LVMethodCall.class Latte/Absyn/LVSimpleIdent.class Latte/Absyn/LVEArrAccess.class Latte/Absyn/LVAttrAcc.class Latte/Absyn/AddOp.class Latte/Absyn/Plus.class Latte/Absyn/Minus.class Latte/Absyn/MulOp.class Latte/Absyn/Times.class Latte/Absyn/Div.class Latte/Absyn/Mod.class Latte/Absyn/RelOp.class Latte/Absyn/LTH.class Latte/Absyn/LE.class Latte/Absyn/GTH.class Latte/Absyn/GE.class Latte/Absyn/EQU.class Latte/Absyn/NE.class
	 rm -f Latte/Absyn/*.class
	 rmdir Latte/Absyn/
	 rm -f Latte/Yylex Latte/Latte.cup Latte/Yylex.java Latte/VisitSkel.java Latte/ComposVisitor.java Latte/AbstractVisitor.java Latte/FoldVisitor.java Latte/AllVisitor.java Latte/PrettyPrinter.java Latte/Skeleton.java Latte/Test.java Latte/sym.java Latte/parser.java Latte/*.class
	rm -f Makefile
	rmdir -p Latte/

