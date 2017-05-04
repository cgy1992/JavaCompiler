package cop5556sp17.AST;

import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Scanner.Token;

public class IdentLValue extends ASTNode {
	
	public IdentLValue(Token firstToken) {
		super(firstToken);
	}
	
	@Override
	public String toString() {
		return "IdentLValue [firstToken=" + firstToken + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIdentLValue(this,arg);
	}

	public String getText() {
		return firstToken.getText();
	}
   
	public Dec dec;

	public Dec getDec() {
		return dec;
	}

	public void setDec(Dec dec) {
		this.dec = dec;
	}
	
	public TypeName type;

	public TypeName getType() {
		return type;
	}

	public void setType(TypeName type) 
	{
		try
		{
			if(type != null)			
				this.type = type;
			else
				this.type = Type.getTypeName(firstToken);
		}
		catch(Exception e)
		{
			//throw new TypeCheckException("Cannot get type of firstToken = " + firstToken.getText());
		}
		
	}
}
