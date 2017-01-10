package Latte;
import Latte.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  Latte.Absyn.Program.Visitor<Latte.Absyn.Program,A>,
  Latte.Absyn.TopDef.Visitor<Latte.Absyn.TopDef,A>,
  Latte.Absyn.Arg.Visitor<Latte.Absyn.Arg,A>,
  Latte.Absyn.FuncDef.Visitor<Latte.Absyn.FuncDef,A>,
  Latte.Absyn.ClassBody.Visitor<Latte.Absyn.ClassBody,A>,
  Latte.Absyn.ClassElem.Visitor<Latte.Absyn.ClassElem,A>,
  Latte.Absyn.Block.Visitor<Latte.Absyn.Block,A>,
  Latte.Absyn.Stmt.Visitor<Latte.Absyn.Stmt,A>,
  Latte.Absyn.Item.Visitor<Latte.Absyn.Item,A>,
  Latte.Absyn.Type.Visitor<Latte.Absyn.Type,A>,
  Latte.Absyn.Expr.Visitor<Latte.Absyn.Expr,A>,
  Latte.Absyn.FunctionCall.Visitor<Latte.Absyn.FunctionCall,A>,
  Latte.Absyn.ArrAccess.Visitor<Latte.Absyn.ArrAccess,A>,
  Latte.Absyn.AttrAccess.Visitor<Latte.Absyn.AttrAccess,A>,
  Latte.Absyn.MethodCall.Visitor<Latte.Absyn.MethodCall,A>,
  Latte.Absyn.LValue.Visitor<Latte.Absyn.LValue,A>,
  Latte.Absyn.AddOp.Visitor<Latte.Absyn.AddOp,A>,
  Latte.Absyn.MulOp.Visitor<Latte.Absyn.MulOp,A>,
  Latte.Absyn.RelOp.Visitor<Latte.Absyn.RelOp,A>
{
/* Program */
    public Program visit(Latte.Absyn.Prog p, A arg)
    {
      ListTopDef listtopdef_ = new ListTopDef();
      for (TopDef x : p.listtopdef_)
      {
        listtopdef_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.Prog(listtopdef_);
    }
/* TopDef */
    public TopDef visit(Latte.Absyn.FnDef p, A arg)
    {
      FuncDef funcdef_ = p.funcdef_.accept(this, arg);
      return new Latte.Absyn.FnDef(funcdef_);
    }    public TopDef visit(Latte.Absyn.ClassDef p, A arg)
    {
      String ident_ = p.ident_;
      ClassBody classbody_ = p.classbody_.accept(this, arg);
      return new Latte.Absyn.ClassDef(ident_, classbody_);
    }    public TopDef visit(Latte.Absyn.ClassDefE p, A arg)
    {
      String ident_1 = p.ident_1;
      String ident_2 = p.ident_2;
      ClassBody classbody_ = p.classbody_.accept(this, arg);
      return new Latte.Absyn.ClassDefE(ident_1, ident_2, classbody_);
    }
/* Arg */
    public Arg visit(Latte.Absyn.Ar p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      return new Latte.Absyn.Ar(type_, ident_);
    }
/* FuncDef */
    public FuncDef visit(Latte.Absyn.FunDef p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      ListArg listarg_ = new ListArg();
      for (Arg x : p.listarg_)
      {
        listarg_.add(x.accept(this,arg));
      }
      Block block_ = p.block_.accept(this, arg);
      return new Latte.Absyn.FunDef(type_, ident_, listarg_, block_);
    }
/* ClassBody */
    public ClassBody visit(Latte.Absyn.ClassBlock p, A arg)
    {
      ListClassElem listclasselem_ = new ListClassElem();
      for (ClassElem x : p.listclasselem_)
      {
        listclasselem_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.ClassBlock(listclasselem_);
    }
/* ClassElem */
    public ClassElem visit(Latte.Absyn.ClassMeth p, A arg)
    {
      FuncDef funcdef_ = p.funcdef_.accept(this, arg);
      return new Latte.Absyn.ClassMeth(funcdef_);
    }    public ClassElem visit(Latte.Absyn.ClassAtr p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;
      return new Latte.Absyn.ClassAtr(type_, ident_);
    }
/* Block */
    public Block visit(Latte.Absyn.Blk p, A arg)
    {
      ListStmt liststmt_ = new ListStmt();
      for (Stmt x : p.liststmt_)
      {
        liststmt_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.Blk(liststmt_);
    }
/* Stmt */
    public Stmt visit(Latte.Absyn.Empty p, A arg)
    {
      return new Latte.Absyn.Empty();
    }    public Stmt visit(Latte.Absyn.BStmt p, A arg)
    {
      Block block_ = p.block_.accept(this, arg);
      return new Latte.Absyn.BStmt(block_);
    }    public Stmt visit(Latte.Absyn.Decl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      ListItem listitem_ = new ListItem();
      for (Item x : p.listitem_)
      {
        listitem_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.Decl(type_, listitem_);
    }    public Stmt visit(Latte.Absyn.Ass p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Ass(lvalue_, expr_);
    }    public Stmt visit(Latte.Absyn.Incr p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      return new Latte.Absyn.Incr(lvalue_);
    }    public Stmt visit(Latte.Absyn.Decr p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      return new Latte.Absyn.Decr(lvalue_);
    }    public Stmt visit(Latte.Absyn.Ret p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Ret(expr_);
    }    public Stmt visit(Latte.Absyn.VRet p, A arg)
    {
      return new Latte.Absyn.VRet();
    }    public Stmt visit(Latte.Absyn.Cond p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Latte.Absyn.Cond(expr_, stmt_);
    }    public Stmt visit(Latte.Absyn.CondElse p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_1 = p.stmt_1.accept(this, arg);
      Stmt stmt_2 = p.stmt_2.accept(this, arg);
      return new Latte.Absyn.CondElse(expr_, stmt_1, stmt_2);
    }    public Stmt visit(Latte.Absyn.While p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Latte.Absyn.While(expr_, stmt_);
    }    public Stmt visit(Latte.Absyn.For p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_1 = p.ident_1;
      String ident_2 = p.ident_2;
      Stmt stmt_ = p.stmt_.accept(this, arg);
      return new Latte.Absyn.For(type_, ident_1, ident_2, stmt_);
    }    public Stmt visit(Latte.Absyn.SExp p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.SExp(expr_);
    }
/* Item */
    public Item visit(Latte.Absyn.NoInit p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.NoInit(ident_);
    }    public Item visit(Latte.Absyn.Init p, A arg)
    {
      String ident_ = p.ident_;
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Init(ident_, expr_);
    }
/* Type */
    public Type visit(Latte.Absyn.Arr p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      return new Latte.Absyn.Arr(type_);
    }    public Type visit(Latte.Absyn.Int p, A arg)
    {
      return new Latte.Absyn.Int();
    }    public Type visit(Latte.Absyn.Str p, A arg)
    {
      return new Latte.Absyn.Str();
    }    public Type visit(Latte.Absyn.Bool p, A arg)
    {
      return new Latte.Absyn.Bool();
    }    public Type visit(Latte.Absyn.Void p, A arg)
    {
      return new Latte.Absyn.Void();
    }    public Type visit(Latte.Absyn.Obj p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.Obj(ident_);
    }
/* Expr */
    public Expr visit(Latte.Absyn.ECastNull p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      return new Latte.Absyn.ECastNull(type_);
    }    public Expr visit(Latte.Absyn.EAttrAccess p, A arg)
    {
      AttrAccess attraccess_ = p.attraccess_.accept(this, arg);
      return new Latte.Absyn.EAttrAccess(attraccess_);
    }    public Expr visit(Latte.Absyn.EMthCall p, A arg)
    {
      MethodCall methodcall_ = p.methodcall_.accept(this, arg);
      return new Latte.Absyn.EMthCall(methodcall_);
    }    public Expr visit(Latte.Absyn.EArrAccess p, A arg)
    {
      ArrAccess arraccess_ = p.arraccess_.accept(this, arg);
      return new Latte.Absyn.EArrAccess(arraccess_);
    }    public Expr visit(Latte.Absyn.EVar p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.EVar(ident_);
    }    public Expr visit(Latte.Absyn.ENewArr p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.ENewArr(type_, expr_);
    }    public Expr visit(Latte.Absyn.ENew p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.ENew(ident_);
    }    public Expr visit(Latte.Absyn.ELitInt p, A arg)
    {
      Integer integer_ = p.integer_;
      return new Latte.Absyn.ELitInt(integer_);
    }    public Expr visit(Latte.Absyn.ELitTrue p, A arg)
    {
      return new Latte.Absyn.ELitTrue();
    }    public Expr visit(Latte.Absyn.ELitFalse p, A arg)
    {
      return new Latte.Absyn.ELitFalse();
    }    public Expr visit(Latte.Absyn.EApp p, A arg)
    {
      FunctionCall functioncall_ = p.functioncall_.accept(this, arg);
      return new Latte.Absyn.EApp(functioncall_);
    }    public Expr visit(Latte.Absyn.EString p, A arg)
    {
      String string_ = p.string_;
      return new Latte.Absyn.EString(string_);
    }    public Expr visit(Latte.Absyn.Neg p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Neg(expr_);
    }    public Expr visit(Latte.Absyn.Not p, A arg)
    {
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.Not(expr_);
    }    public Expr visit(Latte.Absyn.EMul p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      MulOp mulop_ = p.mulop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EMul(expr_1, mulop_, expr_2);
    }    public Expr visit(Latte.Absyn.EAdd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      AddOp addop_ = p.addop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EAdd(expr_1, addop_, expr_2);
    }    public Expr visit(Latte.Absyn.ERel p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      RelOp relop_ = p.relop_.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.ERel(expr_1, relop_, expr_2);
    }    public Expr visit(Latte.Absyn.EAnd p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EAnd(expr_1, expr_2);
    }    public Expr visit(Latte.Absyn.EOr p, A arg)
    {
      Expr expr_1 = p.expr_1.accept(this, arg);
      Expr expr_2 = p.expr_2.accept(this, arg);
      return new Latte.Absyn.EOr(expr_1, expr_2);
    }
/* FunctionCall */
    public FunctionCall visit(Latte.Absyn.FCall p, A arg)
    {
      String ident_ = p.ident_;
      ListExpr listexpr_ = new ListExpr();
      for (Expr x : p.listexpr_)
      {
        listexpr_.add(x.accept(this,arg));
      }
      return new Latte.Absyn.FCall(ident_, listexpr_);
    }
/* ArrAccess */
    public ArrAccess visit(Latte.Absyn.ArrAcc p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      Expr expr_ = p.expr_.accept(this, arg);
      return new Latte.Absyn.ArrAcc(lvalue_, expr_);
    }
/* AttrAccess */
    public AttrAccess visit(Latte.Absyn.AttrAcc p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      String ident_ = p.ident_;
      return new Latte.Absyn.AttrAcc(lvalue_, ident_);
    }
/* MethodCall */
    public MethodCall visit(Latte.Absyn.MCall p, A arg)
    {
      LValue lvalue_ = p.lvalue_.accept(this, arg);
      FunctionCall functioncall_ = p.functioncall_.accept(this, arg);
      return new Latte.Absyn.MCall(lvalue_, functioncall_);
    }
/* LValue */
    public LValue visit(Latte.Absyn.LVFCall p, A arg)
    {
      FunctionCall functioncall_ = p.functioncall_.accept(this, arg);
      return new Latte.Absyn.LVFCall(functioncall_);
    }    public LValue visit(Latte.Absyn.LVMethodCall p, A arg)
    {
      MethodCall methodcall_ = p.methodcall_.accept(this, arg);
      return new Latte.Absyn.LVMethodCall(methodcall_);
    }    public LValue visit(Latte.Absyn.LVSimpleIdent p, A arg)
    {
      String ident_ = p.ident_;
      return new Latte.Absyn.LVSimpleIdent(ident_);
    }    public LValue visit(Latte.Absyn.LVEArrAccess p, A arg)
    {
      ArrAccess arraccess_ = p.arraccess_.accept(this, arg);
      return new Latte.Absyn.LVEArrAccess(arraccess_);
    }    public LValue visit(Latte.Absyn.LVAttrAcc p, A arg)
    {
      AttrAccess attraccess_ = p.attraccess_.accept(this, arg);
      return new Latte.Absyn.LVAttrAcc(attraccess_);
    }
/* AddOp */
    public AddOp visit(Latte.Absyn.Plus p, A arg)
    {
      return new Latte.Absyn.Plus();
    }    public AddOp visit(Latte.Absyn.Minus p, A arg)
    {
      return new Latte.Absyn.Minus();
    }
/* MulOp */
    public MulOp visit(Latte.Absyn.Times p, A arg)
    {
      return new Latte.Absyn.Times();
    }    public MulOp visit(Latte.Absyn.Div p, A arg)
    {
      return new Latte.Absyn.Div();
    }    public MulOp visit(Latte.Absyn.Mod p, A arg)
    {
      return new Latte.Absyn.Mod();
    }
/* RelOp */
    public RelOp visit(Latte.Absyn.LTH p, A arg)
    {
      return new Latte.Absyn.LTH();
    }    public RelOp visit(Latte.Absyn.LE p, A arg)
    {
      return new Latte.Absyn.LE();
    }    public RelOp visit(Latte.Absyn.GTH p, A arg)
    {
      return new Latte.Absyn.GTH();
    }    public RelOp visit(Latte.Absyn.GE p, A arg)
    {
      return new Latte.Absyn.GE();
    }    public RelOp visit(Latte.Absyn.EQU p, A arg)
    {
      return new Latte.Absyn.EQU();
    }    public RelOp visit(Latte.Absyn.NE p, A arg)
    {
      return new Latte.Absyn.NE();
    }
}