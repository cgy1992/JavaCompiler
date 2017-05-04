package cop5556sp17.AST;

import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Scanner.Token;


public abstract class Chain extends Statement {
	
	public Chain(Token firstToken) {
		super(firstToken);
	}

	public TypeName type;
	private boolean isLeft = false;
	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}


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
