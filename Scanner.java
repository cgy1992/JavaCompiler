package cop5556sp17;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author vazra
 *
 */
public class Scanner {
	/**
	 * Kind enum
	 */
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}

	/**
	 * Thrown by Scanner when an illegal character is encountered
	 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}

	/**
	 * Thrown by Scanner when an int literal is not a value that can be
	 * represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
		public IllegalNumberException(String message) {
			super(message);
		}
	}

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;

		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
//new class to store tokenValue and linePos corresponding to each token.
	static class TokenAttributes {
		LinePos linePos;
		String tokenValue;

		public LinePos getLinePos() {
			return linePos;
		}

		public void setLinePos(LinePos linePos) {
			this.linePos = linePos;
		}

		public String getTokenValue() {
			return tokenValue;
		}

		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}

	}

	public class Token {
		public final Kind kind;
		public final int pos; // position in input array
		public final int length;

		// returns the text of this Token
		public String getText() {
			String text = null;
			if (kind.text != "") {
				text = kind.text;
			} else {
				if (kind.equals(Kind.IDENT)) {
					text = tokenAttributes.get(pos).getTokenValue().toString();
				} else if (kind.equals(Kind.INT_LIT)) {
					text = tokenAttributes.get(pos).getTokenValue().toString();
				}else if(kind.equals(Kind.EOF)){
					text= "EOF";
				}
				
			}
			return text;

		}

		// returns a LinePos object representing the line and column of this Token
		LinePos getLinePos() {
			LinePos linePos = tokenAttributes.get(pos).getLinePos();
			return linePos;
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/**
		 * Precondition: kind = Kind.INT_LIT, the text can be represented with a
		 * Java int. Note that the validity of the input should have been
		 * checked when the Token was created. So the exception should never be
		 * thrown.
		 * 
		 * @return int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException {
			int numberVal = 0;
			try {
				numberVal = Integer.parseInt(getText().toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return numberVal;
		}
		
		 @Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }
		 
		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }
		  
		  private Scanner getOuterType() {
			   return Scanner.this;
			  }
		
	}

	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();

	}

	/**
	 * Initializes Scanner object by traversing chars and adding tokens to
	 * tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException 
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		int pos = 0;//gives position of next char(incremented after while loop)
		int tokenPos = 0;//gives position of token
		int tokenPosLine = 0;// gives position of token in line
		int posLine = 0;//gives position of char in line(incremented after while loop and reset on encountering \n,\r
		int lineNo = 0;
		int charLen = chars.length();

		while (pos < charLen) {
			char ch = chars.charAt(pos);
			int asciiVal = ch;
			int lowBoundDig = '1';
			int upBoundDig = '9';
			//check for character being alphabet or $ or _ for ident_start
			if (Character.isAlphabetic(ch) || ch == '$' || ch == '_') {
				StringBuilder tokenVal = new StringBuilder();
				tokenVal.append(ch);
				int next = pos + 1;
				if (next < charLen) {
					char chNext = chars.charAt(next);
					// when subsequent characters are alphabets,$,_ and digits for ident_part
					while (Character.isAlphabetic(chNext) || chNext == '$' || chNext == '_'
							|| Character.isDigit(chNext)) {
						tokenVal.append(chNext);
						next++;
						if (next < charLen) {
							chNext = chars.charAt(next);
						} else {
							break;
						}
					}
				}
				pos = pos + tokenVal.length() - 1;
				tokenPos = pos - tokenVal.length() + 1;
				posLine = posLine + tokenVal.length() - 1;
				if (lineNo == 0) {
					tokenPosLine = posLine - tokenVal.length() + 1;
				} else {
					tokenPosLine = posLine - tokenVal.length();
				}
				//all keywords are special cases of ident
				switch (tokenVal.toString()) {
				case "gray":
					tokens.add(new Token(Kind.OP_GRAY, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "convolve":
					tokens.add(new Token(Kind.OP_CONVOLVE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "blur":
					tokens.add(new Token(Kind.OP_BLUR, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "scale":
					tokens.add(new Token(Kind.KW_SCALE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "integer":
					tokens.add(new Token(Kind.KW_INTEGER, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "width":
					tokens.add(new Token(Kind.OP_WIDTH, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "height":
					tokens.add(new Token(Kind.OP_HEIGHT, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "xloc":
					tokens.add(new Token(Kind.KW_XLOC, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "yloc":
					tokens.add(new Token(Kind.KW_YLOC, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "hide":
					tokens.add(new Token(Kind.KW_HIDE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "show":
					tokens.add(new Token(Kind.KW_SHOW, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "move":
					tokens.add(new Token(Kind.KW_MOVE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "boolean":
					tokens.add(new Token(Kind.KW_BOOLEAN, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "image":
					tokens.add(new Token(Kind.KW_IMAGE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "url":
					tokens.add(new Token(Kind.KW_URL, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "file":
					tokens.add(new Token(Kind.KW_FILE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "frame":
					tokens.add(new Token(Kind.KW_FRAME, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "while":
					tokens.add(new Token(Kind.KW_WHILE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "if":
					tokens.add(new Token(Kind.KW_IF, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "sleep":
					tokens.add(new Token(Kind.OP_SLEEP, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "screenheight":
					tokens.add(new Token(Kind.KW_SCREENHEIGHT, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "screenwidth":
					tokens.add(new Token(Kind.KW_SCREENWIDTH, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "true":
					tokens.add(new Token(Kind.KW_TRUE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				case "false":
					tokens.add(new Token(Kind.KW_FALSE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				default:
					tokens.add(new Token(Kind.IDENT, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}

			} else if (ch == '0') {//check if character is 0 which is a terminal so output token
				StringBuilder tokenVal = new StringBuilder();
				tokenVal.append(ch);
				tokenPos = pos - tokenVal.length() + 1;
				posLine = posLine + tokenVal.length() - 1;
				if (lineNo == 0) {
					tokenPosLine = posLine - tokenVal.length() + 1;
				} else {
					tokenPosLine = posLine - tokenVal.length();
				}
				tokens.add(new Token(Kind.INT_LIT, tokenPos, tokenVal.length()));
				updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

			} else if (asciiVal >= lowBoundDig && asciiVal <= upBoundDig) {//output INT_LIT token in the range of integers
				StringBuilder tokenVal = new StringBuilder();
				tokenVal.append(ch);
				int next = pos + 1;
				if (next < charLen) {
					char chNext = chars.charAt(next);
					while (Character.isDigit(chNext)) {
						tokenVal.append(chNext);
						next++;
						if (next < charLen) {
							chNext = chars.charAt(next);
						} else {
							break;
						}
					}
				}
				pos = pos + tokenVal.length() - 1;
				tokenPos = pos - tokenVal.length() + 1;
				posLine = posLine + tokenVal.length() - 1;
				if (lineNo == 0) {
					tokenPosLine = posLine - tokenVal.length() + 1;
				} else {
					tokenPosLine = posLine - tokenVal.length();
				}
				// Token.intVal();
				try {
					Integer.parseInt(tokenVal.toString());
					Token numberVal = new Token(Kind.INT_LIT, tokenPos, tokenVal.length());
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					tokens.add(numberVal);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					throw new IllegalNumberException("Encountered illegal number " + tokenVal.toString() + "at Line No:"
							+ lineNo + "and column no:" + posLine);
				}

			} else if (ch == ';' || ch == ',' || ch == '(' || ch == ')' || ch == '{' || ch == '}') {// for separators

				switch (ch) {

				case ';': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.SEMI, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case ',': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.COMMA, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case '(': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.LPAREN, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case ')': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.RPAREN, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case '{': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.LBRACE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case '}': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.RBRACE, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}

				}
			} else if (ch == '|' || ch == '-' || ch == '&' || ch == '=' || ch == '!' || ch == '<' || ch == '>'
					|| ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '!') {
				switch (ch) {// for operators
				case '|': {
					if (pos + 1 < charLen && chars.charAt(pos + 1) == '-') {

						if (pos + 2 < charLen && chars.charAt(pos + 2) == '>') {

							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch).append(chars.charAt(pos + 1)).append(chars.charAt(pos + 2));
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.BARARROW, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

						} else {
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch);
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.OR, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
						}

					} else {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.OR, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					}
					break;
				}
				case '-': {
					if (pos + 1 < charLen) {
						if (chars.charAt(pos + 1) == '>') {
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch).append(chars.charAt(pos + 1));
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.ARROW, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
						} else {
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch);
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.MINUS, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
						}
					} else {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.MINUS, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					}
					break;
				}
				case '&': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.AND, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
				}
					break;
				case '=': {
					if ((pos + 1) < charLen && chars.charAt(pos + 1) == '=') {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch).append(chars.charAt(pos + 1));
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.EQUAL, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

					} else {
						// tokens.add(new Token())
						throw new IllegalCharException("encountered '=' which is an illegal character at Line No: "
								+ lineNo + "column no: " + tokenPosLine);
					}
					break;
				}

				case '!': {
					StringBuilder tokenVal = new StringBuilder();
					if ((pos + 1) < charLen && chars.charAt(pos + 1) == '=') {

						tokenVal.append(ch).append(chars.charAt(pos + 1));
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.NOTEQUAL, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

					} else {
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.NOT, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

					}
					break;
				}

				case '<': {
					if (pos + 1 < charLen) {
						if (chars.charAt(pos + 1) == '=')

						{
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch).append(chars.charAt(pos + 1));
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.LE, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
						}

						else if (chars.charAt(pos + 1) == '-') {
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch).append(chars.charAt(pos + 1));
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.ASSIGN, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
						} else {
							StringBuilder tokenVal = new StringBuilder();
							tokenVal.append(ch);
							pos = pos + tokenVal.length() - 1;
							tokenPos = pos - tokenVal.length() + 1;
							posLine = posLine + tokenVal.length() - 1;
							if (lineNo == 0) {
								tokenPosLine = posLine - tokenVal.length() + 1;
							} else {
								tokenPosLine = posLine - tokenVal.length();
							}
							tokens.add(new Token(Kind.LT, tokenPos, tokenVal.length()));
							updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

						}
					} else {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.LT, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					}
					break;
				}
				case '>': {
					if ((pos + 1) < charLen && chars.charAt(pos + 1) == '=') {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch).append(chars.charAt(pos + 1));
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.GE, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

					} else {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.GT, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					}

					break;
				}
				case '+': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.PLUS, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case '*': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.TIMES, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				case '/': {
					if (pos + 1 < charLen && chars.charAt(pos + 1) == '*') {
						StringBuilder tokenVal = new StringBuilder();// if the comment is only a line
						StringBuilder tokenValNewLine = new StringBuilder();// for the last line of comment
						StringBuilder tokenValLine = new StringBuilder();// for the /n counts
						StringBuilder countLineStringBuffer = new StringBuilder();//for all lines in between
						int countLineString = 0;
						tokenVal.append(ch).append(chars.charAt(pos + 1));
						int next = pos + 2;
						if (next < charLen) {
							while (next < charLen-1) {
								char chNext = chars.charAt(next);
								if (chNext == '*' && chars.charAt(next + 1) == '/') {
									if (lineNo == 0) {
										tokenVal.append(chNext).append(chars.charAt(next + 1));
									} else {
										tokenValNewLine.append(chNext).append(chars.charAt(next + 1));
									}
									next++;
									break;
								} else {
									if (Character.isWhitespace(chNext)) {
										if (chNext == '\n' || (chNext == '\r'&& chars.charAt(next+1)=='\n')) {
											lineNo++;
											posLine = 0;
											tokenPosLine = 0;
											tokenValLine.append(chNext);
											if(chNext=='\r')
											{
												tokenValLine.append(chars.charAt(next+1));
												next++;
											}
											countLineStringBuffer.append(tokenValNewLine);
											countLineString = countLineString + tokenValNewLine.length() - 1;
											tokenValNewLine = new StringBuilder();
											pos = pos + tokenValLine.length() - 1;
											tokenPos = pos - tokenValLine.length() + 1;

										} else {
											if (lineNo == 0) {
												tokenVal.append(chNext);
												pos = pos + tokenVal.length() - 1;
												tokenPos = pos - tokenVal.length() + 1;
												posLine = posLine + tokenVal.length() - 1;
												tokenPosLine = posLine - tokenVal.length() + 1;

											} else {
												tokenValNewLine.append(chNext);
												pos = pos + tokenValNewLine.length() - 1;
												tokenPos = pos - tokenValNewLine.length() + 1;
												posLine = posLine + countLineString + tokenValNewLine.length();
												tokenPosLine = posLine - tokenValNewLine.length() - countLineString;
											}

										}
									} else {
										if (lineNo == 0) {
											tokenVal.append(chNext);
										} else {
											tokenValNewLine.append(chNext);
										}
									}
								}
								next++;
							}
							if (lineNo == 0) {
								if (!(tokenVal.substring(tokenVal.length() - 2, tokenVal.length()).equals("*/"))) {
									throw new IllegalCharException("Unclosed comments");
								}
							} else {
								String totalTokenVal = tokenVal.toString().trim() + tokenValLine.toString()
										+ countLineStringBuffer.toString() + tokenValNewLine.toString().trim();
								if (!(totalTokenVal.substring((totalTokenVal.length() - 2), (totalTokenVal.length()))
										.equals("*/"))) {
									throw new IllegalCharException("Unclosed comments");
								}
							}
							if (lineNo == 0) {
								//System.out.println(tokenVal.toString());
								pos = next;
								tokenPos = pos - tokenVal.length() + 1;
								posLine = posLine + tokenVal.length() - 1;
								tokenPosLine = posLine - tokenVal.length() + 1;
								//updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo,
										//tokenVal.toString());
							} else {
								pos = next;
								tokenPosLine = tokenValNewLine.length();
								posLine = tokenPosLine;
								//updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, "comment");
							}
							
						}
						
					} else {
						StringBuilder tokenVal = new StringBuilder();
						tokenVal.append(ch);
						pos = pos + tokenVal.length() - 1;
						tokenPos = pos - tokenVal.length() + 1;
						posLine = posLine + tokenVal.length() - 1;
						if (lineNo == 0) {
							tokenPosLine = posLine - tokenVal.length() + 1;
						} else {
							tokenPosLine = posLine - tokenVal.length();
						}
						tokens.add(new Token(Kind.DIV, tokenPos, tokenVal.length()));
						updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());

					}

					break;
				}

				case '%': {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					tokens.add(new Token(Kind.MOD, tokenPos, tokenVal.length()));
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, tokenVal.toString());
					break;
				}
				}
			} else if (Character.isWhitespace(ch)) {
				if (ch == '\n' || (ch == '\r' && chars.charAt(pos+1)=='\n')) {
					lineNo++;
					posLine = 0;
					tokenPosLine = 0;
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					if(ch=='\r')
					{
						tokenVal.append(chars.charAt(pos+1));
					}			
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, "new line");
				} else {
					StringBuilder tokenVal = new StringBuilder();
					tokenVal.append(ch);
					pos = pos + tokenVal.length() - 1;
					tokenPos = pos - tokenVal.length() + 1;
					posLine = posLine + tokenVal.length() - 1;
					if (lineNo == 0) {
						tokenPosLine = posLine - tokenVal.length() + 1;
					} else {
						tokenPosLine = posLine - tokenVal.length();
					}
					updateTokenAttributes(tokens.size(), tokenPos, tokenPosLine, lineNo, "whitespace");
				}
			} else {
				throw new IllegalCharException("Encountered illegal character" + ch + "at" + " Line number " + lineNo
						+ "and" + "column number" + tokenPosLine);
			}
			pos++;
			posLine++;
		}
		StringBuilder tokenVal = new StringBuilder();
		tokenVal.append(Kind.EOF);
		posLine = posLine + tokenVal.length() - 1;
		if (lineNo == 0) {
			tokenPosLine = posLine - tokenVal.length() + 1;
		} else {
			tokenPosLine = posLine - tokenVal.length();
		}
		tokens.add(new Token(Kind.EOF, pos, 0));
		updateTokenAttributes(tokens.size(), pos, tokenPosLine, lineNo, Kind.EOF.text.toString());
		//for (int i = 0; i < tokens.size(); i++) {
			//System.out.println("Token number " + tokens.get(i).getText() + " token position " + tokens.get(i).pos
				//	+ tokens.get(i).kind);
		//	System.out.println("LinePos!!" + getLinePos(tokens.get(i)));
		//}
		return this;
	}

	HashMap<Integer, TokenAttributes> tokenAttributes = new HashMap<Integer, TokenAttributes>();
 //HashMap to store linePos and tokenValue ,key is tokenPos,value is distinctToken
	private HashMap<Integer, TokenAttributes> updateTokenAttributes(int tokenIndex, int tokenPos, int tokenPosLine,
			int lineNo, String tokenValue) {
		LinePos tokenLinePos = new LinePos(lineNo, tokenPosLine);
		TokenAttributes distinctToken = new TokenAttributes();
		distinctToken.setLinePos(tokenLinePos);
		distinctToken.setTokenValue(tokenValue);
		tokenAttributes.put(tokenPos, distinctToken);
		return tokenAttributes;
	}

	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;

	/*
	 * Return the next token in the token list and update the state so that the
	 * next call will return the Token..
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}

	/*
	 * Return the next token in the token list without updating the state. (So
	 * the following call to next will return the same token.)
	 */
	public Token peek() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);
	}

	/**
	 * Returns a LinePos object containing the line and position in line of the
	 * given token.
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		return t.getLinePos();
	}

}
