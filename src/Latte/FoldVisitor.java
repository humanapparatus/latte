package Latte;

import Latte.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Program */
    public R visit(Latte.Absyn.Prog p, A arg) {
      R r = leaf(arg);
      for (TopDef x : p.listtopdef_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* TopDef */
    public R visit(Latte.Absyn.FnDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.funcdef_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ClassDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.classbody_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ClassDefE p, A arg) {
      R r = leaf(arg);
      r = combine(p.classbody_.accept(this, arg), r, arg);
      return r;
    }

/* Arg */
    public R visit(Latte.Absyn.Ar p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* FuncDef */
    public R visit(Latte.Absyn.FunDef p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Arg x : p.listarg_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }

/* ClassBody */
    public R visit(Latte.Absyn.ClassBlock p, A arg) {
      R r = leaf(arg);
      for (ClassElem x : p.listclasselem_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* ClassElem */
    public R visit(Latte.Absyn.ClassMeth p, A arg) {
      R r = leaf(arg);
      r = combine(p.funcdef_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ClassAtr p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }

/* Block */
    public R visit(Latte.Absyn.Blk p, A arg) {
      R r = leaf(arg);
      for (Stmt x : p.liststmt_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* Stmt */
    public R visit(Latte.Absyn.Empty p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.BStmt p, A arg) {
      R r = leaf(arg);
      r = combine(p.block_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Decl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      for (Item x : p.listitem_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }
    public R visit(Latte.Absyn.Ass p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Incr p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Decr p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Ret p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.VRet p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Cond p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.CondElse p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_1.accept(this, arg), r, arg);
      r = combine(p.stmt_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.While p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.For p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.stmt_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.SExp p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Item */
    public R visit(Latte.Absyn.NoInit p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Init p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(Latte.Absyn.Arr p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Int p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Str p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Bool p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Void p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Obj p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* Expr */
    public R visit(Latte.Absyn.ECastNull p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EAttrAccess p, A arg) {
      R r = leaf(arg);
      r = combine(p.attraccess_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EMthCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.methodcall_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EArrAccess p, A arg) {
      R r = leaf(arg);
      r = combine(p.arraccess_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ENewArr p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ENew p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitTrue p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.ELitFalse p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.EApp p, A arg) {
      R r = leaf(arg);
      r = combine(p.functioncall_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EString p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Neg p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.Not p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EMul p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.mulop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.addop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.ERel p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.relop_.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EAnd p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.EOr p, A arg) {
      R r = leaf(arg);
      r = combine(p.expr_1.accept(this, arg), r, arg);
      r = combine(p.expr_2.accept(this, arg), r, arg);
      return r;
    }

/* FunctionCall */
    public R visit(Latte.Absyn.FCall p, A arg) {
      R r = leaf(arg);
      for (Expr x : p.listexpr_)
      {
        r = combine(x.accept(this, arg), r, arg);
      }
      return r;
    }

/* ArrAccess */
    public R visit(Latte.Absyn.ArrAcc p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      r = combine(p.expr_.accept(this, arg), r, arg);
      return r;
    }

/* AttrAccess */
    public R visit(Latte.Absyn.AttrAcc p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      return r;
    }

/* MethodCall */
    public R visit(Latte.Absyn.MCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.lvalue_.accept(this, arg), r, arg);
      r = combine(p.functioncall_.accept(this, arg), r, arg);
      return r;
    }

/* LValue */
    public R visit(Latte.Absyn.LVFCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.functioncall_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.LVMethodCall p, A arg) {
      R r = leaf(arg);
      r = combine(p.methodcall_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.LVSimpleIdent p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.LVEArrAccess p, A arg) {
      R r = leaf(arg);
      r = combine(p.arraccess_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Latte.Absyn.LVAttrAcc p, A arg) {
      R r = leaf(arg);
      r = combine(p.attraccess_.accept(this, arg), r, arg);
      return r;
    }

/* AddOp */
    public R visit(Latte.Absyn.Plus p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Minus p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* MulOp */
    public R visit(Latte.Absyn.Times p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Div p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.Mod p, A arg) {
      R r = leaf(arg);
      return r;
    }

/* RelOp */
    public R visit(Latte.Absyn.LTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.LE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.GTH p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.GE p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.EQU p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Latte.Absyn.NE p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
